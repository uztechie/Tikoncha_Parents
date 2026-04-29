package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable


@Serializable
data class PolicyResponse(
    val success: Boolean,
    val data: PolicyData?,
    val error: String?,
    val code: Int
)

@Serializable
data class PolicyData(
    val policies:List<PolicyDto>
)
@Serializable
data class PolicyDto(
    val policy_name: String,
    val rule_name: String?,
    val rule_id: String,
    val policy_id: String,
    val scope_type: String,            // "SCHOOL" | "STUDENT" | "PARENT_CHILD"
    val scope_id: String,
    val policy_is_active: Boolean,
    val resource_type: String,         // "APP" | "SITE" | ...
    val action: String,                // "ALLOW" | "DENY" ...
    val priority: Int,
    val packages: List<String>? = emptyList(),
    val sites: List<String>? = emptyList(),
    val categories: List<String>? = emptyList(),
    val time_rule: List<TimeRuleDto>? = emptyList(),
    val limit_rule: List<LimitRuleDto>? = emptyList(),
    val location_rule: LocationRuleDto? = null,
    val wifi: List<String>? = null,
    val reason: String? = null,
    val policy_template: String? = null,
)

@Serializable
data class TimeRuleDto(
    val start_time: Int?,   // minutes-from-midnight, e.g. "620"
    val end_time: Int?,     // minutes-from-midnight, e.g. "720"
    val time_include: Boolean, // true => faqat shu oraliqda, false => shu oraliqdan tashqarida
    val days: List<Int> = emptyList() // ISO: Mon=1 ... Sun=7
)

@Serializable
data class LimitRuleDto(
    val limit_amount: Int,           // minutes
    val limit_type: String,          // "DAY" yoki "HOUR"
    val days: List<Int> = emptyList()
)

@Serializable
data class LocationRuleDto(
    val polygon: List<List<Double>>? = emptyList(), // [ [lat, lng], ... ]
    val circle_radius: Double? = null,               // meters
    val center_latitude: Double? = null,               // meters
    val center_longitude: Double? = null,               // meters
    val location_include: Boolean,                // true => ichida, false => tashqarida
    val type: String? = null                      // "POLYGON" yoki "CIRCLE"
)
