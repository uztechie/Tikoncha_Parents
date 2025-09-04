package uz.tikoncha_parent.presentation.home

import uz.tikoncha_parent.data.remote.model.GetRuleItem
import uz.tikoncha_parent.domain.model.AppUsage
import uz.tikoncha_parent.domain.model.HourMinute
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.domain.model.UsagePeriod

data class HomeState(
    val rulesAppList: List<GetRuleItem> = emptyList(),
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
    val rulesError: String = "",

    val createRuleError: String = "",
    val createRuleLoading: Boolean = false,
    val createRuleSuccess: Boolean = false,

    val isTodaySelected: Boolean = false,
    val averageUsageTime: HourMinute = HourMinute(),

    val dailyChartData: Map<Int, Double> = emptyMap(),
    val weeklyChartData: Map<Int, Double> = emptyMap(),
    val dateSelectionType: DateSelectionType = DateSelectionType.WEEK,

    val selectedApp: AppUsageUi? = null


)
