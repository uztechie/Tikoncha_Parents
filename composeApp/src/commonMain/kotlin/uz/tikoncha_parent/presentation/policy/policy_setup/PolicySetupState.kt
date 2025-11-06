package uz.tikoncha_parent.presentation.policy.policy_setup

import uz.tikoncha_parent.presentation.policy.limit_rule.LimitRuleUi
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleUi

data class PolicySetupState(
    val limitList: List<LimitRuleUi> = emptyList(),
    val timeList: List<TimeRuleUi> = emptyList(),
)
