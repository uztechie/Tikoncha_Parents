package uz.tikoncha_parent.presentation.tracking

import androidx.compose.runtime.Composable
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
import tikoncha_parents.composeapp.generated.resources.tracking_days_ago
import tikoncha_parents.composeapp.generated.resources.tracking_hours_ago
import tikoncha_parents.composeapp.generated.resources.tracking_just_now
import tikoncha_parents.composeapp.generated.resources.tracking_minutes_ago
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
fun formatLastSeen(epochMs: Long?): String {
    if (epochMs == null || epochMs <= 0L) return ""

    val now = Clock.System.now().toEpochMilliseconds()
    val diff = now - epochMs
    if (diff < 0) return formatDate(epochMs)

    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        seconds < 60 -> stringResource(Res.string.tracking_just_now)
        minutes < 60 -> "$minutes ${stringResource(Res.string.tracking_minutes_ago)}"
        hours < 24 -> "$hours ${stringResource(Res.string.tracking_hours_ago)}"
        days < 7 -> "$days ${stringResource(Res.string.tracking_days_ago)}"
        else -> formatDate(epochMs)
    }
}

@Composable
fun formatDate(epochMs: Long?): String {
    if (epochMs == null || epochMs <= 0L) return ""

    val instant = Instant.fromEpochMilliseconds(epochMs)
    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

    val day = dateTime.day
    val monthRes = monthStringRes(dateTime.monthNumber)
    val monthName = if (monthRes != null) stringResource(monthRes) else ""
    val year = dateTime.year

    return "$day-$monthName $year"
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