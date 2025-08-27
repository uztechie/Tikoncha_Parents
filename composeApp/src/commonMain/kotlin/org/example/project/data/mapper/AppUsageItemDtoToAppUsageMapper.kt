package org.example.project.data.mapper

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.daysUntil
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.example.project.data.remote.model.AppUsageDataDto
import org.example.project.domain.model.AppUsage
import org.example.project.domain.model.HourMinute
import org.example.project.presentation.domain.model.UsagePeriod
import org.example.project.presentation.home.AppUsageUi
import org.example.project.presentation.home.DateSelectionType
import kotlin.time.ExperimentalTime

private fun parseDateDdMMyyyy(s: String): LocalDate {
    val (d, m, y) = s.split("-").map { it.toInt() }
    return LocalDate(y, m, d)
}

private fun parseTimeHHmm(s: String): LocalTime {
    val (h, m) = s.split(":").map { it.toInt() }
    return LocalTime(hour = h, minute = m)
}

fun AppUsageDataDto.toAppUsageList(): List<AppUsage>{
    val dataList = this.items
    return buildList {
        for (item in dataList){
            for ((dateStr, hourMap) in item.usage){
                val date = parseDateDdMMyyyy(dateStr)
                for ((timeStr, usageMillis) in hourMap){
                    add(
                        AppUsage(
                            packageName = item.packageName,
                            appName = item.name,
                            date = date,
                            usageTime = parseTimeHHmm(timeStr),
                            usageMillis = usageMillis
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
fun List<AppUsage>.mapToDailyUsagePeriods(): List<UsagePeriod> {

    val dates = this
        .map { it.date }
        .distinct()
        .sortedDescending()

    val formatter = LocalDate.Format {
        day() // dd
        char('.')
        monthNumber() // MM
    }


    return dates
        .distinct()
        .sorted()
        .map { date ->
            val timeZone = TimeZone.currentSystemDefault()
            val startInstant = date.atStartOfDayIn(timeZone)
            val endInstant = date.plus(1, DateTimeUnit.DAY)
                .atStartOfDayIn(timeZone)
                .minus(1, DateTimeUnit.MILLISECOND, timeZone)

            val startMillis = startInstant.toEpochMilliseconds()
            val endMillis = endInstant.toEpochMilliseconds()
            UsagePeriod(
                label = "${formatter.format(date)}.${date.year}",
                startDate = date,
                endDate = date,
                type = DateSelectionType.DAY
            )
        }
}

@OptIn(ExperimentalTime::class)
fun List<AppUsage>.mapToWeeklyUsagePeriods(): List<UsagePeriod> {

    val dates = this
        .map { it.date }
        .distinct()
        .sorted()

    val timeZone = TimeZone.currentSystemDefault()
    val formatter = LocalDate.Format {
        day() // dd
        char('.')
        monthNumber() // MM
    }

    val localDates = dates
        .distinct()
        .sorted()

    val weeks = localDates.groupBy { d -> isoWeekKey(d) }


    return weeks.entries
        .sortedWith(compareBy <Map.Entry<Pair<Int, Int>, List<LocalDate>>> { it.key.first } // year desc
            .thenBy { it.key.second }) // week desc

        .map { (_, days) ->
            val start = days.minOrNull()!!
            val end = days.maxOrNull()!!
            // Kun boshidan millis
            val startMillis = start.atStartOfDayIn(timeZone).toEpochMilliseconds()
            // end kuni oxiri: (ertasi kun 00:00) - 1 ms
            val endMillis = end
                .plus(1, DateTimeUnit.DAY)
                .atStartOfDayIn(timeZone)
                .minus(1, DateTimeUnit.MILLISECOND, timeZone)
                .toEpochMilliseconds()


            val startLabel = start.format(formatter).padStart(5, '0') // dd.MM ko‘rinishida
            val endLabel = end.format(formatter).padStart(5, '0')


            UsagePeriod(
                label = "$startLabel - $endLabel.${end.year}",
                startDate = start,
                endDate = end,
                type = DateSelectionType.WEEK
            )
        }
}

private fun isoWeekKey(date: LocalDate): Pair<Int, Int> {
    fun thursdayOfWeek(d: LocalDate): LocalDate {
        val delta = DayOfWeek.THURSDAY.isoDayNumber - d.dayOfWeek.isoDayNumber // [-6..+6]
        return d.plus(delta, DateTimeUnit.DAY)
    }
    val thu = thursdayOfWeek(date)
    val weekYear = thu.year
    val week1Thu = thursdayOfWeek(LocalDate(weekYear, 1, 4)) // ISO hafta-1 ning payshanbasi
    val weekNo = 1 + week1Thu.daysUntil(thu) / 7
    return weekYear to weekNo
}

fun List<AppUsage>.toDailyUsageMinutesForChart(startDate: LocalDate?): Map<Int, Double>{
    if (startDate == null){
        return emptyMap()
    }

    val dailyUsageTime = this
        .filter { it.date == startDate }
        .groupBy {usage ->
            val hour = usage.usageTime.hour
            if (hour == 0) 24 else hour
        }
        .mapValues { (_, usages)->
            usages.sumOf { it.usageMillis.toDouble() } /60_000.0
        }
    return (1..24).associateWith { hour->
        dailyUsageTime[hour]?:0.0
    }

}

fun List<AppUsage>.toWeeklyUsageMinutesForChart(startDate: LocalDate?): Map<Int, Double> {
    if (startDate == null){
        return emptyMap()
    }
    // startDate haftaning boshini (dushanba) topamiz
    val startOfWeek = startDate.minus(startDate.dayOfWeek.ordinal.toLong(), DateTimeUnit.DAY)

    // Shu haftaning 7 kunlik sanalarini olamiz (dushanba..yakshanba)
    val weekDays = (0..6).map { startOfWeek.plus(it, DateTimeUnit.DAY) }

    // Sana bo‘yicha yig‘ib olish
    val groupedByDate = this.groupBy { it.date }
        .mapValues { (_, usages) ->
            usages.sumOf { it.usageMillis.toDouble() } / 60_000.0  // minut
        }

    // 1..7 orasida har kuni qiymat bo‘lishi kerak
    return (1..7).associateWith { index ->
        val day = weekDays[index - 1]
        groupedByDate[day] ?: 0.0
    }


}
fun List<AppUsage>.toWeeklyAverage(startDate: LocalDate?): HourMinute {
    if (startDate == null){
        return HourMinute()
    }
    // startDate haftaning boshini topamiz (dushanba)
    val startOfWeek = startDate.minus(startDate.dayOfWeek.ordinal.toLong(), DateTimeUnit.DAY)

    // Shu haftaning 7 kunlik sanalarini olamiz (dushanba..yakshanba)
    val weekDays = (0..6).map { startOfWeek.plus(it, DateTimeUnit.DAY) }

    // Sana bo‘yicha yig‘ib olish
    val groupedByDate = this.groupBy { it.date }
        .mapValues { (_, usages) ->
            usages.sumOf { it.usageMillis }
        }

    // Faqat mavjud (nol bo‘lmagan) usage millis
    val values = weekDays.mapNotNull { day ->
        groupedByDate[day]?.takeIf { it > 0 }
    }

    // O‘rtacha millis hisoblash
    val avgMillis = if (values.isEmpty()) 0L else (values.average().toLong())

    // HourMinute ga aylantirish
    return avgMillis.toHourMinute()
}

fun List<AppUsage>.toDailyAverage(startDate: LocalDate?): HourMinute{
    if (startDate == null){
        return HourMinute()
    }
    val dailyUsageTime = this
        .filter { it.date == startDate }
        .sumOf { it.usageMillis }.toHourMinute()

    return dailyUsageTime
}


fun List<AppUsage>.toUsageUi(startDate: LocalDate?, endDate: LocalDate?): List<AppUsageUi> {
    if (startDate == null || endDate == null){
        return emptyList()
    }
    return this
        .filter { it.date >= startDate && it.date<=endDate}
        .groupBy { it.packageName }
        .map { (pkg, usages) ->
            val totalMillis = usages.sumOf { it.usageMillis }
            val totalMinutes = totalMillis / 60_000
            val hours = (totalMinutes / 60).toInt()
            val minutes = (totalMinutes % 60).toInt()

            AppUsageUi(
                packageName = pkg,
                name = usages.first().appName,   // birinchi AppUsage'dan olish mumkin
                icon = "",                       // ⚠️ siz bu yerda haqiqiy icon path/url ni qo‘yishingiz kerak
                usageTime = HourMinute(hours, minutes),
                allowed = false
            )
        }
        .sortedByDescending { it.usageTime.hour * 60 + it.usageTime.minute }
}

fun Long.toHourMinute(): HourMinute {
    val hours = this / (1000 * 60 * 60)
    val minutes = (this / (1000 * 60)) % 60

    return HourMinute(hours.toInt(), minutes.toInt())
}


