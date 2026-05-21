package uz.tikoncha_parent.presentation.statistic


import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import uz.tikoncha_parent.domain.model.HourMinute
import uz.tikoncha_parent.domain.model.app_usage.AppUsage


internal fun LocalDate.startOfWeek(): LocalDate {
    // kotlinx-datetime: DayOfWeek.ordinal — MONDAY=0 ... SUNDAY=6
    val ord = dayOfWeek.ordinal
    return this.minus(ord, DateTimeUnit.DAY)
}

private fun dayLabelFor(date: LocalDate, today: LocalDate): DayLabel = when (date) {
    today -> DayLabel.TODAY
    today.minus(1, DateTimeUnit.DAY) -> DayLabel.YESTERDAY
    else -> DayLabel.NONE
}

/* ============ AGGREGATION ============ */

private fun sumInRange(
    usages: List<AppUsage>, from: LocalDate, toIncl: LocalDate
): Long = usages.sumOf { app ->
    app.usage.entries.sumOf { (d, hours) ->
        if (d in from..toIncl) hours.values.sum() else 0L
    }
}

private fun sumForDay(usages: List<AppUsage>, day: LocalDate): Long =
    usages.sumOf { it.usage[day]?.values?.sum() ?: 0L }

private fun sumForDayHours(
    usages: List<AppUsage>, day: LocalDate, hourFromIncl: Int, hourToIncl: Int
): Long = usages.sumOf { app ->
    val hours = app.usage[day] ?: return@sumOf 0L
    hours.entries.sumOf { (h, ms) -> if (h in hourFromIncl..hourToIncl) ms else 0L }
}

/* ============ PAGES ============ */

fun buildWeeklyPages(usages: List<AppUsage>, today: LocalDate): List<PagePeriod> {
    val allDates = usages.flatMap { it.usage.keys }.toSet()
    val anchor = (allDates.minOrNull() ?: today).startOfWeek()

    val weeks = mutableListOf<Pair<LocalDate, LocalDate>>()
    var cursor = anchor
    while (cursor <= today) {
        val end = cursor.plus(6, DateTimeUnit.DAY).coerceAtMost(today)
        weeks.add(cursor to end)
        cursor = cursor.plus(7, DateTimeUnit.DAY)
    }

    return weeks.map { (start, end) ->
        val total = sumInRange(usages, start, end)
        val perDay = (0..6).map { i ->
            val d = start.plus(i, DateTimeUnit.DAY)
            if (d in start..end) sumForDay(usages, d) else 0L
        }
        val activeDays = perDay.count { it > 0L }
        val avgMs = if (activeDays > 0) total / activeDays else 0L

        PagePeriod(
            title = PageTitle.Week(
                startDay = start.dayOfMonth,
                startMonthIndex = start.month.number - 1,
                endDay = end.dayOfMonth,
                endMonthIndex = end.month.number - 1
            ),
            subtitle = HourMinute.fromMillis(total),
            chartSubtitle = ChartSubtitle.WeeklyAverage(HourMinute.fromMillis(avgMs)),
            startDate = start,
            endDateInclusive = end,
            totalMillis = total
        )
    }
}

fun buildDailyPages(usages: List<AppUsage>, today: LocalDate): List<PagePeriod> {
    val allDates = usages.flatMap { it.usage.keys }.toSet()
    val minDate = allDates.minOrNull() ?: today

    val days = generateSequence(minDate) { d ->
        d.plus(1, DateTimeUnit.DAY).takeIf { it <= today }
    }.toList()

    return days.map { d ->
        val total = sumForDay(usages, d)
        PagePeriod(
            title = PageTitle.Day(
                dayLabel = dayLabelFor(d, today),
                day = d.dayOfMonth,
                monthIndex = d.month.number - 1
            ),
            subtitle = HourMinute.fromMillis(total),
            chartSubtitle = null,
            startDate = d,
            endDateInclusive = d,
            totalMillis = total
        )
    }
}

