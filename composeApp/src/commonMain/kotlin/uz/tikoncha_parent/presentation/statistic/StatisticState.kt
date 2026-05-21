package uz.tikoncha_parent.presentation.statistic

import kotlinx.datetime.LocalDate
import uz.tikoncha_parent.data.remote.model.permission_status.PermissionStatusIssus
import uz.tikoncha_parent.domain.model.SubscriptionLimit
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.domain.model.app_usage.AppUsage
import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class StatisticState(
    /* ----- Async holatlar ----- */
    val childrenResponseState: ResponseState<Nothing> = ResponseState.Idle,
    val appUsageResponseState: ResponseState<Nothing> = ResponseState.Idle,

    /* ----- Child ----- */
    val childrenList: List<UserInfo> = emptyList(),
    val selectedChild: UserInfo? = null,

    /* ----- Permission / Subscription ----- */
    val permissionIssueList: List<PermissionStatusIssus> = emptyList(),
    val subscriptionLimit: SubscriptionLimit? = null,
    val showBlur: Boolean = false,

    /* ----- Raw data ----- */
    val appUsageList: List<AppUsage> = emptyList(),
    val today: LocalDate? = null,

    /* ----- Tab / Pager ----- */
    val dateSelectionType: DateSelectionType = DateSelectionType.DAY,   // default DAILY
    val pages: List<PagePeriod> = emptyList(),
    val selectedPageIndex: Int = 0,

    /* ----- Tanlangan page uchun derived ----- */
    val bars: List<ChartBarUi> = emptyList(),
    val topApps: List<TopAppUi> = emptyList(),

    /* ----- Bar click dialog ----- */
    val usageDetails: UsageDetailsUi? = null,
    val showUsageDetailsDialog: Boolean = false,
) {
    val selectedPage: PagePeriod? get() = pages.getOrNull(selectedPageIndex)
}