package uz.tikoncha_parent.data.mapper

import uz.tikoncha_parent.data.remote.model.SubscriptionLimitData
import uz.tikoncha_parent.data.remote.model.SubscriptionPlansData
import uz.tikoncha_parent.domain.model.SubscriptionLimit
import uz.tikoncha_parent.domain.model.SubscriptionType
import uz.tikoncha_parent.presentation.profile.subscription.PlanUi
import uz.tikoncha_parent.presentation.profile.subscription.SubscriptionUi

fun SubscriptionPlansData.toSubscriptionPlanUi(): SubscriptionUi {
    return SubscriptionUi(
        planId = id,
        type = SubscriptionType.fromString(name),
        monthly = PlanUi(
            price = monthly.price,
            coin = monthly.coin,
            feature = monthly.feature,
            bonus = monthly.bonus
        ),
        annual = PlanUi(
            price = annual.price,
            coin = annual.coin,
            feature = annual.feature,
            bonus = annual.bonus
        )
    )
}

fun SubscriptionLimitData.toSubscriptionLimit(): SubscriptionLimit{
    return SubscriptionLimit(
        appUsageDaily = app_usage.app_usage_daily ?: Int.MAX_VALUE,
        appUsageWeekly = app_usage.app_usage_weekly ?: Int.MAX_VALUE,
        policyCount = policy.policy_count ?: Int.MAX_VALUE,
        timeRule = policy.time_rule ?: Int.MAX_VALUE,
        limitRule = policy.limit_rule ?: Int.MAX_VALUE,
        locationRule = policy.location_rule ?: Int.MAX_VALUE,
        wifiRule = policy.wifi_rule ?: Int.MAX_VALUE,
        appLaunchCountRule = policy.app_launch_count_rule ?: Int.MAX_VALUE,
        appCount = policy.app_count ?: Int.MAX_VALUE
    )
}