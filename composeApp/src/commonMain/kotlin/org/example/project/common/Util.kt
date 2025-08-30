package org.example.project.common


import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
object Util {

    val currentMillis: Long = Clock.System.now().toEpochMilliseconds()

    fun formatLocalDate(millis: Long): String {
        val zone = TimeZone.currentSystemDefault()
        val inputDate: LocalDate = Instant.fromEpochMilliseconds(millis)
            .toLocalDateTime(zone).date

        val today = Clock.System.now().toLocalDateTime(zone).date
        val yesterday = today.minus(1, DateTimeUnit.DAY)

        return when (inputDate) {
            today -> "Bugun"
            yesterday -> "Kecha"
            else -> formatDateDdMmYyyy(inputDate)
        }
    }

    private fun formatDateDdMmYyyy(date: LocalDate): String {
        return buildString {
            append(date.day.toString().padStart(2, '0'))
            append(".")
            append(date.month.number.toString().padStart(2, '0'))
            append(".")
            append(date.year)
        }
    }

    fun getCurrentDate(): LocalDate {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

    fun getCurrentTime(): LocalTime {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time
    }

    fun getMonthName(date: LocalDate): String {
        val uzbekMonths = listOf(
            "Yanvar", "Fevral", "Mart", "Aprel", "May", "Iyun",
            "Iyul", "Avgust", "Sentyabr", "Oktyabr", "Noyabr", "Dekabr"
        )
        return uzbekMonths[date.month.number - 1]
    }


    fun format6DigitCode(raw: String): String {
        val digits = raw.filter { it.isDigit() }.take(6)
        return if (digits.length <= 3) digits else "${digits.take(3)}-${digits.drop(3)}"
    }


}