package uz.tikoncha_parent.presentation.home

import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.domain.model.UsagePeriod

sealed interface HomeEvent {
    data class GetUsageList(val usagePeriod: UsagePeriod, val dateSelectionType: DateSelectionType): HomeEvent

    object OnChildSelectClicked: HomeEvent
    data class OnChildSelected(val child: UserInfo): HomeEvent

    data object GetChildren: HomeEvent
    data object GetAppUsage: HomeEvent

    data class TodaySelected(val today: Boolean): HomeEvent
    data class OnLockClicked(val appUsageUi: AppUsageUi): HomeEvent
}