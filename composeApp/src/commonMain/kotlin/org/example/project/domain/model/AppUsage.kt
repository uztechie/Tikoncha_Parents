package org.example.project.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class AppUsage(
    val packageName: String,
    val appName: String,
    val date: LocalDate,
    val usageMillis: Long,
    val usageTime: LocalTime,
    val allowed: Boolean = false,
)
