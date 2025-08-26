package org.example.project.presentation.home

import org.example.project.domain.model.HourMinute

data class AppUsageUi(
    val packageName: String,
    val name: String,
    val icon: String,
    val usageTime: HourMinute
)

