package uz.tikoncha_parent.presentation.policy.location_rule

import uz.tikoncha_parent.domain.model.LocationRule
import uz.tikoncha_parent.presentation.map.LatLng

sealed interface LocationRuleEvent {
    data class Init(
        val rule: LocationRule?,
        val canUpdate: Boolean,
    ) : LocationRuleEvent
    data class CameraIdle(val latLng: LatLng) : LocationRuleEvent
    data class RadiusChanged(val meters: Int) : LocationRuleEvent
    data class ReverseChanged(val reverse: Boolean) : LocationRuleEvent
    data object LocateMe : LocationRuleEvent
    data object Save : LocationRuleEvent
    data object Back : LocationRuleEvent

    object RequestLocationPermission : LocationRuleEvent
    object RecheckPermission : LocationRuleEvent
    object GpsEnabledByUser : LocationRuleEvent
    object OpenAppSettings : LocationRuleEvent
    object DismissGpsDialog : LocationRuleEvent
    object DismissPermissionDialog : LocationRuleEvent
    object AutoFocusConsumed : LocationRuleEvent
}