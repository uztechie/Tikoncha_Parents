package org.example.project.presentation.domain.model

import org.example.project.presentation.home.DateSelectionType

data class UsagePeriod(
    val type: DateSelectionType,
    val label: String,
    val startDate: Long,
    val endDate: Long = startDate,
)
