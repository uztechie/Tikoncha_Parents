package uz.tikoncha_parent.presentation.policy.app_site_selection

data class CategoryGroupUi(
    val id: String,
    val displayName: String,
    val emoji: String,
    val apps: List<AppSelectionUi>,
)
