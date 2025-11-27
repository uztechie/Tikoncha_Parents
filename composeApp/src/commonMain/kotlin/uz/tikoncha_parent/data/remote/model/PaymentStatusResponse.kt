package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class PaymentStatusResponse(
    val success: Boolean,
    val data: PaymentStatusData? = null,
    val error: String? = null,
    val code: Int? = null,
)

@Serializable
data class PaymentStatusData(
    val status: String
)