package uz.tikoncha_parent.presentation.policy.location_rule

import cafe.adriel.voyager.core.model.ScreenModel
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.domain.model.GeoType
import uz.tikoncha_parent.domain.model.LocationRule
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.platform.isLocationServiceEnabled
import uz.tikoncha_parent.presentation.map2.LatLng
import kotlin.math.PI
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class LocationRuleScreenModel(
    private val locationTracker: LocationTracker,
    private val permissionsController: PermissionsController,   // ⬅️ YANGI inject
) : ScreenModel {

    private val _state = MutableStateFlow(LocationRuleState())
    val state = _state.asStateFlow()

    private val _effect = Channel<LocationRuleEffect>(Channel.BUFFERED)
    val effect: Flow<LocationRuleEffect> = _effect.receiveAsFlow()

    private var initialized = false
    private var trackingJob: Job? = null
    private var idleStopJob: Job? = null
    private var lastLocation: LatLng? = null

    fun onEvent(event: LocationRuleEvent) {
        when (event) {
            is LocationRuleEvent.Init -> handleInit(event.rule, event.canUpdate)
            is LocationRuleEvent.CameraIdle -> {
                val s = _state.value
                if (s.canUpdate && s.geoType == GeoType.CIRCLE) {
                    _state.update { it.copy(center = event.latLng) }
                }
            }
            is LocationRuleEvent.RadiusChanged ->
                if (_state.value.canUpdate) _state.update { it.copy(radiusMeters = event.meters) }
            is LocationRuleEvent.ReverseChanged ->
                if (_state.value.canUpdate) _state.update { it.copy(reverse = event.reverse) }

            LocationRuleEvent.LocateMe -> ensureLocation(focusSelf = true)
            LocationRuleEvent.RecheckPermission -> ensureLocation(focusSelf = true)
            LocationRuleEvent.RequestLocationPermission -> requestPermission()

            LocationRuleEvent.GpsEnabledByUser -> {
                _state.update { it.copy(showGpsDialog = false) }
                ensureLocation(focusSelf = true)
            }
            LocationRuleEvent.OpenAppSettings -> screenModelScope.launch {
                _effect.send(LocationRuleEffect.OpenAppSettings)
            }
            LocationRuleEvent.DismissGpsDialog ->
                _state.update { it.copy(showGpsDialog = false) }
            LocationRuleEvent.DismissPermissionDialog ->
                _state.update {
                    it.copy(permissionDenied = false, permissionDeniedAlways = false)
                }

            LocationRuleEvent.Save -> save()
            LocationRuleEvent.Back -> screenModelScope.launch {
                _effect.send(LocationRuleEffect.NavigateBack)
            }

            LocationRuleEvent.AutoFocusConsumed -> _state.update {
                it.copy(autoFocusTarget = null, autoFitBoundsTarget = null)
            }
        }
    }

    private fun handleInit(rule: LocationRule?, canUpdate: Boolean) {
        if (initialized) return
        initialized = true

        _state.update {
            it.copy(
                canUpdate = canUpdate,
                geoType = rule?.geoType ?: GeoType.CIRCLE,
                center = if (rule?.centerLat != null && rule.centerLng != null) {
                    LatLng(rule.centerLat, rule.centerLng)
                } else null,
                radiusMeters = rule?.radiusMeters ?: 200,
                polygon = rule?.polygon ?: emptyList(),
                reverse = rule?.reverse ?: false,
            )
        }

        // Eski rule → kamerani rule'ga move
        if (rule != null) {
            when (rule.geoType) {
                GeoType.CIRCLE -> _state.value.center?.let { center ->
                    _state.update { it.copy(autoFocusTarget = center) }
                }
                GeoType.POLYGON -> {
                    val pts = _state.value.polygon
                    if (pts.size >= 3) {
                        screenModelScope.launch {
                            _effect.send(
                                LocationRuleEffect.FitBounds(pts.map { LatLng(it.lat, it.lng) })
                            )
                        }
                    }
                }
            }
        }

        if (canUpdate) {
            // ⬇️ MUHIM
            // Yangi rule → focusSelf=true (init paytida kamera Self'ga move)
            // Eski rule → focusSelf=false (silent — kamera allaqachon rule'da)
            ensureLocation(focusSelf = rule == null)
        }
    }

    /** TrackingScreenModel bilan bir xil mantiq */
    private fun ensureLocation(focusSelf: Boolean = false) {
        scheduleIdleStop()

        screenModelScope.launch {
            when (permissionsController.getPermissionState(Permission.LOCATION)) {
                PermissionState.Granted -> {
                    if (!isLocationServiceEnabled()) {
                        if (focusSelf) _state.update { it.copy(showGpsDialog = true) }
                        return@launch
                    }
                    _state.update { it.copy(showGpsDialog = false) }
                    startTrackingIfNeeded()
                    if (focusSelf) moveCameraToSelf()
                }
                PermissionState.NotDetermined -> {
                    if (focusSelf) requestPermission()
                }
                PermissionState.Denied, PermissionState.NotGranted -> {
                    if (focusSelf) _state.update {
                        it.copy(permissionAsked = true, permissionDenied = true)
                    }
                }
                PermissionState.DeniedAlways -> {
                    if (focusSelf) _state.update {
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
                _state.update {
                    it.copy(permissionDenied = false, permissionDeniedAlways = false)
                }
                ensureLocation(focusSelf = true)
            } catch (_: DeniedAlwaysException) {
                _state.update {
                    it.copy(permissionAsked = true, permissionDeniedAlways = true)
                }
            } catch (_: DeniedException) {
                _state.update {
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

                    _state.update { s ->
                        s.copy(
                            userLocation = newLoc,
                            // Yangi rule + center yo'q + CIRCLE → birinchi GPS center bo'ladi
                            center = if (s.center == null && s.geoType == GeoType.CIRCLE) newLoc else s.center,
                        )
                    }
                }
            }.onFailure { Logger.e("LocationRuleScreenModel", "GPS error: ${it.message}") }
        }
    }

    private suspend fun moveCameraToSelf() {
        val current = _state.value.userLocation
        if (current != null) {
            _state.update { it.copy(autoFocusTarget = current) }   // ⬅️ state
            return
        }

        _state.update { it.copy(isLocating = true) }
        try {
            for (i in 0 until 20) {
                delay(500)
                val loc = _state.value.userLocation
                if (loc != null) {
                    _state.update { it.copy(autoFocusTarget = loc) }   // ⬅️ state
                    return
                }
            }
        } finally {
            _state.update { it.copy(isLocating = false) }
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

    private fun save() {
        val s = _state.value
        if (!s.canSave) return

        val rule = when (s.geoType) {
            GeoType.CIRCLE -> LocationRule(
                geoType = GeoType.CIRCLE,
                centerLat = s.center!!.lat,
                centerLng = s.center.lon,
                radiusMeters = s.radiusMeters,
                polygon = null,
                reverse = s.reverse,
            )
            GeoType.POLYGON -> LocationRule(
                geoType = GeoType.POLYGON,
                centerLat = null,
                centerLng = null,
                radiusMeters = null,
                polygon = s.polygon,
                reverse = s.reverse,
            )
        }
        screenModelScope.launch { _effect.send(LocationRuleEffect.SaveResult(rule)) }
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