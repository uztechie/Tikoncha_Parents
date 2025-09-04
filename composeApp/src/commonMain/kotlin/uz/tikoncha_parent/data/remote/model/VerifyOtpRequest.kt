package uz.saidburxon.newedu.data.model

import kotlinx.serialization.Serializable

@Serializable
data class VerifyOtpRequest(
    val phone: String,
    val otp_code: String,
)
