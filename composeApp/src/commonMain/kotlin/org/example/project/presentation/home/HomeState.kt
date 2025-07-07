package org.example.project.presentation.home

import org.example.project.presentation.domain.model.UsagePeriod

data class HomeState(
    val counter:Int = 0,

    val socialAppUsageList: List<AppUsage> = emptyList(),
    val gameUsageList: List<AppUsage> = emptyList(),
    val otherAppUsageList: List<AppUsage> = emptyList(),
    val dailyPeriods: List<UsagePeriod> = emptyList(),
    val weeklyPeriods: List<UsagePeriod> = emptyList(),

    val dailyChartData: Map<Int, Double> = emptyMap(),
    val weeklyChartData: Map<Int, Double> = emptyMap(),

    val childrenList: List<String> = emptyList(),
    val currentChildren: String = ""
)
