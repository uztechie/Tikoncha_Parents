package uz.tikoncha_parent.presentation.task

import androidx.compose.runtime.Composable
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.month_april
import tikoncha_parents.composeapp.generated.resources.month_august
import tikoncha_parents.composeapp.generated.resources.month_december
import tikoncha_parents.composeapp.generated.resources.month_february
import tikoncha_parents.composeapp.generated.resources.month_january
import tikoncha_parents.composeapp.generated.resources.month_july
import tikoncha_parents.composeapp.generated.resources.month_june
import tikoncha_parents.composeapp.generated.resources.month_march
import tikoncha_parents.composeapp.generated.resources.month_may
import tikoncha_parents.composeapp.generated.resources.month_november
import tikoncha_parents.composeapp.generated.resources.month_october
import tikoncha_parents.composeapp.generated.resources.month_september
import tikoncha_parents.composeapp.generated.resources.weekday_friday
import tikoncha_parents.composeapp.generated.resources.weekday_monday
import tikoncha_parents.composeapp.generated.resources.weekday_saturday
import tikoncha_parents.composeapp.generated.resources.weekday_sunday
import tikoncha_parents.composeapp.generated.resources.weekday_thursday
import tikoncha_parents.composeapp.generated.resources.weekday_tuesday
import tikoncha_parents.composeapp.generated.resources.weekday_wednesday
import kotlin.time.Instant

@Composable
fun formatDayMonthWithWeekday(epochMs: Long?): String {
    if (epochMs == null || epochMs <= 0L) return ""

    val instant = Instant.fromEpochMilliseconds(epochMs)
    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

    val day = dateTime.day
    val monthRes = monthStringRes(dateTime.monthNumber)
    val monthName = if (monthRes != null) stringResource(monthRes) else ""

    val weekdayRes = weekdayStringRes(dateTime.dayOfWeek)
    val weekdayName = if (weekdayRes != null) stringResource(weekdayRes) else ""

    return "$day-$monthName, $weekdayName"
}
private fun monthStringRes(monthNumber: Int): StringResource? = when (monthNumber) {
    1 -> Res.string.month_january
    2 -> Res.string.month_february
    3 -> Res.string.month_march
    4 -> Res.string.month_april
    5 -> Res.string.month_may
    6 -> Res.string.month_june
    7 -> Res.string.month_july
    8 -> Res.string.month_august
    9 -> Res.string.month_september
    10 -> Res.string.month_october
    11 -> Res.string.month_november
    12 -> Res.string.month_december
    else -> null
}
private fun weekdayStringRes(dayOfWeek: DayOfWeek): StringResource? = when (dayOfWeek) {
    DayOfWeek.MONDAY    -> Res.string.weekday_monday
    DayOfWeek.TUESDAY   -> Res.string.weekday_tuesday
    DayOfWeek.WEDNESDAY -> Res.string.weekday_wednesday
    DayOfWeek.THURSDAY  -> Res.string.weekday_thursday
    DayOfWeek.FRIDAY    -> Res.string.weekday_friday
    DayOfWeek.SATURDAY  -> Res.string.weekday_saturday
    DayOfWeek.SUNDAY    -> Res.string.weekday_sunday
    else                -> null
}