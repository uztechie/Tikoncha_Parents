package uz.tikoncha_parent.presentation.policy.app_selection


data class AppWebState(
    val appWebSelectionIndex: Int = 0,
    val isLoading: Boolean = false,
    val categories: List<AppCategoryUi> = emptyList(),
    val error: String? = null
)
