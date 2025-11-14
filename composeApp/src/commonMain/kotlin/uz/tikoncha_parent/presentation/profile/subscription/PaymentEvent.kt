package uz.tikoncha_parent.presentation.profile.subscription

sealed interface PaymentEvent {
    data object Purchase: PaymentEvent
    data object ResetResponseState: PaymentEvent


}