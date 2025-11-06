package uz.tikoncha_parent.data.mapper

import uz.tikoncha_parent.data.remote.model.AppsCategoryDto
import uz.tikoncha_parent.presentation.policy.app_selection.AppCategoryUi
import uz.tikoncha_parent.presentation.policy.app_selection.AppsUi


fun AppsCategoryDto.toUi(expanded: Boolean = false): AppCategoryUi =
    AppCategoryUi(
        id = id,
        title = title,
        expanded = expanded,
        checked = true,
        apps = apps.map {
            AppsUi(
                id = it.id,
                title = it.name,
                iconUrl = it.iconUrl,
                checked = it.checked,
            )
        }
    )
