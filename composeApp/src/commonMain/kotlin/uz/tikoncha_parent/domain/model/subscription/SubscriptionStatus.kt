package uz.tikoncha_parent.domain.model.subscription

data class SubscriptionStatus(
    val planType: PlanType,
    val planName: String?,
    val planDuration: PlanDuration?,
    val amount: Long,
    val isExpired: Boolean,
    val remainingDays: Int,
    val createdAt: String?,   // dd.MM.yyyy
    val expiresAt: String?    // dd.MM.yyyy
)

enum class PlanType {
    FREE, PLUS, UNKNOWN;

    companion object {
        fun fromString(value: String?): PlanType = when (value?.uppercase()) {
            "FREE" -> FREE
            "PLUS" -> PLUS
            else -> UNKNOWN
        }
    }
}

enum class PlanDuration {
    MONTHLY, ANNUAL;

    companion object {
        fun fromString(value: String?): PlanDuration? = when (value?.uppercase()) {
            "MONTHLY" -> MONTHLY
            "ANNUAL" -> ANNUAL
            else -> null
        }
    }
}