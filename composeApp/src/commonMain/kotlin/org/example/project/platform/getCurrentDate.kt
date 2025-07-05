package org.example.project.platform

import kotlinx.datetime.LocalDate

expect fun getCurrentDate(): LocalDate
expect fun getMonthName(date: LocalDate): String
