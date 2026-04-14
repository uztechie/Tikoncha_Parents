package uz.tikoncha_parent.presentation.base

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CustomLoadingIndicator(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    lineCount: Int = 8,
    size: Dp = 48.dp,
    lineWidth: Dp = 3.dp,
    lineLength: Dp = 10.dp
) {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 800,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(
        modifier = modifier.size(size)
    ) {
        val centerX = this.size.width / 2
        val centerY = this.size.height / 2
        val radius = this.size.width / 2 - lineLength.toPx()

        rotate(angle, Offset(centerX, centerY)) {
            for (i in 0 until lineCount) {
                val lineAngle = (i * 360f / lineCount) * (PI / 180f).toFloat()
                val alpha = 1f - (i.toFloat() / lineCount) * 0.7f

                val startX = centerX + radius * cos(lineAngle)
                val startY = centerY + radius * sin(lineAngle)
                val endX = centerX + (radius + lineLength.toPx()) * cos(lineAngle)
                val endY = centerY + (radius + lineLength.toPx()) * sin(lineAngle)

                drawLine(
                    color = color.copy(alpha = alpha),
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = lineWidth.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
    }
}