package uz.tikoncha_parent.presentation.policy.location_rule

import uz.tikoncha_parent.domain.model.LocationRule
import uz.tikoncha_parent.presentation.map.LatLng

sealed interface LocationRuleEffect {
    data class MoveCamera(val target: LatLng, val zoom: Float = 16f) : LocationRuleEffect
    data class FitBounds(val points: List<LatLng>) : LocationRuleEffect
    data class SaveResult(val rule: LocationRule) : LocationRuleEffect
    data object NavigateBack : LocationRuleEffect
    object OpenAppSettings : LocationRuleEffect
}