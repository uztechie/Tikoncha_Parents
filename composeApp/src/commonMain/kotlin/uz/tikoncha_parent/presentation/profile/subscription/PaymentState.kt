package uz.tikoncha_parent.presentation.profile.subscription

import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class PaymentState(
    val serviceId: Int = 0,
    val merchantTransId: String = "",
    val amount: Int = 0,
    val responseState: ResponseState<Nothing> = ResponseState.Idle
)