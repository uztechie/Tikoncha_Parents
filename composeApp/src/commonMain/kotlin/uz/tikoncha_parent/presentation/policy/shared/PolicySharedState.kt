package uz.tikoncha_parent.presentation.policy.shared

import uz.tikoncha_parent.domain.model.LocationRule
import uz.tikoncha_parent.domain.model.Policy
import uz.tikoncha_parent.domain.model.SubscriptionLimit
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.policy.PolicyItemUi
import uz.tikoncha_parent.presentation.policy.app_selection.AppSelectionUi
import uz.tikoncha_parent.presentation.policy.limit_rule.LimitRuleUi
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleUi

data class PolicySharedState(

    val limitList: List<LimitRuleUi> = emptyList(),
    val timeList: List<TimeRuleUi> = emptyList(),
    val locationRule: LocationRule? = null,
    val selectedApps: List<AppSelectionUi> = emptyList(),
    val selectedChild: UserInfo? = null,
    val policyTitle: String = "",
    val selectedPolicy: PolicyItemUi? = null,
    val canUpdate: Boolean = true,
    val subscriptionLimit: SubscriptionLimit = SubscriptionLimit()

)
