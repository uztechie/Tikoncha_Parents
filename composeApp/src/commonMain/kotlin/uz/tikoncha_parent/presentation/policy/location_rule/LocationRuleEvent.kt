package uz.tikoncha_parent.presentation.policy.location_rule

import uz.tikoncha_parent.domain.model.LocationData
import uz.tikoncha_parent.domain.model.LocationRule


sealed class LocationRuleEvent {
    data class SetLocation(
        val locationData: LocationData?,
        val locationRule: LocationRule?,
        val editable: Boolean,
        val policyName: String,
        val isDark: Boolean
    ): LocationRuleEvent()


    data class SetJsonString(val json: String?): LocationRuleEvent()
}