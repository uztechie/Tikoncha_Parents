package uz.tikoncha_parent.presentation.statistic

import uz.tikoncha_parent.domain.model.UserInfo

sealed interface StatisticEvent {
    data object Init : StatisticEvent
    data object GetChildren : StatisticEvent
    data object RefreshChild : StatisticEvent
    data object GetAppUsage : StatisticEvent
    data object RefreshSubscriptionLimit : StatisticEvent

    data class OnChildSelected(val child: UserInfo) : StatisticEvent

    data class ChangeMode(val mode: DateSelectionType) : StatisticEvent
    data class PageChanged(val index: Int) : StatisticEvent

    data class BarClicked(val bar: ChartBarUi) : StatisticEvent
    data object DismissUsageDetailsDialog : StatisticEvent

    data object ClearAll : StatisticEvent
}