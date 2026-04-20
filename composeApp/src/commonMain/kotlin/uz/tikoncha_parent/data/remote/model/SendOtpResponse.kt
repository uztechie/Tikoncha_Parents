package uz.saidburxon.newedu.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SendOtpResponse(
    val success: Boolean,
    val data: SendOtpResponseData? = null,
    val error: String? = null,
    val url: String? = null,
    val code: Int,
)


@Serializable
data class SendOtpResponseData(
    val message: String? = null,
)

