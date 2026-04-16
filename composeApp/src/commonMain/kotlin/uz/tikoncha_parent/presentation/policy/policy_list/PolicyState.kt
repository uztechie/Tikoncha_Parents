package uz.tikoncha_parent.presentation.policy.policy_list

import uz.tikoncha_parent.domain.model.PolicyType
import uz.tikoncha_parent.domain.model.SubscriptionLimit
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.ui_state.ResponseState


data class PolicyState(
    val policyResponseState: ResponseState<Nothing> = ResponseState.Idle,
    val policies: List<PolicyItemUi> = emptyList(),
    val selectedChild: UserInfo? = null,
    val subscriptionLimit: SubscriptionLimit = SubscriptionLimit()
){
    val canCreatePolicy: Boolean
        get() = subscriptionLimit.policyCount.let {
            policies.count { it.policyType == PolicyType.PARENT_CHILD } < it
        } ?: true
}
