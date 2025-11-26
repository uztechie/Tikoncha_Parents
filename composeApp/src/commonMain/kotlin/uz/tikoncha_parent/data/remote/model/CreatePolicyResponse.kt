package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CreatePolicyResponse(
    val success: Boolean,
    val data: CreatePolicyData? = null,
    val error: String? = null,
    val code: Int? = null,
)

@Serializable
data class CreatePolicyData(
    val policy: CreatePolicyDto? = null,
    val rule: CreateRuleDto? = null,
)

@Serializable
data class CreatePolicyDto(
    val id: String,
    val name: String,
    val scope_type: String,            // "SCHOOL" | "STUDENT" | "PARENT_CHILD"
    val scope_id: String,
    val created_by: String,
    val created_at: String,
    val is_active: Boolean)

@Serializable
data class CreateRuleDto(
    val id: String,
    val policy_id: String,
    val name: String?,
    val resource_type: String,
    val action: String,
    val created_at: String,
    val priority: Int,
    val packages: List<String>? = emptyList(),
    val sites: List<String>? = emptyList(),
    val time_rule: List<TimeRuleDto>? = emptyList(),
    val limit_rule: List<LimitRuleDto>? = emptyList(),
    val location_rule: LocationRuleDto? = null,
    val wifi: List<String>? = null,
    val reason: String? = null
)


