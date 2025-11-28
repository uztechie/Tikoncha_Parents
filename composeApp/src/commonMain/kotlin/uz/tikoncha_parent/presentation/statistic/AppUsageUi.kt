package uz.tikoncha_parent.presentation.statistic

import uz.tikoncha_parent.domain.model.HourMinute

data class AppUsageUi(
    val packageName: String,
    val name: String,
    val icon: String,
    val usageTime: HourMinute,
    val allowed: Boolean = false
)

