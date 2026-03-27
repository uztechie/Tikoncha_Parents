package uz.tikoncha_parent.presentation.otp

import uz.saidburxon.newedu.data.model.VerifyOtpResponseData
import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class OtpState(
    val phoneNumber: String = "",
    val otpCode: String = "",
    val isRunning: Boolean = false,
    val isTelegram: Boolean? = null,
    val hasInputError: Boolean = false,
    var timeLife: Int = 0,
    var isUserExists: Boolean? = null,
    val responseState: ResponseState<VerifyOtpResponseData> = ResponseState.Idle
)
