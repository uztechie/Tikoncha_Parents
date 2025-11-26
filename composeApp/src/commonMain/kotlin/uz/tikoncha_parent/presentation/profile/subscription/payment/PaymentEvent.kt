package uz.tikoncha_parent.presentation.profile.subscription.payment

sealed interface PaymentEvent {
    data object Purchase: PaymentEvent
    data object ResetResponseState: PaymentEvent


}