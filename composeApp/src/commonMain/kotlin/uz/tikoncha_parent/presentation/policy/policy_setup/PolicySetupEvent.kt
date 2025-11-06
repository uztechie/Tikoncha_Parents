package uz.tikoncha_parent.presentation.policy.policy_setup

import uz.tikoncha_parent.presentation.policy.limit_rule.LimitRuleUi
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleUi

sealed interface PolicySetupEvent {

    data class SetLimitRule(val list: List<LimitRuleUi>) : PolicySetupEvent
    data class SetTimeRule(val list: List<TimeRuleUi>) : PolicySetupEvent

    data object ClearData : PolicySetupEvent

}