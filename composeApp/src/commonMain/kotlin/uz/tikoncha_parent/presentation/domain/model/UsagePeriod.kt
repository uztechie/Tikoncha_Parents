package uz.tikoncha_parent.presentation.domain.model

import kotlinx.datetime.LocalDate
import uz.tikoncha_parent.presentation.statistic.DateSelectionType

data class UsagePeriod(
    val type: DateSelectionType,
    val label: String,
    val startDate: LocalDate,
    val endDate: LocalDate = startDate,
)
