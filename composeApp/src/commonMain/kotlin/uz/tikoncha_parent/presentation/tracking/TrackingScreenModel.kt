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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.ChildrenLocationUseCase
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.platform.isLocationServiceEnabled
import uz.tikoncha_parent.presentation.map2.LatLng

class TrackingScreenModel(
    private val childrenLocationUseCase: ChildrenLocationUseCase,
    private val locationTracker: LocationTracker,
    private val permissionsController: PermissionsController
) : StateScreenModel<TrackingState>(TrackingState()) {

    private val _effect = Channel<TrackingEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        // FAQAT bolalarni yuklash. Permission birinchi kirishda so'ralmaydi!
        loadChildren()
    }

    fun onEvent(event: TrackingEvent) {
        when (event) {
            TrackingEvent.Refresh -> loadChildren()

            TrackingEvent.SelfClicked ->  startSelfLocationFlow()
            is TrackingEvent.PersonClicked -> handlePersonClicked(event.personId)
            is TrackingEvent.MarkerClicked -> selectPerson(event.markerId)
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
            // Bu event'lar endi ScreenModel ichida ishlatilmaydi
            TrackingEvent.PermissionGranted,
            TrackingEvent.PermissionDenied,
            TrackingEvent.PermissionDeniedAlways -> Unit

            TrackingEvent.DismissPermissionDialog -> {
                // Dialog yopildi — flag'larni tozalash
                mutableState.update {
                    it.copy(
                        permissionDenied = false,
                        permissionDeniedAlways = false
                    )
                }
            }
            TrackingEvent.RecheckPermission -> {
                // Settings'dan qaytdi — permission'ni qayta tekshirish
                recheckPermissionAfterSettings()
            }

        }
    }

    /**
     * Person card bosilganda chaqiriladi.
     * - Self bo'lsa: permission/GPS flow ishga tushadi
     * - Bola bo'lsa: faqat tanlash + kameraga fokus
     */
    private fun recheckPermissionAfterSettings() {
        screenModelScope.launch {
            val ps = permissionsController.getPermissionState(Permission.LOCATION)
            Logger.d("TrackingScreenModel", "recheck after settings: $ps")

            when (ps) {
                PermissionState.Granted -> {
                    mutableState.update {
                        it.copy(permissionDenied = false, permissionDeniedAlways = false)
                    }
                    checkGpsAndStart(focusOnSelf = true)  // ← GPS'ni ham tekshiradi
                }
                else -> Unit
            }
        }
    }

    private fun handlePersonClicked(id: String) {
        val person = state.value.people.firstOrNull { it.id == id } ?: return
        selectPerson(id)
    }

    /**
     * "Siz" card bosilganda boshlanadigan flow.
     * Permission yo'q bo'lsa — so'raydi.
     * GPS o'chiq bo'lsa — dialog.
     * Hammasi ok bo'lsa — location oladi va mapda fokus qiladi.
     */
    private fun startSelfLocationFlow() {
        screenModelScope.launch {
            val ps = permissionsController.getPermissionState(Permission.LOCATION)
            Logger.d("TrackingScreenModel", "self click → permission=$ps")

            when (ps) {
                PermissionState.Granted -> {
                    // Ruxsat bor → GPS tekshirish va location olish
                    checkGpsAndStart(focusOnSelf = true)
                }
                PermissionState.NotDetermined -> {
                    // Birinchi marta — permission so'rash
                    requestPermission()
                }
                PermissionState.Denied, PermissionState.NotGranted -> {
                    // Rad etilgan — qayta so'rash dialog'ini ochish
                    mutableState.update {
                        it.copy(permissionAsked = true, permissionDenied = true)
                    }
                }
                PermissionState.DeniedAlways -> {
                    // "Don't ask again" — Settings'ga yo'naltirish dialog
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
                // Berildi! Dialog'larni yopish va davom etish
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
                    it.copy(
                        showGpsDialog = false,
                        userLocationEnabled = true   // ← Yandex layer ni yoqish
                    )
                }
                if (focusOnSelf) {
                    _effect.send(TrackingEffect.MoveToUserLocation)  // ← Kameraga buyruq
                }
            } else {
                mutableState.update { it.copy(showGpsDialog = true) }
            }
        }
    }

    private fun startLocationTracking(focusOnSelf: Boolean) {
        screenModelScope.launch {
            try {
                locationTracker.startTracking()
                val loc = locationTracker.getLocationsFlow()
                    .filterNotNull()
                    .first()

                Logger.d("TrackingScreenModel", "self location: ${loc.latitude}, ${loc.longitude}")

                val newSelfLocation = LatLng(loc.latitude, loc.longitude)

                // Self'ni yangilash — placeholder o'rnida endi to'liq location bor
                mutableState.update { state ->
                    val updatedPeople = state.people.map { person ->
                        if (person.isSelf) {
                            person.copy(location = newSelfLocation)
                        } else {
                            person
                        }
                    }
                    state.copy(
                        people = updatedPeople,
                        selectedPersonId = if (focusOnSelf) "self" else state.selectedPersonId
                    )
                }

                locationTracker.stopTracking()

                // Avtomatik mapga fokus qilish
                if (focusOnSelf) {
                    _effect.send(TrackingEffect.MoveCamera(newSelfLocation, zoom = 16f))
                }
            } catch (e: Exception) {
                Logger.d("TrackingScreenModel", "location error: ${e.message}")
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
                        // Self'ni saqlab, faqat bolalar yangilanadi
                        state.copy(
                            people = children,
                            isLoading = false
                        )
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