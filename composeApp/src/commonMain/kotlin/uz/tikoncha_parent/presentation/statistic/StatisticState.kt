package uz.tikoncha_parent.presentation.statistic

import uz.tikoncha_parent.data.remote.model.GetRuleItem
import uz.tikoncha_parent.domain.model.AppUsage
import uz.tikoncha_parent.domain.model.HourMinute
import uz.tikoncha_parent.domain.model.SubscriptionLimit
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.domain.model.UsagePeriod
import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class StatisticState(
    val rulesAppList: List<GetRuleItem> = emptyList(),
    val appUsageUiList: List<AppUsageUi> = emptyList(),
    val dailyPeriods: List<UsagePeriod> = emptyList(),
    val appUsageList: List<AppUsage> = emptyList(),
    val weeklyPeriods: List<UsagePeriod> = emptyList(),



    val selectedChild: UserInfo? = null,

    val appUsageResponseState: ResponseState<Nothing> = ResponseState.Idle,

    val isTodaySelected: Boolean = false,
    val averageUsageTime: HourMinute = HourMinute(),

    val dailyChartData: Map<Int, Double> = emptyMap(),
    val weeklyChartData: Map<Int, Double> = emptyMap(),
    val dateSelectionType: DateSelectionType = DateSelectionType.WEEK,

    val selectedApp: AppUsageUi? = null,
    val subscriptionLimit: SubscriptionLimit? = null,

    val childrenList: List<UserInfo> = emptyList(),
    val childrenResponseState: ResponseState<Nothing> = ResponseState.Idle,
)
