package org.example.project.platform

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toLocalDateTime
import java.time.format.TextStyle
import java.util.Locale

actual fun getCurrentDate(): LocalDate {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
}

actual fun getMonthName(date: LocalDate): String {
    val javaDate = date.toJavaLocalDate()
    return javaDate.month.getDisplayName(TextStyle.FULL, Locale("uz"))
        .replaceFirstChar { it.uppercase() }
}