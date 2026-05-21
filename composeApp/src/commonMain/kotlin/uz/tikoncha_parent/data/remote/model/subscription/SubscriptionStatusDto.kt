package uz.tikoncha_parent.data.remote.model.subscription

data class SubscriptionStatusDto(
    val planType: String,            // "FREE" | "PLUS" | "PREMIUM" ...
    val amount: Long = 0L,
    val isExpired: Boolean = false,
    val remainingDays: Int = 0,
    val planDuration: String? = null, // "MONTHLY" | "YEARLY" | null
    val createdAt: String? = null,    // ISO 8601
    val planName: String? = null,
    val status: String,               // "ACTIVE" | ...
    val expiresAt: String? = null,    // ISO 8601
    val isFreeTier: Boolean = true,
    val limits: LimitsDto = LimitsDto(),
)

data class LimitsDto(
    val canAccessFullStats: Boolean = false,
    val canShareMedia: Boolean = false,
    val canUseVoice: Boolean = false,
)
