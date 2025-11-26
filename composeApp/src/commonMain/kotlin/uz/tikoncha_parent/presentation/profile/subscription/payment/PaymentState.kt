package uz.tikoncha_parent.presentation.profile.subscription.payment

import uz.tikoncha_parent.domain.model.PaymentStatus
import uz.tikoncha_parent.domain.model.SubscriptionDuration
import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class PaymentState(
    val serviceId: Int = 0,
    val merchantTransId: String = "",
    val amount: Int = 0,
    val responseState: ResponseState<Nothing> = ResponseState.Idle,

    val subscriptionDuration: SubscriptionDuration = SubscriptionDuration.MONTHLY,
    val paymentStatus: PaymentStatus = PaymentStatus.START,
)