package uz.tikoncha_parent.presentation.policy

import uz.tikoncha_parent.domain.model.SubscriptionLimit
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.ui_state.ResponseState


data class PolicyState(
    val childrenList: List<UserInfo> = emptyList(),
    val selectedChild: UserInfo? = null,
    val policyResponseState: ResponseState<Nothing> = ResponseState.Idle,
    val policies: List<PolicyItemUi> = emptyList(),
    val subscriptionLimit: SubscriptionLimit = SubscriptionLimit()
)
