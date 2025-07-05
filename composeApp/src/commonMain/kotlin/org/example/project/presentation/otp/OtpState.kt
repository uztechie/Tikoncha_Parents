package org.example.project.presentation.otp

data class OtpState(
    val phoneNumber: String = "",
    val otpCode: String = "",
    val accept: Boolean = false,
    val isRunning: Boolean = false,
    var timeLife: Int = 60,
    var isUserExists: Boolean? = null
)
