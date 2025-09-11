package uz.tikoncha_parent.presentation.otp

import uz.saidburxon.newedu.data.model.VerifyOtpResponse
import uz.saidburxon.newedu.data.model.VerifyOtpResponseData
import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class OtpState(
    val phoneNumber: String = "",
    val otpCode: String = "",
    val isRunning: Boolean = false,
    var timeLife: Int = 60,
    var isUserExists: Boolean? = null,
    val responseState: ResponseState<VerifyOtpResponseData> = ResponseState.Idle
)
