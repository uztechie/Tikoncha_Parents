package uz.tikoncha_parent.presentation.policy.app_site_selection


data class AppSelectionUi(
    val name: String,
    val packageName: String,
    val category: String? = null,
    val order: Int = 0,
    val iconUrl: String? = null,
    val usageMinutes: Long = 0,
)
