package uz.tikoncha_parent.presentation.otp

sealed class OtpEvent {
    data class SetPhone(val phoneNumber: String): OtpEvent()
    data class OnOtpUpdate(val otpCode: String): OtpEvent()
    object OnConfirmClicked: OtpEvent()
    object TimeStart: OtpEvent()
    object Tick : OtpEvent()
    object Reset : OtpEvent()
    object ClearNavigation : OtpEvent()
}