package org.example.project.presentation.home

data class AppUsageUi(
    val packageName: String,
    val name: String,
    val icon: String,
    val usageTime: String,
    val allowed: Boolean = true
)

