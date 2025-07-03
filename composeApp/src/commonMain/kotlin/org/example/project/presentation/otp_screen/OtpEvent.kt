package org.example.project.presentation.otp_screen

sealed class OtpEvent {
    data class SetPhone(val phoneNumber: String): OtpEvent()
    data class OnOtpUpdate(val otpCode: String): OtpEvent()
    object OnConfirmClicked: OtpEvent()
    object TimeStart: OtpEvent()
    object Tick : OtpEvent()
    object ClearNavigation : OtpEvent()
}