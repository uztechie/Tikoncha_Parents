package uz.tikoncha_parent.presentation.policy.location_rule

import kotlinx.serialization.Serializable
import uz.tikoncha_parent.domain.model.LocationRule

@Serializable
data class LocationRuleUi(
    val language: String,
    val is_dark: Boolean,
    val lat: Double?,
    val lng: Double?,
    val editable: Boolean,
    val policy_name: String,
    val location_rule: LocationRule?,
    val parent: Boolean
)
