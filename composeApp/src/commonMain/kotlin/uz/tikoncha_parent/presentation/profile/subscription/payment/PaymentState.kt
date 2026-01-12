package uz.tikoncha_parent.presentation.profile.subscription.payment

import uz.tikoncha_parent.domain.model.PaymentStatus
import uz.tikoncha_parent.domain.model.SubscriptionDuration
import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class PaymentState(
    val amount: Int = 0,
    val selectedPaymentType: PaymentType? = null,
    val paymentResponseState: ResponseState<Nothing> = ResponseState.Idle,
    val applePaymentResponseState: ResponseState<Nothing> = ResponseState.Idle,
    val merchantTransId: String = "",
    val serviceId: Int = 0,
    val merchantId: Int = 46788,
    val subscriptionDuration: SubscriptionDuration = SubscriptionDuration.MONTHLY,
    val paymentStatus: PaymentStatus = PaymentStatus.START,
    val paymentStatusError: String = "",
    val isTestAccount: Boolean = false,
    val planId: String = ""
)