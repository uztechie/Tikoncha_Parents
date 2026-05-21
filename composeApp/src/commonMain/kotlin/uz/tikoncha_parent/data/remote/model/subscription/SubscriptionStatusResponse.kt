package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionStatusResponse(
    val success: Boolean = true,
    val code: Int? = null,
    val error: String? = null,
    val data: SubscriptionStatusDto? = null
)

@Serializable
data class SubscriptionStatusDto(
    @SerialName("plan_type")      val planType: String? = null,
    @SerialName("plan_name")      val planName: String? = null,
    @SerialName("plan_duration")  val planDuration: String? = null,
    @SerialName("amount")         val amount: Long = 0,
    @SerialName("is_expired")     val isExpired: Boolean = false,
    @SerialName("remaining_days") val remainingDays: Int = 0,
    @SerialName("created_at")     val createdAt: String? = null,
    @SerialName("expires_at")     val expiresAt: String? = null
)