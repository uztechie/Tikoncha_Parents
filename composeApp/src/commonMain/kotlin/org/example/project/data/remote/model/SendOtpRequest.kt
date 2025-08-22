package uz.saidburxon.newedu.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SendOtpRequest(
    val phone: String
)
