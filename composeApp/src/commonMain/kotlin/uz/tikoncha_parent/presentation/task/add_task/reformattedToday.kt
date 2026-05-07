package uz.tikoncha_parent.presentation.task.add_task

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.number

fun reformattedToday(reformatedDate: LocalDate?): String {
    val day = reformatedDate?.day
    val monthUz = uzbekMonthName(reformatedDate?.let { it.month.number } ?: 0 )
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

