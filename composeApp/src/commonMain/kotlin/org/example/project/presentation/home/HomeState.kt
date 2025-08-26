package org.example.project.presentation.home

import org.example.project.domain.model.AppUsage
import org.example.project.domain.model.HourMinute
import org.example.project.domain.model.UserInfo
import org.example.project.presentation.domain.model.UsagePeriod

data class HomeState(
    val appUsageUiList: List<AppUsageUi> = emptyList(),
    val dailyPeriods: List<UsagePeriod> = emptyList(),
    val appUsageList: List<AppUsage> = emptyList(),
    val weeklyPeriods: List<UsagePeriod> = emptyList(),



    val childrenList: List<UserInfo> = emptyList(),
    val selectedChildren: UserInfo? = null,


    val childrenLoading: Boolean = false,
    val childrenError: String = "",

    val appUsageLoading: Boolean = false,
    val appUsageError: String = "",

    val isTodaySelected: Boolean = false,
    val averageUsageTime: HourMinute = HourMinute(),

    val dailyChartData: Map<Int, Double> = emptyMap(),
    val weeklyChartData: Map<Int, Double> = emptyMap(),
    val dateSelectionType: DateSelectionType = DateSelectionType.WEEK


)
