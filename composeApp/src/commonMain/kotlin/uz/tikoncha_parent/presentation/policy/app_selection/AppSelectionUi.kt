package uz.tikoncha_parent.presentation.policy.app_selection


data class AppSelectionUi(
    val name: String,
    val packageName: String,
    val checked: Boolean,
    val order:Int,
    val iconUrl: String?
)
