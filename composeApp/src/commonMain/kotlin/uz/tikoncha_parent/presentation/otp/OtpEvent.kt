package uz.tikoncha_parent.presentation.otp

sealed class OtpEvent {
    data class SetPhone(val phoneNumber: String): OtpEvent()
    data class OnOtpUpdate(val otpCode: String): OtpEvent()
    data class SetTelegram(val isTelegram: Boolean): OtpEvent()
    object OnConfirmClicked: OtpEvent()
    object TimeStart: OtpEvent()
    object Reset : OtpEvent()
    object SendOtp : OtpEvent()
}