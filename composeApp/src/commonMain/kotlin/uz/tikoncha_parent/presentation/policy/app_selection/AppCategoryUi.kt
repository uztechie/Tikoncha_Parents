package uz.tikoncha_parent.presentation.policy.app_selection

import androidx.compose.ui.graphics.Color
import uz.tikoncha_parent.ui.CardColors

data class AppsUi (
    val id: String,
    val title: String,
    val iconUrl: String?,
    val checked: Boolean,
    val iconBg: Color = CardColors
)
data class AppCategoryUi(
    val id: String,
    val title: String,
    val expanded: Boolean,
    val checked: Boolean,
    val apps: List<AppsUi>
)
