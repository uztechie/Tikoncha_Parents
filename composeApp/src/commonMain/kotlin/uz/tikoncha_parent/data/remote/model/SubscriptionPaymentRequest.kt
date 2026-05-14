package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionPaymentRequest(
    val plan_id: String,
    val plan_duration: String,
    val child_user_id: String?,
    val child_phone: String?,
    val promocode_code: String?
)
