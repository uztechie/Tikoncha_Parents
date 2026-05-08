package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePolicyRequest(
    val name: String,
    val resource_type: String,
    val action: String,
    val priority: Int,
    val packages: List<String>,
    val categories: List<String>,
    val sites: List<String>,
    val features: List<String>,
    val time_rule: List<TimeRuleDto>? = null,
    val limit_rule: List<LimitRuleDto>? = null,
    val location_rule: LocationRuleDto? = null,
    val wifi: List<String>? = null,
    val policy_template: String? = null,

    )

