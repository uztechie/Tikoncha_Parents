package uz.tikoncha_parent.presentation.policy

import uz.tikoncha_parent.domain.model.PolicyType
import uz.tikoncha_parent.presentation.policy.limit_rule.LimitRuleUi
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleUi

data class PolicyItemUi(
    val policyId: String,
    val policyName: String,
    val isMine: Boolean,
    val appCount: Int,
    val webCount: Int,
    val hasTimeRule: Boolean,
    val hasLimitRule: Boolean,
    val hasLocationRule: Boolean,
    val policyType: PolicyType,
    val isActive: Boolean,
    val packages: List<String>,
    val sites: List<String>,
    val timeRule: List<TimeRuleUi>,
    val limitRule: List<LimitRuleUi>,
)
