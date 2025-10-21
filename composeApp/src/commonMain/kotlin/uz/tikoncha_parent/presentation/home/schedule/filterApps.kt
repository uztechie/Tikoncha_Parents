package uz.tikoncha_parent.presentation.home.schedule

import uz.tikoncha_parent.data.mapper.AppCategoryUi
import uz.tikoncha_parent.data.mapper.AppsUi

fun filterApps(
    categories: List<AppCategoryUi>,
    query: String
): List<AppsUi> {
    val q = query.trim().lowercase()
    return categories.flatMap { it.apps }.filter { it.title.lowercase().contains(q) }
}
