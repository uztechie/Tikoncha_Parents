package uz.tikoncha_parent.presentation.policy.app_selection

import uz.tikoncha_parent.domain.model.SubscriptionLimit


sealed class AppWebEvent {

    data class OnAppWebSelected(val index: Int): AppWebEvent()
    data class SetChildId(val id: String): AppWebEvent()
    data class SetSubscriptionLimit(val limit: SubscriptionLimit): AppWebEvent()
    data class ToggleApp(val app: AppSelectionUi, val checked: Boolean): AppWebEvent()
    data class SetSelectedApps(val apps: List<AppSelectionUi>): AppWebEvent()

    data class SetServerPackages(val packages: List<String>) : AppWebEvent()

    data object GetAppsFromServer: AppWebEvent()
    data object ClearData: AppWebEvent()
    data object ClearAppList: AppWebEvent()

//    data class SetSubscriptionLimit(val subscriptionLimitEntity: SubscriptionLimitEntity?): AppWebEvent()
    data object DismissLimitDialog: AppWebEvent()


}