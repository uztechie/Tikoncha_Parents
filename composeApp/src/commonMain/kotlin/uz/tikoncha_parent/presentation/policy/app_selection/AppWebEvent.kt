package uz.tikoncha_parent.presentation.policy.app_selection

sealed class AppWebEvent {
    data class OnAppWebSelected(val genderIndex: Int): AppWebEvent()
    data class ExpandCategory(val category: AppCategoryUi): AppWebEvent()
    data class ToggleCategory(val category: AppCategoryUi, val checked: Boolean): AppWebEvent()
    data class ToggleApp(val app: AppsUi, val checked: Boolean): AppWebEvent()

}