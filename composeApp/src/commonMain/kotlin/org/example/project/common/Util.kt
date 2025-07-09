package org.example.project.common

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

object Util {

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
            append(date.dayOfMonth.toString().padStart(2, '0'))
            append(".")
            append(date.monthNumber.toString().padStart(2, '0'))
            append(".")
            append(date.year)
        }
    }
}