package uz.tikoncha_parent.data.remote.model.subscription

import kotlinx.serialization.Serializable

@Serializable
data class PromoCodeValidationResponse(
    val success: Boolean,
    val data: PromoCodeValidationData? = null,
    val error: String? = null,
    val code: Int,
)

@Serializable
data class PromoCodeValidationData(
    val code: String,
    val discount_percentage: Int,
    val original_amount: Int,
    val discounted_amount: Int,
    val savings: Int,
)
