package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable


@Serializable
data class SubscriptionLimitResponse(
    val success: Boolean,
    val data: SubscriptionLimitData? = null,
    val error: String? = null,
    val code: Int? = null
)


@Serializable
data class SubscriptionLimitData(
    val children: List<SubscriptionLimitDto>
)

@Serializable
data class SubscriptionLimitDto(
    val child_id: String? = null,
    val first_name: String? = null,
    val last_name: String? = null,
    val subscription: String? = null,
    val app_usage: SubscriptionAppUsageDto,
    val policy: SubscriptionPolicyDto
)

@Serializable
data class SubscriptionAppUsageDto(
    val app_usage_daily: Int? = null,
    val app_usage_weekly: Int? = null,
)

@Serializable
data class SubscriptionPolicyDto(
    val policy_count: Int? = null,
    val time_rule: Int? = null,
    val limit_rule: Int? = null,
    val location_rule: Int? = null,
    val wifi_rule: Int? = null,
    val app_launch_count_rule: Int? = null,
    val app_count: Int? = null,
)
