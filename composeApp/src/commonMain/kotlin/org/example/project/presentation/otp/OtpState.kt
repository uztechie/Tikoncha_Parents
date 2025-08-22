package org.example.project.presentation.otp

import uz.saidburxon.newedu.data.model.VerifyOtpResponseData

data class OtpState(
    val phoneNumber: String = "",
    val otpCode: String = "",
    val isRunning: Boolean = false,
    var timeLife: Int = 60,
    var isUserExists: Boolean? = null,
    var loading: Boolean = false,
    var success: Boolean = false,
    var errorMessage: String? = null,
    var data: VerifyOtpResponseData? = null
)
