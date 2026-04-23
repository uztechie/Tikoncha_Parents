package uz.tikoncha_parent.presentation.policy.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.d
import tikoncha_parents.composeapp.generated.resources.s
import uz.tikoncha_parent.ui.theme.AppColors

/**
 * 24 soatlik doiraviy vaqt progress indikatori (Figma spec: 88×88 dp).
 *
 * Markazida "Xs, Yd" formatida vaqt matni chiqadi. Matn o'lchami:
 *  • diametr bo'yicha proporsional (~20% diameter),
 *  • matn uzunligi bo'yicha cheklangan (circle ichidan chiqmasligi uchun),
 *  • [textVisibilityThreshold] dan kichik bo'lsa smooth fade bilan yashiriladi.
 */
@Composable
fun TimeProgressCircle(
    minutes: Int,
    modifier: Modifier = Modifier.size(88.dp),
    colors: TimeProgressCircleColors = TimeProgressCircleDefaults.colors(),
    strokeWidth: Dp = 8.dp,
    animationDurationMs: Int = 500,
    showText: Boolean = true,
    textHorizontalPadding: Dp = 12.dp,
    textVisibilityThreshold: Dp = 48.dp
) {
    BoxWithConstraints(
        modifier = modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        val density = LocalDensity.current
        val sizeDp: Dp = maxWidth
        val sizePx = with(density) { sizeDp.toPx() }
        val strokePx = with(density) { strokeWidth.toPx() }
        val textMeasurer = rememberTextMeasurer()

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

            drawCircle(
                color = colors.trackColor,
                radius = radius,
                center = center,
                style = Stroke(width = strokePx)
            )

            val current = progressAnim.value.coerceIn(0f, 1440f)
            if (current > 0f) {
                val sweepDeg = (current / 1440f) * 360f
                drawArc(
                    color = colors.progressColor,
                    startAngle = -90f,
                    sweepAngle = sweepDeg,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2f, radius * 2f),
                    style = Stroke(width = strokePx, cap = StrokeCap.Round)
                )
            }
        }

        if (showText) {
            val targetAlpha = if (sizeDp >= textVisibilityThreshold) 1f else 0f
            val textAlpha by animateFloatAsState(
                targetValue = targetAlpha,
                animationSpec = tween(durationMillis = 200),
                label = "time-progress-text-alpha"
            )

            if (textAlpha > 0.01f) {
                val safeMinutes = minutes.coerceIn(0, 1440)
                val hours = safeMinutes / 60
                val mins = safeMinutes % 60
                val textContent = "${hours}${stringResource(Res.string.s)} ${mins}${stringResource(Res.string.d)}"

                // Arc va matn orasidagi bo'sh joy (ikki tomondan)
                val horizontalPaddingPx = with(density) { textHorizontalPadding.toPx() }
                val availableWidthPx = (sizePx - strokePx * 2f - horizontalPaddingPx * 2f)
                    .coerceAtLeast(0f)

                val proportionalSp = sizeDp.value * 0.20f

                val referenceSp = 100f
                val measuredWidthPx = textMeasurer.measure(
                    text = textContent,
                    style = TextStyle(
                        fontSize = referenceSp.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                ).size.width.toFloat()

                val maxSpByWidth = if (measuredWidthPx > 0f && availableWidthPx > 0f) {
                    referenceSp * (availableWidthPx / measuredWidthPx)
                } else {
                    proportionalSp
                }

                val fontSizeSp = minOf(proportionalSp, maxSpByWidth)
                    .coerceAtLeast(8f)
                    .sp

                Text(
                    text = textContent,
                    color = AppColors.text.primary,
                    fontSize = fontSizeSp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun TimeProgressCircle(
    time: LocalTime,
    modifier: Modifier = Modifier.size(88.dp),
    colors: TimeProgressCircleColors = TimeProgressCircleDefaults.colors(),
    strokeWidth: Dp = 14.dp,
    animationDurationMs: Int = 500,
    showText: Boolean = true,
    textVisibilityThreshold: Dp = 48.dp,
    textHorizontalPadding: Dp = 12.dp
) {
    TimeProgressCircle(
        minutes = time.hour * 60 + time.minute,
        modifier = modifier,
        colors = colors,
        strokeWidth = strokeWidth,
        animationDurationMs = animationDurationMs,
        showText = showText,
        textVisibilityThreshold = textVisibilityThreshold,
        textHorizontalPadding = textHorizontalPadding
    )
}

// =====================================================================
//  COLORS
// =====================================================================

@Immutable
data class TimeProgressCircleColors(
    val trackColor: Color,
    val progressColor: Color,
    val textColor: Color
)

object TimeProgressCircleDefaults {
    @Composable
    fun colors(
        trackColor: Color = AppColors.bg.primaryContainer,
        progressColor: Color = AppColors.bg.primary,
        textColor: Color = AppColors.bg.primary
    ): TimeProgressCircleColors = TimeProgressCircleColors(
        trackColor = trackColor,
        progressColor = progressColor,
        textColor = textColor
    )
}

// =====================================================================
//  PREVIEW
// =====================================================================

@Preview
@Composable
private fun TimeProgressCirclePreview_1h3m() {
    // Qisqa matn: "1s, 3d" — diametrga proporsional ishlaydi
    PreviewSurfaceSmall { TimeProgressCircle(minutes = 63) }
}

@Preview
@Composable
private fun TimeProgressCirclePreview_MaxLength() {
    // Eng uzun matn: "23s, 59d" — kenglik cheklovi ishga tushadi
    PreviewSurfaceSmall { TimeProgressCircle(minutes = 23 * 60 + 59) }
}

@Preview
@Composable
private fun TimeProgressCirclePreview_Noon() {
    PreviewSurfaceSmall { TimeProgressCircle(time = LocalTime(12, 0)) }
}

@Preview
@Composable
private fun TimeProgressCirclePreview_TooSmall() {
    PreviewSurfaceSmall {
        TimeProgressCircle(
            time = LocalTime(9, 30),
            modifier = Modifier.size(40.dp),
            strokeWidth = 4.dp
        )
    }
}

@Preview
@Composable
private fun TimeProgressCirclePreview_Large() {
    PreviewSurfaceSmall {
        TimeProgressCircle(
            minutes = 23 * 60 + 59,
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
            minutes = 495,
            colors = TimeProgressCircleDefaults.colors(
                trackColor = Color(0xFF2D2D3D),
                progressColor = Color(0xFF7B61FF),
                textColor = Color.White
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