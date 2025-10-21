package uz.tikoncha_parent.presentation.home.schedule

import uz.tikoncha_parent.data.mapper.AppCategoryUi

data class ScheduleState(
    val genderIndex: Int = 0,
    val isLoading: Boolean = false,
    val categories: List<AppCategoryUi> = emptyList(),
    val error: String? = null
)
