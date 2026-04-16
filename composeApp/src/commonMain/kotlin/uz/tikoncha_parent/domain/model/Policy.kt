package uz.tikoncha_parent.domain.model

import kotlinx.serialization.Serializable
import uz.tikoncha_parent.domain.model.policy.PolicyAction


@Serializable
data class Policy(
    val ruleId: String = "",
    val policyId: String = "",
    val policyName: String = "",
    val displayName: String = "",
    val action: PolicyAction = PolicyAction.DENY,
    val isEnabled: Boolean = true,
    val policyType: PolicyType = PolicyType.PARENT_CHILD,
    val priority: Int = 100,
    val categories: List<String> = emptyList(),
    val apps: List<String> = emptyList(),
    val sites: List<String> = emptyList(),
    val activationTime: List<TimeRule>? = null,
    val timeRules: List<TimeRule> = emptyList(),
    val limitRule: List<LimitRule> = emptyList(),
    val locationRule: LocationRule? = null,
)