package uz.tikoncha_parent.domain.model.app_usage

import kotlinx.datetime.LocalDate

data class AppUsage(
    val packageName: String,
    val name: String,
    val iconUrl: String?,
    val usage: Map<LocalDate, Map<Int, Long>>
)