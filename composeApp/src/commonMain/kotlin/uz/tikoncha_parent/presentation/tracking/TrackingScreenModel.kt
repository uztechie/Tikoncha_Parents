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
import kotlinx.coroutines.channels.Channel
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

class TrackingScreenModel(
    private val childrenLocationUseCase: ChildrenLocationUseCase,
    private val locationTracker: LocationTracker,
    private val permissionsController: PermissionsController,
    private val subscriptionLimitUseCase: SubscriptionLimitUseCase,
    private val permissionStatusUseCase: PermissionStatusUseCase
) : StateScreenModel<TrackingState>(TrackingState()) {

    private val _effect = Channel<TrackingEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        loadChildren()
        loadSubscriptionLimits()  // ⬅️ YANGI
    }

    fun onEvent(event: TrackingEvent) {
        when (event) {
            TrackingEvent.Refresh -> {
                loadChildren()
                loadSubscriptionLimits()
            }

            TrackingEvent.SelfClicked -> startSelfLocationFlow()
            is TrackingEvent.PersonClicked -> handlePersonClicked(event.personId)
            is TrackingEvent.MarkerClicked -> handleMarkerClicked(event.markerId)
            TrackingEvent.FitAll -> fitAll()

            TrackingEvent.RequestLocationPermission -> requestPermission()

            TrackingEvent.GpsEnabledByUser -> {
                mutableState.update { it.copy(showGpsDialog = false) }
                checkGpsAndStart(focusOnSelf = true)
            }
            TrackingEvent.DismissGpsDialog -> {
                mutableState.update { it.copy(showGpsDialog = false) }
            }
            TrackingEvent.OpenAppSettings -> {
                screenModelScope.launch {
                    _effect.send(TrackingEffect.OpenAppSettings)
                }
            }
            TrackingEvent.PermissionGranted,
            TrackingEvent.PermissionDenied,
            TrackingEvent.PermissionDeniedAlways -> Unit

            TrackingEvent.DismissPermissionDialog -> {
                mutableState.update {
                    it.copy(
                        permissionDenied = false,
                        permissionDeniedAlways = false
                    )
                }
            }
            TrackingEvent.RecheckPermission -> recheckPermissionAfterSettings()

            // ⬇️ YANGI
            TrackingEvent.DismissSubscriptionDialog -> {
                mutableState.update { it.copy(showSubscriptionDialog = false) }
            }

            TrackingEvent.DismissPersonSheet -> {
                mutableState.update {
                    it.copy(
                        showPersonSheet = false,
                        sheetPerson = null,
                        sheetIssues = emptyList(),
                        isCheckingPermissionStatus = false
                    )
                }
            }



            TrackingEvent.RetryLocation -> {
                loadChildren()
            }

            is TrackingEvent.OpenYoutubeUrl -> {
                _effect.trySend(TrackingEffect.OpenUrl(event.url))
            }
        }
    }

    private fun handlePersonClicked(id: String) {
        val person = state.value.people.firstOrNull { it.id == id } ?: return

        if (person.isSelf) {
            startSelfLocationFlow()
            return
        }

        // FREE — subscription dialog
        if (!hasPlusSubscription(id)) {
            mutableState.update { it.copy(showSubscriptionDialog = true) }
            return
        }

        // PLUS — sheet ochiladi (location bor yoki yo'q — sheet ichida hal qilinadi)
        mutableState.update {
            it.copy(
                selectedPersonId = id,
                sheetPerson = person,
                showPersonSheet = true,
                isCheckingPermissionStatus = true,
            )
        }

        // Location bor bo'lsa kamerani zoom qilamiz
        if (person.location != null) {
            screenModelScope.launch {
                _effect.send(TrackingEffect.MoveCamera(person.location, zoom = 16f))
            }
        }

        loadPermissionStatus(person.id)
    }

    private fun handleMarkerClicked(id: String) {
        handlePersonClicked(id)  // bir xil mantiq
    }


    private fun loadPermissionStatus(childUserId: String) {
        screenModelScope.launch {
            val res = permissionStatusUseCase.invoke(
                PermissionStatusRequest(
                    userId = childUserId,
                    state = PermissionStatusType.LOCATION.name
                )
            )
            when (res) {
                is Resource.Success -> {
                    mutableState.update {
                        it.copy(
                            isCheckingPermissionStatus = false,
                            sheetIssues = res.data.issues
                        )
                    }
                }
                is Resource.Error -> {
                    // Xato — issue ko'rsatmaymiz, loading'ni yopamiz
                    mutableState.update {
                        it.copy(
                            isCheckingPermissionStatus = false,
                            sheetIssues = emptyList()
                        )
                    }
                }
                else -> Unit
            }
        }
    }

    fun openUrl(url: String) {
        screenModelScope.launch {
            _effect.send(TrackingEffect.OpenUrl(url))
        }
    }
    /**
     * Bola PLUS obunaga ega ekanligini tekshiradi.
     */
    private fun hasPlusSubscription(childId: String): Boolean {
        val limit = state.value.subscriptionLimits.firstOrNull { it.childId == childId }
            ?: return false  // limit topilmasa — FREE deb hisoblanadi

        return limit.subscriptionType != SubscriptionType.FREE
    }

    private fun loadSubscriptionLimits() {
        screenModelScope.launch {
            // 1. Server'dan yangilash
            subscriptionLimitUseCase.invoke()

            // 2. AppSettings'dan o'qish (use case yangilangandan keyin)
            mutableState.update {
                it.copy(subscriptionLimits = AppSettings.subscriptionLimitList)
            }
            Logger.d(
                "TrackingScreenModel",
                "subscriptionLimits = ${state.value.subscriptionLimits}"
            )
        }
    }

    // ... qolgan kod o'zgarmaydi (recheckPermissionAfterSettings, startSelfLocationFlow, etc) ...

    private fun recheckPermissionAfterSettings() {
        screenModelScope.launch {
            val ps = permissionsController.getPermissionState(Permission.LOCATION)
            Logger.d("TrackingScreenModel", "recheck after settings: $ps")
            when (ps) {
                PermissionState.Granted -> {
                    mutableState.update {
                        it.copy(permissionDenied = false, permissionDeniedAlways = false)
                    }
                    checkGpsAndStart(focusOnSelf = true)
                }
                else -> Unit
            }
        }
    }

    private fun startSelfLocationFlow() {
        screenModelScope.launch {
            val ps = permissionsController.getPermissionState(Permission.LOCATION)
            when (ps) {
                PermissionState.Granted -> checkGpsAndStart(focusOnSelf = true)
                PermissionState.NotDetermined -> requestPermission()
                PermissionState.Denied, PermissionState.NotGranted -> {
                    mutableState.update {
                        it.copy(permissionAsked = true, permissionDenied = true)
                    }
                }
                PermissionState.DeniedAlways -> {
                    mutableState.update {
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
                checkGpsAndStart(focusOnSelf = true)
            } catch (e: DeniedAlwaysException) {
                mutableState.update {
                    it.copy(permissionAsked = true, permissionDeniedAlways = true)
                }
            } catch (e: DeniedException) {
                mutableState.update {
                    it.copy(permissionAsked = true, permissionDenied = true)
                }
            }
        }
    }

    private fun checkGpsAndStart(focusOnSelf: Boolean) {
        screenModelScope.launch {
            val gpsOn = isLocationServiceEnabled()
            if (gpsOn) {
                mutableState.update {
                    it.copy(showGpsDialog = false, userLocationEnabled = true)
                }
                if (focusOnSelf) {
                    _effect.send(TrackingEffect.MoveToUserLocation)
                }
            } else {
                mutableState.update { it.copy(showGpsDialog = true) }
            }
        }
    }

    private fun loadChildren() {
        screenModelScope.launch {
            mutableState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val res = childrenLocationUseCase()) {
                is Resource.Success -> {
                    val children = res.data.orEmpty().mapNotNull { it.toPerson() }
                    mutableState.update { state ->
                        state.copy(people = children, isLoading = false)
                    }
                }
                is Resource.Error -> {
                    val msg = res.message ?: "Xatolik"
                    mutableState.update {
                        it.copy(isLoading = false, errorMessage = msg)
                    }
                    _effect.send(TrackingEffect.ShowError(msg))
                }
                else -> Unit
            }
        }
    }

    private fun selectPerson(id: String) {
        val person = state.value.people.firstOrNull { it.id == id } ?: return
        val location = person.location ?: return
        if (!hasPlusSubscription(id)) return
        mutableState.update { it.copy(selectedPersonId = id) }
        screenModelScope.launch {
            _effect.send(TrackingEffect.MoveCamera(location, zoom = 16f))
        }
    }

    private fun fitAll() {
        val points = state.value.people.mapNotNull { it.location }
        if (points.isEmpty()) return
        screenModelScope.launch {
            _effect.send(TrackingEffect.FitBounds(points))
        }
    }

    override fun onDispose() {
        locationTracker.stopTracking()
        super.onDispose()
    }
}