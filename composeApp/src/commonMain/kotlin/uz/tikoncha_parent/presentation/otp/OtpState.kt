package uz.tikoncha_parent.presentation.otp

import uz.tikoncha_parent.domain.model.Session
import uz.tikoncha_parent.domain.model.app_error.ErrorCause
import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class OtpState(
    val phoneNumber: String = "",
    val otpCode: String = "",
    val isRunning: Boolean = false,
    val hasInputError: Boolean = false,
    val timeLife: Int = 0,
    val isSendingOtp: Boolean = false,
    val responseState: ResponseState<Session> = ResponseState.Idle,
) {
    val deleteAccountUrl: String? get() = ((responseState as? ResponseState.Error)?.failure?.cause as? ErrorCause.AccountDeletionRequired)?.url
}
