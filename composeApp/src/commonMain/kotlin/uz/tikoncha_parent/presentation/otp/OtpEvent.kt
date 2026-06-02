package uz.tikoncha_parent.presentation.otp

sealed class OtpEvent {
    data class SetPhone(val phoneNumber: String) : OtpEvent()
    data class OnOtpUpdate(val otpCode: String) : OtpEvent()
    data object OnConfirmClicked : OtpEvent()
    data object SendOtp : OtpEvent()       // qayta yuborish
    data object Reset : OtpEvent()
    data object ResetError : OtpEvent()
}