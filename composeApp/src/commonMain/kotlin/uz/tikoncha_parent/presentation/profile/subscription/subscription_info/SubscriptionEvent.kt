package uz.tikoncha_parent.presentation.profile.subscription.subscription_info

sealed interface SubscriptionEvent {
    data object Load : SubscriptionEvent
    data object Retry : SubscriptionEvent
    data object ResetResponseState : SubscriptionEvent
    data object OnCardClick : SubscriptionEvent
    data object OnErrorDismissed : SubscriptionEvent
    data object OnBackClick : SubscriptionEvent
}