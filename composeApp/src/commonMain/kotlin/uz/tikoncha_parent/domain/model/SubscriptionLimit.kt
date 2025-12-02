package uz.tikoncha_parent.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionLimit(
    val childId: String = "",
    val name: String = "",
    val lastname: String = "",
    val subscriptionType: SubscriptionType = SubscriptionType.FREE,
    val appUsageDaily: Int = Int.MAX_VALUE,
    val appUsageWeekly: Int = Int.MAX_VALUE,
    val policyCount: Int = Int.MAX_VALUE,
    val timeRule: Int = Int.MAX_VALUE,
    val limitRule: Int = Int.MAX_VALUE,
    val locationRule: Int = Int.MAX_VALUE,
    val wifiRule: Int = Int.MAX_VALUE,
    val appLaunchCountRule: Int = Int.MAX_VALUE,
    val appCount: Int = Int.MAX_VALUE
)
