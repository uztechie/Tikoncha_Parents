package uz.tikoncha_parent.presentation.policy.common

import uz.tikoncha_parent.domain.model.WeekDay

data class WeekDayChipUi(
    val day: WeekDay,
    val labelResId: Int,
    val selected: Boolean,
    val enabled: Boolean
)
