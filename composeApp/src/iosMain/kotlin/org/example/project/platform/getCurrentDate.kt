package org.example.project.platform

import kotlinx.datetime.*

actual fun getCurrentDate(): LocalDate {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
}

actual fun getMonthName(date: LocalDate): String {
    val uzbekMonths = listOf(
        "Yanvar", "Fevral", "Mart", "Aprel", "May", "Iyun",
        "Iyul", "Avgust", "Sentyabr", "Oktyabr", "Noyabr", "Dekabr"
    )
    return uzbekMonths[date.monthNumber - 1]
}
