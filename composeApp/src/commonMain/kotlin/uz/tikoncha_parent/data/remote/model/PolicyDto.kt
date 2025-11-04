package uz.tikoncha_parent.data.remote.model

data class PolicyDto(
    val policy_id: String,
    val rule_id: String,
    val policy_name: String,
    val scope_type: String,
    val scope_id: String,
    val policy_is_active: Boolean,
    val resource_type: String,
    val action: String,
    val priority: String,
    val packages: List<String>?,
    val sites: List<String>?,
    val time_rule: List<TimeRuleDto>?,
    val limit_rule: List<LimitRuleDto>?,
    val location_rule: List<LocationRuleDto>?,
    val wifi: List<String>?,
    )
