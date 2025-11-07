package uz.tikoncha_parent.presentation.policy.shared

import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.policy.limit_rule.LimitRuleUi
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleUi

sealed interface PolicySharedEvent {

    data class SetLimitRule(val list: List<LimitRuleUi>) : PolicySharedEvent
    data class SetTimeRule(val list: List<TimeRuleUi>) : PolicySharedEvent


    data class SetSelectedChild(val child: UserInfo?) : PolicySharedEvent
    data object ClearData : PolicySharedEvent
}