package uz.tikoncha_parent.presentation.policy.policy_setup

import uz.tikoncha_parent.domain.model.LocationRule
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.policy.PolicyItemUi
import uz.tikoncha_parent.presentation.policy.limit_rule.LimitRuleUi
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleUi

sealed interface PolicySetupEvent {

    data class SetLimitRule(val list: List<LimitRuleUi>) :PolicySetupEvent
    data class SetTimeRule(val list: List<TimeRuleUi>) : PolicySetupEvent
    data class SetLocationRule(val locationRule: LocationRule?) : PolicySetupEvent




    data object ClearData : PolicySetupEvent

    data object SavePolicy : PolicySetupEvent
    data object DeletePolicy : PolicySetupEvent
    data object ResetResponseState : PolicySetupEvent

    data class UpdatePackagesLint(val value: String): PolicySetupEvent
    data class SetSelectedApps(val list: List<String>): PolicySetupEvent
    data class SetPolicy(val policyItemUi: PolicyItemUi): PolicySetupEvent
    data class SetTitle(val title: String): PolicySetupEvent
    data class SetSelectedChild(val child: UserInfo?): PolicySetupEvent



}