package uz.tikoncha_parent.presentation.policy.location_rule

import uz.tikoncha_parent.domain.model.LocationRule


data class LocationRuleState(
    val rule: LocationRule? = null,
    val ruleUi: LocationRuleUi? = null,
    val ruleJson: String? = null
)
