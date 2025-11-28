package uz.tikoncha_parent.presentation.profile.subscription.subscription_payment

import uz.tikoncha_parent.domain.model.UserInfo

sealed class SubscriptionPaymentEvent {

    data object ResetResponseState: SubscriptionPaymentEvent()
    data class SetSelectedChild(val child: UserInfo): SubscriptionPaymentEvent()
}