package uz.tikoncha_parent.presentation.profile.subscription.subscription_info

sealed interface SubscriptionEffect {
    data object NavigateToPayment : SubscriptionEffect
    data object NavigateToInfoMode : SubscriptionEffect
    data object PopBack : SubscriptionEffect
}