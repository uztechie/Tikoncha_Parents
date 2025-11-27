package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionPaymentRequest(
    val tier: String,
    val child_user_id: String?
)
