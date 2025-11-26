package uz.tikoncha_parent.domain.model

data class Policy(
    val ruleId: String = "",
    val policyId: String = "",
    val policyName: String = "",
    val displayName: String = "",
    val isEnabled: Boolean = true,
    val policyType: PolicyType = PolicyType.STUDENT,
    val priority: Int = 100,
    val apps: List<String> = emptyList(),
    val webs: List<String> = emptyList(),
    val activationTime: List<TimeRule>? = null,
    val timeRules: List<TimeRule> = emptyList(),
    val limitRule: List<LimitRule> = emptyList(),
    val locationRule: LocationRule? = null,
)