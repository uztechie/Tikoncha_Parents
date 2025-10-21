package uz.tikoncha_parent.data.mapper

import androidx.compose.ui.graphics.Color
import uz.tikoncha_parent.data.remote.model.AppsCategoryDto
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
    val apps: List<AppsUi>
)

fun AppsCategoryDto.toUi(expanded: Boolean = false): AppCategoryUi =
    AppCategoryUi(
        id = id,
        title = title,
        expanded = expanded,
        apps = apps.map {
            AppsUi(
                id = it.id,
                title = it.name,
                iconUrl = it.iconUrl,
                checked = it.checked,
            )
        }
    )
