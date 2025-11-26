package uz.tikoncha_parent.domain.model

enum class SubscriptionType {
    FREE,
    PLUS,
    PRO;

    companion object Companion {
        fun fromString(value: String?): SubscriptionType {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: FREE
        }
    }
}

enum class SubscriptionDuration{
    MONTHLY,
    ANNUAL;
}