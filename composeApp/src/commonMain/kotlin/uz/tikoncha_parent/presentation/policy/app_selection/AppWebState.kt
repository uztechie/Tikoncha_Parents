package uz.tikoncha_parent.presentation.policy.app_selection

import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class AppWebState(
    val childId: String = "",
    val appsResponseState: ResponseState<Nothing> = ResponseState.Idle,
    val appWebSelectionIndex: Int = 0,
    val isLoading: Boolean = false,
    val apps: List<AppSelectionUi> = emptyList(),
    val selectedPkgs: Set<String> = emptySet(),
    val error: String? = null,
    val showLimitReachedDialog: Boolean = false,

    val serverPkgs: Set<String> = emptySet(),
    val serverRequestedCount: Int = 0,                  // server ro‘yxati uzunligi
    val serverPresentInstalledCount: Int = 0,           // serverdan kelib, userda oʻrnatilgani nechta
    val serverMissingCount: Int = 0,
//    val subscriptionLimitEntity: SubscriptionLimitEntity? = null
)
