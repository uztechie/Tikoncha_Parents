package org.example.project.presentation.domain.model

import kotlinx.datetime.LocalDate
import org.example.project.presentation.home.DateSelectionType

data class UsagePeriod(
    val type: DateSelectionType,
    val label: String,
    val startDate: LocalDate,
    val endDate: LocalDate = startDate,
)
