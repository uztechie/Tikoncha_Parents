package uz.tikoncha_parent.presentation.policy.common


import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.daqiqa
import tikoncha_parents.composeapp.generated.resources.soat

/** LocalTime ni daqiqalar soniga aylantiradi (00:00 dan). */
fun LocalTime.toMinutes(): Int = hour * 60 + minute

/** LocalTime ni "HH:mm" ko'rinishida formatlaydi. */
fun LocalTime.toHhMm(): String {
    val hh = hour.toString().padStart(2, '0')
    val mm = minute.toString().padStart(2, '0')
    return "$hh:$mm"
}

@Composable
fun formatDuration(totalMinutes: Int): String {
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60

    val hourText = stringResource(Res.string.soat)
    val minuteText = stringResource(Res.string.daqiqa)

    return when {
        hours == 0 -> "$minutes $minuteText"
        minutes == 0 -> "$hours $hourText"
        else -> "$hours $hourText, $minutes $minuteText"
    }
}