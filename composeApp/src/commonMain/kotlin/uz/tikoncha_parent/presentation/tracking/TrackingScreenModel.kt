package uz.tikoncha_parent.presentation.tracking

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.location.LOCATION
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.remote.model.permission_status.PermissionStatusRequest
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.model.SubscriptionType
import uz.tikoncha_parent.domain.model.permission_status.PermissionStatusType
import uz.tikoncha_parent.domain.use_case.ChildrenLocationUseCase
import uz.tikoncha_parent.domain.use_case.payment.SubscriptionLimitUseCase
import uz.tikoncha_parent.domain.use_case.permission_status.PermissionStatusUseCase
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.platform.isLocationServiceEnabled
import uz.tikoncha_parent.presentation.map.LatLng
import kotlin.math.PI
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class TrackingScreenModel(
    private val childrenLocationUseCase: ChildrenLocationUseCase,
    private val locationTracker: LocationTracker,
    private val permissionsController: PermissionsController,
    private val subscriptionLimitUseCase: SubscriptionLimitUseCase,
    private val permissionStatusUseCase: PermissionStatusUseCase,
) : StateScreenModel<TrackingState>(TrackingState()) {

    private val _effect = Channel<TrackingEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private var trackingJob: Job? = null
    private var idleStopJob: Job? = null
    private var lastLocation: LatLng? = null

    init {
        loadChildren()
        loadSubscriptionLimits()
        ensureLocation()                    // ⬅️ init'da boshlash
    }

    fun onEvent(event: TrackingEvent) {
        when (event) {
            // ── Location lifecycle (umumiy nuqta) ──
            TrackingEvent.SelfClicked,
            TrackingEvent.RecheckPermission -> ensureLocation(focusSelf = true)

            TrackingEvent.GpsEnabledByUser -> {
                mutableState.update { it.copy(showGpsDialog = false) }
                ensureLocation(focusSelf = true)
            }

            TrackingEvent.RequestLocationPermission -> requestPermission()

            // ── Refresh ──
            TrackingEvent.Refresh -> {
                loadChildren()
                loadSubscriptionLimits()
                ensureLocation()
            }
            TrackingEvent.RetryLocation -> loadChildren()

            // ── Dialog/sheet dismisses ──
            TrackingEvent.DismissGpsDialog ->
                mutableState.update { it.copy(showGpsDialog = false) }
            TrackingEvent.DismissPermissionDialog ->
                mutableState.update {
                    it.copy(permissionDenied = false, permissionDeniedAlways = false)
                }
            TrackingEvent.DismissSubscriptionDialog ->
                mutableState.update { it.copy(showSubscriptionDialog = false) }
            TrackingEvent.DismissPersonSheet ->
                mutableState.update {
                    it.copy(
                        showPersonSheet = false,
                        sheetPerson = null,
                        sheetIssues = emptyList(),
                        isCheckingPermissionStatus = false,
                    )
                }

            TrackingEvent.OpenAppSettings -> screenModelScope.launch {
                _effect.send(TrackingEffect.OpenAppSettings)
            }

            // ── Person/marker click ──
            is TrackingEvent.PersonClicked -> handlePersonClicked(event.personId)
            is TrackingEvent.MarkerClicked -> handleMarkerClicked(event.markerId)
            TrackingEvent.FitAll -> fitAll()

            // ── Misc ──
            is TrackingEvent.OpenYoutubeUrl ->
                _effect.trySend(TrackingEffect.OpenUrl(event.url))
            is TrackingEvent.SetSelfText ->
                mutableState.update { it.copy(selfText = event.text) }

            TrackingEvent.PermissionGranted,
            TrackingEvent.PermissionDenied,
            TrackingEvent.PermissionDeniedAlways -> Unit
        }
    }

    /**
     * Asosiy mantiq.
     * - permission tekshir
     * - GPS tekshir
     * - tracker.start (idempotent)
     * - focusSelf=true bo'lsa kamerani Self'ga move
     *
     * focusSelf=false (init/silent): hech qanday dialog ko'rsatmaydi, faqat tracker boshlanadi.
     * focusSelf=true (user explicit): permission/GPS yo'q bo'lsa dialog ko'rsatadi.
     */
    private fun ensureLocation(focusSelf: Boolean = false) {
        scheduleIdleStop()

        screenModelScope.launch {
            when (permissionsController.getPermissionState(Permission.LOCATION)) {
                PermissionState.Granted -> {
                    if (!isLocationServiceEnabled()) {
                        if (focusSelf) mutableState.update { it.copy(showGpsDialog = true) }
                        return@launch
                    }
                    mutableState.update {
                        it.copy(showGpsDialog = false, userLocationEnabled = true)
                    }
                    startTrackingIfNeeded()
                    if (focusSelf) moveCameraToSelf()
                }
                PermissionState.NotDetermined -> {
                    if (focusSelf) requestPermission()
                }
                PermissionState.Denied, PermissionState.NotGranted -> {
                    if (focusSelf) mutableState.update {
                        it.copy(permissionAsked = true, permissionDenied = true)
                    }
                }
                PermissionState.DeniedAlways -> {
                    if (focusSelf) mutableState.update {
                        it.copy(permissionAsked = true, permissionDeniedAlways = true)
                    }
                }
            }
        }
    }

    private fun requestPermission() {
        screenModelScope.launch {
            try {
                permissionsController.providePermission(Permission.LOCATION)
                mutableState.update {
                    it.copy(permissionDenied = false, permissionDeniedAlways = false)
                }
                ensureLocation(focusSelf = true)
            } catch (_: DeniedAlwaysException) {
                mutableState.update {
                    it.copy(permissionAsked = true, permissionDeniedAlways = true)
                }
            } catch (_: DeniedException) {
                mutableState.update {
                    it.copy(permissionAsked = true, permissionDenied = true)
                }
            }
        }
    }

    private fun startTrackingIfNeeded() {
        if (trackingJob?.isActive == true) return
        trackingJob = screenModelScope.launch {
            runCatching {
                locationTracker.startTracking()
                locationTracker.getLocationsFlow().collect { loc ->
                    val newLoc = LatLng(loc.latitude, loc.longitude)
                    val last = lastLocation
                    if (last != null && haversineMeters(last, newLoc) < 15.0) return@collect
                    lastLocation = newLoc

                    mutableState.update { s ->
                        val newSelf = s.self?.copy(location = newLoc)
                            ?: Person(
                                id = "self",
                                name = s.selfText,
                                avatarUrl = null,
                                location = newLoc,
                                isSelf = true,
                            )
                        s.copy(self = newSelf)
                    }
                }
            }.onFailure { Logger.e("TrackingScreenModel", "GPS error: ${it.message}") }
        }
    }

    private suspend fun moveCameraToSelf() {
        mutableState.update { it.copy(selectedPersonId = "self") }

        val current = state.value.self?.location
        if (current != null) {
            _effect.send(TrackingEffect.MoveCamera(current, zoom = 16f))
            return
        }

        // location null → loading + GPS kut
        mutableState.update { it.copy(isLocatingSelf = true) }
        try {
            for (i in 0 until 20) {
                delay(500)
                val loc = state.value.self?.location
                if (loc != null) {
                    _effect.send(TrackingEffect.MoveCamera(loc, zoom = 16f))
                    return
                }
            }
        } finally {
            mutableState.update { it.copy(isLocatingSelf = false) }
        }
    }

    private fun scheduleIdleStop() {
        idleStopJob?.cancel()
        idleStopJob = screenModelScope.launch {
            delay(5 * 60 * 1000L)
            runCatching { locationTracker.stopTracking() }
            trackingJob?.cancel()
            trackingJob = null
        }
    }

    // ── Person/marker click (o'zgarmaydi, faqat self click ensureLocation chaqiradi) ──

    private fun handlePersonClicked(id: String) {
        val person = state.value.people.firstOrNull { it.id == id } ?: return

        if (person.isSelf) {
            ensureLocation(focusSelf = true)
            return
        }
        if (!hasPlusSubscription(id)) {
            mutableState.update { it.copy(showSubscriptionDialog = true) }
            return
        }
        mutableState.update {
            it.copy(
                selectedPersonId = id,
                sheetPerson = person,
                showPersonSheet = true,
                isCheckingPermissionStatus = true,
            )
        }
        if (person.location != null) {
            screenModelScope.launch {
                _effect.send(TrackingEffect.MoveCamera(person.location, zoom = 16f))
            }
        }
        loadPermissionStatus(person.id)
    }

    private fun handleMarkerClicked(id: String) {
        if (id == "self" || state.value.self?.id == id) {
            ensureLocation(focusSelf = true)
            return
        }
        handlePersonClicked(id)
    }

    private fun loadPermissionStatus(childUserId: String) {
        screenModelScope.launch {
            when (val res = permissionStatusUseCase.invoke(
                PermissionStatusRequest(
                    userId = childUserId,
                    state = PermissionStatusType.LOCATION.name,
                )
            )) {
                is Resource.Success -> mutableState.update {
                    it.copy(isCheckingPermissionStatus = false, sheetIssues = res.data.issues)
                }
                is Resource.Error -> mutableState.update {
                    it.copy(isCheckingPermissionStatus = false, sheetIssues = emptyList())
                }
                else -> Unit
            }
        }
    }

    private fun hasPlusSubscription(childId: String): Boolean {
        val limit = state.value.subscriptionLimits.firstOrNull { it.childId == childId }
            ?: return false
        return limit.subscriptionType != SubscriptionType.FREE
    }

    private fun loadSubscriptionLimits() {
        screenModelScope.launch {
            subscriptionLimitUseCase.invoke()
            mutableState.update { it.copy(subscriptionLimits = AppSettings.subscriptionLimitList) }
        }
    }

    private fun loadChildren() {
        screenModelScope.launch {
            mutableState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val res = childrenLocationUseCase()) {
                is Resource.Success -> {
                    val children = res.data.orEmpty().mapNotNull { it.toPerson() }
                    mutableState.update { it.copy(people = children, isLoading = false) }
                }
                is Resource.Error -> {
                    val msg = res.message ?: "Xatolik"
                    mutableState.update { it.copy(isLoading = false, errorMessage = msg) }
                    _effect.send(TrackingEffect.ShowError(msg))
                }
                else -> Unit
            }
        }
    }

    private fun fitAll() {
        val points = state.value.people.mapNotNull { it.location }
        if (points.isEmpty()) return
        screenModelScope.launch { _effect.send(TrackingEffect.FitBounds(points)) }
    }

    fun openUrl(url: String) {
        screenModelScope.launch { _effect.send(TrackingEffect.OpenUrl(url)) }
    }

    private fun haversineMeters(a: LatLng, b: LatLng): Double {
        val earthR = 6371000.0
        val lat1 = a.lat * PI / 180.0
        val lat2 = b.lat * PI / 180.0
        val dLat = (b.lat - a.lat) * PI / 180.0
        val dLon = (b.lon - a.lon) * PI / 180.0
        val h = sin(dLat / 2).pow(2) + sin(dLon / 2).pow(2) * cos(lat1) * cos(lat2)
        return 2 * earthR * asin(sqrt(h))
    }

    override fun onDispose() {
        trackingJob?.cancel()
        idleStopJob?.cancel()
        runCatching { locationTracker.stopTracking() }
        super.onDispose()
    }
}