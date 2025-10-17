package uz.tikoncha_parent.presentation.home.schedule

import uz.tikoncha_parent.data.mapper.AppsUi

data class SearchHit(
    val app: AppsUi,
    val categoryId: String,
    val categoryTitle: String
)