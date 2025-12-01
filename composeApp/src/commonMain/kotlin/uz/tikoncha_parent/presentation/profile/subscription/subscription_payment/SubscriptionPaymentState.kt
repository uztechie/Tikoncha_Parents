package uz.tikoncha_parent.presentation.profile.subscription.subscription_payment

import uz.tikoncha_parent.domain.model.SubscriptionType
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.profile.subscription.SubscriptionUi
import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class SubscriptionPaymentState(
    val subscriptionUi: SubscriptionUi? = null,
    val currentPlan: SubscriptionType = SubscriptionType.FREE,
    val subscriptionPlanState: ResponseState<Nothing> = ResponseState.Idle,
    val selectedChild: UserInfo? = null,
    val children: List<UserInfo> = emptyList(),
)
