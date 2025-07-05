package org.example.project.presentation.task

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.LocalDate

fun reformattedToday(): String {
    val today: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    val currentDay = today.dayOfMonth
    val currentMonth = today.monthNumber // 1–12

    val monthUz = uzbekMonthName(currentMonth)

    return "$currentDay - $monthUz"
}

fun uzbekMonthName(monthNumber: Int): String {
    return when (monthNumber) {
        1 -> "Yanvar"
        2 -> "Fevral"
        3 -> "Mart"
        4 -> "Aprel"
        5 -> "May"
        6 -> "Iyun"
        7 -> "Iyul"
        8 -> "Avgust"
        9 -> "Sentyabr"
        10 -> "Oktyabr"
        11 -> "Noyabr"
        12 -> "Dekabr"
        else -> ""
    }
}
