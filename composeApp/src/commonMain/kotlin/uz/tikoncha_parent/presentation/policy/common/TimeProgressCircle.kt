package uz.tikoncha_parent.presentation.policy.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.tikoncha_parent.ui.theme.AppColors

/**
 * Kichik 24 soatlik doiraviy vaqt progress indikatori (Figma spec: 88×88 dp).
 *
 * Tashqi halqa (track) + ustiga progress arc chiziladi.
 * Progress 0 daqiqadan (soat 12 yuqori pozitsiya) boshlanib, clockwise aylanadi.
 *
 * Ishlatish:
 * ```
 * // Minutes bilan (0..1440)
 * TimeProgressCircle(minutes = 360)        // 6:00 — chorak aylana
 *
 * // LocalTime bilan
 * TimeProgressCircle(time = LocalTime(6, 0))
 * ```
 *
 * @param minutes 0..1440 (360 = 6:00, 720 = 12:00)
 * @param modifier Modifier (o'lchami — default 88dp)
 * @param colors ranglar
 * @param strokeWidth halqa qalinligi (dp)
 * @param animationDurationMs qiymat o'zgarganida animatsiya davomiyligi
 */
@Composable
fun TimeProgressCircle(
    minutes: Int,
    modifier: Modifier = Modifier.size(88.dp),
    colors: TimeProgressCircleColors = TimeProgressCircleDefaults.colors(),
    strokeWidth: Dp = 8.dp,
    animationDurationMs: Int = 500
) {
    BoxWithConstraints(
        modifier = modifier.aspectRatio(1f)
    ) {
        val density = LocalDensity.current
        val sizePx = with(density) { maxWidth.toPx() }
        val strokePx = with(density) { strokeWidth.toPx() }

        // Animatable qiymat — qiymat o'zgarganida silliq o'tadi
        val progressAnim = remember { Animatable(minutes.toFloat()) }

        LaunchedEffect(minutes) {
            progressAnim.animateTo(
                targetValue = minutes.toFloat(),
                animationSpec = tween(animationDurationMs, easing = FastOutSlowInEasing)
            )
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(sizePx / 2f, sizePx / 2f)
            val radius = (sizePx - strokePx) / 2f

            // 1) Track — to'liq halqa (orqa)
            drawCircle(
                color = colors.trackColor,
                radius = radius,
                center = center,
                style = Stroke(width = strokePx)
            )

            // 2) Progress arc — yuqoridan clockwise
            val current = progressAnim.value.coerceIn(0f, 1440f)
            if (current > 0f) {
                val sweepDeg = (current / 1440f) * 360f

                drawArc(
                    color = colors.progressColor,
                    startAngle = -90f,           // yuqoridan (soat 12 pozitsiyasi)
                    sweepAngle = sweepDeg,
                    useCenter = false,
                    topLeft = Offset(
                        center.x - radius,
                        center.y - radius
                    ),
                    size = Size(radius * 2f, radius * 2f),
                    style = Stroke(
                        width = strokePx,
                        cap = StrokeCap.Round
                    )
                )
            }
        }
    }
}

/**
 * LocalTime bilan ishlash uchun overload.
 */
@Composable
fun TimeProgressCircle(
    time: LocalTime,
    modifier: Modifier = Modifier.size(88.dp),
    colors: TimeProgressCircleColors = TimeProgressCircleDefaults.colors(),
    strokeWidth: Dp = 14.dp,
    animationDurationMs: Int = 500
) {
    TimeProgressCircle(
        minutes = time.hour * 60 + time.minute,
        modifier = modifier,
        colors = colors,
        strokeWidth = strokeWidth,
        animationDurationMs = animationDurationMs
    )
}

// =====================================================================
//  COLORS
// =====================================================================

@Immutable
data class TimeProgressCircleColors(
    val trackColor: Color,       // orqa halqa
    val progressColor: Color     // progress arc
)

object TimeProgressCircleDefaults {

    @Composable
    fun colors(
        trackColor: Color = AppColors.bg.primaryContainer,
        progressColor: Color = AppColors.bg.primary
    ): TimeProgressCircleColors = TimeProgressCircleColors(
        trackColor = trackColor,
        progressColor = progressColor
    )
}

// =====================================================================
//  PREVIEW
// =====================================================================

@Preview
@Composable
private fun TimeProgressCirclePreview_6am() {
    PreviewSurfaceSmall {
        TimeProgressCircle(time = LocalTime(6, 0))
    }
}

@Preview
@Composable
private fun TimeProgressCirclePreview_Noon() {
    PreviewSurfaceSmall {
        TimeProgressCircle(time = LocalTime(12, 0))
    }
}

@Preview
@Composable
private fun TimeProgressCirclePreview_6pm() {
    PreviewSurfaceSmall {
        TimeProgressCircle(time = LocalTime(18, 0))
    }
}

@Preview
@Composable
private fun TimeProgressCirclePreview_Minutes() {
    PreviewSurfaceSmall {
        // 4 soat 30 daqiqa = 270 min
        TimeProgressCircle(minutes = 270)
    }
}

@Preview
@Composable
private fun TimeProgressCirclePreview_CustomSize() {
    PreviewSurfaceSmall {
        TimeProgressCircle(
            minutes = 420,               // 7:00
            modifier = Modifier.size(120.dp),
            strokeWidth = 12.dp
        )
    }
}

@Preview
@Composable
private fun TimeProgressCirclePreview_CustomColors() {
    PreviewSurfaceSmall(bg = Color(0xFF1E1E2A)) {
        TimeProgressCircle(
            minutes = 480,               // 8:00
            colors = TimeProgressCircleDefaults.colors(
                trackColor = Color(0xFF2D2D3D),
                progressColor = Color(0xFF7B61FF)
            ),
            strokeWidth = 10.dp
        )
    }
}

@Composable
private fun PreviewSurfaceSmall(
    bg: Color = Color(0xFFF9F3E9),
    content: @Composable () -> Unit
) {
    Surface(color = bg) {
        Box(
            modifier = Modifier
                .size(140.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}