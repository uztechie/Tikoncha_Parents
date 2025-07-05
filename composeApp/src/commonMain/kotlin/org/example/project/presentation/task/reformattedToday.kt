package org.example.project.presentation.task

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

fun reformattedToday(reformatedDate: LocalDate?): String {
    val day = reformatedDate?.dayOfMonth
    val monthUz = uzbekMonthName(reformatedDate?.monthNumber ?:0 )
    return "$day - $monthUz"
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

fun formatTime(localTime: LocalTime?): String {
    if (localTime == null) return ""

    val hour = localTime.hour.toString().padStart(2, '0')
    val minute = localTime.minute.toString().padStart(2, '0')

    return "$hour:$minute"
}

