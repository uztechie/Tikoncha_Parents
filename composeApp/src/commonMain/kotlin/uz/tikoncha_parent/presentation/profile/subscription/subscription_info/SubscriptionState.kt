package uz.tikoncha_parent.presentation.profile.subscription.subscription_info

import uz.tikoncha_parent.domain.model.subscription.SubscriptionStatus
import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class SubscriptionState(
    val subscriptionStatusState: ResponseState<Unit> = ResponseState.Idle,
    val subscription: SubscriptionStatus? = null,
    val selectedChildId: String? = null,
)