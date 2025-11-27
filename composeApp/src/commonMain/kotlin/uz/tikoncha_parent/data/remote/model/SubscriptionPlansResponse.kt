package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionPlansResponse(
    val success: Boolean,
    val data: List<SubscriptionPlansData>? = null,
    val error: String? = null,
    val code: Int? = null
)

@Serializable
data class SubscriptionPlansData(
    val id: String,
    val name: String,
    val monthly: SubscriptionPlansDto,
    val annual: SubscriptionPlansDto,
)

@Serializable
data class SubscriptionPlansDto(
    val price: Int,
    val coin: Int,
    val feature: List<String>,
    val bonus: List<String>,
)
