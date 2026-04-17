package uz.tikoncha_parent.presentation.policy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import kotlinx.datetime.LocalTime
import uz.tikoncha_parent.ui.theme.AppColors

/**
 * TimeRangePicker ranglari (Figma: Parent design → Time selected).
 */
@Immutable
data class TimeRangePickerColors(
    val trackColor: Color,           // Ellipse 1: #E5E5E6
    val innerRingColor: Color,       // Ellipse 4 stroke: #CECFD2
    val outerDashedColor: Color,     // Tashqi dashed (active arc ichida): #FFFFFF
    val innerDashedColor: Color,     // Ichki dashed (raqamlar halqasida): #CECFD2
    val activeColor: Color,          // Ellipse 2 + icon: #C3955B (icon/accent-primary)
    val labelColor: Color,           // 0/6/12/18: #22262F (text/primary)
    val currentTimeColor: Color      // hozirgi vaqt nuqtasi
)

object TimeRangePickerDefaults {

    @Composable
    fun colors(
        trackColor: Color = AppColors.border.secondary,
        innerRingColor: Color = AppColors.border.primary,
        outerDashedColor: Color = AppColors.border.tertiary,
        innerDashedColor: Color = AppColors.border.primary,
        activeColor: Color = AppColors.border.accentEmphasis,
        labelColor: Color = AppColors.text.primary,
        currentTimeColor: Color = Color(0xFF2F6BFF)
    ): TimeRangePickerColors = TimeRangePickerColors(
        trackColor = trackColor,
        innerRingColor = innerRingColor,
        outerDashedColor = outerDashedColor,
        innerDashedColor = innerDashedColor,
        activeColor = activeColor,
        labelColor = labelColor,
        currentTimeColor = currentTimeColor
    )
}

// -------- LocalTime overload --------

@Composable
fun TimeRangePicker(
    start: LocalTime,
    end: LocalTime,
    onTimeChange: (start: LocalTime, end: LocalTime) -> Unit,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier,
    reverse: Boolean = false,
    colors: TimeRangePickerColors = TimeRangePickerDefaults.colors(),
    animationDurationMs: Int = 350,
    startIcon: (@Composable () -> Unit)? = null,
    endIcon: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    centerContent: @Composable () -> Unit = {}
) {
    TimeRangePicker(
        startMinutes = start.hour * 60 + start.minute,
        endMinutes = end.hour * 60 + end.minute,
        onTimeChange = { s, e ->
            onTimeChange(
                LocalTime(hour = s / 60, minute = s % 60),
                LocalTime(hour = e / 60, minute = e % 60)
            )
        },
        modifier = modifier,
        inverted = reverse,
        colors = colors,
        animationDurationMs = animationDurationMs,
        startIcon = startIcon,
        endIcon = endIcon,
        onClick = onClick,
        centerContent = centerContent
    )
}
