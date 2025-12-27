package uz.tikoncha_parent.presentation.policy.shared

import uz.tikoncha_parent.domain.model.LocationRule
import uz.tikoncha_parent.domain.model.Policy
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.policy.PolicyItemUi
import uz.tikoncha_parent.presentation.policy.limit_rule.LimitRuleUi
import uz.tikoncha_parent.presentation.policy.policy_setup.PolicyDraftSnapshot
import uz.tikoncha_parent.presentation.policy.rule_type_selection.RuleType
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleUi

sealed interface PolicySharedEvent {

    data class SetLimitRule(val list: List<LimitRuleUi>) : PolicySharedEvent
    data class SetTimeRule(val list: List<TimeRuleUi>) : PolicySharedEvent
    data class SetLocationRule(val locationRule: LocationRule?) : PolicySharedEvent
    data class SetSelectedChild(val child: UserInfo) : PolicySharedEvent

    data class SetPolicy(val policyItemUi: PolicyItemUi): PolicySharedEvent
    data object ClearData : PolicySharedEvent
    data class SetPolicyTitle(val title: String): PolicySharedEvent
    data class SetPolicyDraftSnapshot(val snapshot: PolicyDraftSnapshot): PolicySharedEvent
    data object StopPolicyDraftSnapshotUpdate: PolicySharedEvent

    data object RefreshSubscriptionLimit: PolicySharedEvent
    data object LoadSubscriptionLimit: PolicySharedEvent



}