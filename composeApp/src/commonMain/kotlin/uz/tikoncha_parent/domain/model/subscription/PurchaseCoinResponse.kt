package uz.tikoncha_parent.domain.model.subscription

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseCoinResponse(
    val success: Boolean,
    val data: PurchaseCoinData? = null,
    val error: String? = null,
    val code: Int,
)

@Serializable
data class PurchaseCoinData(
    val merchant_trans_id: String,
    val amount: Int,
    val coins: Int,
    val service_id: Int
)
