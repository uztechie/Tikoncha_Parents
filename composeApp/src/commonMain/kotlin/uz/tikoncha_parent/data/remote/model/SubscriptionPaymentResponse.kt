package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionPaymentResponse(
    val success: Boolean,
    val data: SubscriptionPurchaseData? = null,
    val error: String? = null,
    val code: Int? = null,
)

@Serializable
data class SubscriptionPurchaseData(
    val merchant_trans_id: String,
    val amount: Int,
    val service_id: Int,
    val tier: String,
    val coins_included: Int
)