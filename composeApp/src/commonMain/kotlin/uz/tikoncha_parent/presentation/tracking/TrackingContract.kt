package uz.tikoncha_parent.presentation.tracking

import androidx.compose.runtime.Immutable
import uz.tikoncha_parent.presentation.map2.LatLng


@Immutable
data class Person(
    val id: String,
    val name: String,
    val location: LatLng?,
    val isSelf: Boolean,
    val lastSeenEpochMs: Long? = null,
    val lastSeen: String = "",
    val avatarUrl: String? = null
)

@Immutable
data class TrackingState(
    val people: List<Person> = listOf(),
    val self: Person? = null,
    val selectedPersonId: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val permissionAsked: Boolean = false,           // avval so'ralganmi
    val permissionDenied: Boolean = false,          // bekor qilingan
    val permissionDeniedAlways: Boolean = false,    // "Don't ask again" yoki Settings'dan
    val showGpsDialog: Boolean = false,             // GPS o'chiq — dialog ko'rsatish
    val showPermissionRationale: Boolean = false,    // permissio
    val userLocationEnabled: Boolean = false
) {

}

sealed interface TrackingEvent {
    data object Refresh : TrackingEvent
    data object SelfClicked : TrackingEvent
    data class PersonClicked(val personId: String) : TrackingEvent
    data class MarkerClicked(val markerId: String) : TrackingEvent
    data object FitAll : TrackingEvent

    data object RequestLocationPermission : TrackingEvent
    data object PermissionGranted : TrackingEvent
    data object PermissionDenied : TrackingEvent
    data object PermissionDeniedAlways : TrackingEvent
    data object GpsEnabledByUser : TrackingEvent
    data object DismissGpsDialog : TrackingEvent
    data object OpenAppSettings : TrackingEvent
    data object DismissPermissionDialog : TrackingEvent
    data object RecheckPermission : TrackingEvent
}

sealed interface TrackingEffect {
    data class MoveCamera(val target: LatLng, val zoom: Float = 16f) : TrackingEffect
    data class FitBounds(val points: List<LatLng>) : TrackingEffect
    data class ShowError(val message: String) : TrackingEffect

    data object RequestPermission : TrackingEffect       // UI permission dialog ochishi kerak
    data object OpenAppSettings : TrackingEffect         // Settings ekraniga o'tkazish
    data object OpenLocationSettings : TrackingEffect    // GPS yoqish ekraniga o'tkazish
    data object MoveToUserLocation: TrackingEffect    // GPS yoqish ekraniga o'tkazish

}