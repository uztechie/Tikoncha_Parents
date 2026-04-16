package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CreatePolicyRequest(
    val policy_name: String = "",
    val rule_name: String,
    val scope_type: String,
    val scope_id: String?,
    val policy_is_active: Boolean,
    val resource_type: String,
    val action: String,
    val priority: Int,
    val packages: List<String>,
    val categories: List<String>,
    val sites: List<String>,
    val time_rule: List<TimeRuleDto>? = null,
    val limit_rule: List<LimitRuleDto>? = null,
    val location_rule: LocationRuleDto? = null,
    val wifi: List<String>? = null

)