/* ============ BARS ============ */

fun emptyBars(mode: DateSelectionType): List<ChartBarUi> = when (mode) {
    DateSelectionType.WEEK -> (0..6).map { ChartBarUi(it, 0.0, 0L) }
    DateSelectionType.DAY  -> (0..11).map { ChartBarUi(it, 0.0, 0L) }
}

fun buildWeeklyBars(usages: List<AppUsage>, page: PagePeriod): List<ChartBarUi> =
    (0..6).map { i ->
        val day = page.startDate.plus(i, DateTimeUnit.DAY)
        val ms = if (day in page.startDate..page.endDateInclusive)
            sumForDay(usages, day) else 0L
        ChartBarUi(slotIndex = i, valueMinutes = ms / 60_000.0, totalMillis = ms)
    }

fun buildDailyBars(usages: List<AppUsage>, page: PagePeriod): List<ChartBarUi> =
    (0..11).map { slot ->
        val hFrom = slot * 2
        val hTo = hFrom + 1
        val ms = sumForDayHours(usages, page.startDate, hFrom, hTo)
        ChartBarUi(slotIndex = slot, valueMinutes = ms / 60_000.0, totalMillis = ms)
    }

/* ============ TOP APPS ============ */

fun buildTopApps(usages: List<AppUsage>, page: PagePeriod): List<TopAppUi> =
    usages.mapNotNull { app ->
        val ms = app.usage.entries.sumOf { (d, hours) ->
            if (d in page.startDate..page.endDateInclusive) hours.values.sum() else 0L
        }
        if (ms <= 0L) null
        else TopAppUi(
            packageName = app.packageName,
            name = app.name,
            iconUrl = app.iconUrl,
            usageMillis = ms,
            usage = HourMinute.fromMillis(ms)
        )
    }.sortedByDescending { it.usageMillis }

/* ============ DIALOG ============ */

fun buildWeeklyBarDetails(
    usages: List<AppUsage>, page: PagePeriod, bar: ChartBarUi
): UsageDetailsUi {
    val day = page.startDate.plus(bar.slotIndex, DateTimeUnit.DAY)
    val items = usages.mapNotNull { app ->
        val ms = app.usage[day]?.values?.sum() ?: 0L
        if (ms <= 0L) null
        else UsageDetailItem(
            packageName = app.packageName,
            name = app.name,
            iconUrl = app.iconUrl,
            usageMillis = ms,
            usage = HourMinute.fromMillis(ms)
        )
    }.sortedByDescending { it.usageMillis }

    return UsageDetailsUi(
        title = UsageDetailsTitle.WeekdayDate(
            weekdayIndex = bar.slotIndex,
            day = day.dayOfMonth,
            monthIndex = day.month.number - 1
        ),
        total = HourMinute.fromMillis(bar.totalMillis),
        items = items
    )
}

fun buildDailyBarDetails(
    usages: List<AppUsage>, page: PagePeriod, bar: ChartBarUi
): UsageDetailsUi {
    val day = page.startDate
    val hFrom = bar.slotIndex * 2
    val hTo = hFrom + 1
    val items = usages.mapNotNull { app ->
        val hours = app.usage[day] ?: return@mapNotNull null
        val ms = hours.entries.sumOf { (h, m) -> if (h in hFrom..hTo) m else 0L }
        if (ms <= 0L) null
        else UsageDetailItem(
            packageName = app.packageName,
            name = app.name,
            iconUrl = app.iconUrl,
            usageMillis = ms,
            usage = HourMinute.fromMillis(ms)
        )
    }.sortedByDescending { it.usageMillis }

    return UsageDetailsUi(
        title = UsageDetailsTitle.HourRange(
            day = day.dayOfMonth,
            monthIndex = day.month.number - 1,
            hourFrom = hFrom,
            hourToExclusive = hTo + 1
        ),
        total = HourMinute.fromMillis(bar.totalMillis),
        items = items
    )
}