package uz.tikoncha_parent.presentation.policy.shared

import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.policy.limit_rule.LimitRuleUi
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleUi

data class PolicySharedState(
    val limitList: List<LimitRuleUi> = emptyList(),
    val timeList: List<TimeRuleUi> = emptyList(),
    val selectedChild: UserInfo? = null
)
