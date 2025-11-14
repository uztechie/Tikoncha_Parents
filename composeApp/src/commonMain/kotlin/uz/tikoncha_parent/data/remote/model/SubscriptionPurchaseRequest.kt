package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionPurchaseRequest(
    val tier: String,
    val child_user_id: String?
)
