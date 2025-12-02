package uz.tikoncha_parent.presentation.statistic

import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.domain.model.UsagePeriod
import uz.tikoncha_parent.presentation.new_home.HomeEvent
import uz.tikoncha_parent.presentation.profile.coins.CoinsEvent

sealed interface StatisticEvent {
    data class GetUsageList(val usagePeriod: UsagePeriod, val dateSelectionType: DateSelectionType): StatisticEvent
    data object GetAppUsage: StatisticEvent
    data object RefreshChild: StatisticEvent

    data object RefreshSubscriptionLimit: StatisticEvent

    data class TodaySelected(val today: Boolean): StatisticEvent

    data class OnChildSelected(val child: UserInfo): StatisticEvent

    data object GetChildren: StatisticEvent
}