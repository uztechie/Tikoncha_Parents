package uz.tikoncha_parent.presentation.policy.app_site_selection

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.tikoncha_parent.presentation.base.dpToPx
import uz.tikoncha_parent.presentation.base.singleClick
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun AppCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {

    val markColor = AppColors.text.inverse
    val animatedColor by animateColorAsState(
        targetValue = if (checked) AppColors.bg.primary else AppColors.border.secondary,
        animationSpec = tween(durationMillis = 300),
        label = "borderColor"
    )

    val animatedFill by animateColorAsState(
        targetValue = if (checked) AppColors.bg.primary else AppColors.bg.surface,
        animationSpec = tween(durationMillis = 300),
        label = "fillColor"
    )

    val scale by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "checkScale"
    )

    val strokePx = 1.dp.dpToPx()
    val radius = 8.dp.dpToPx()

    Canvas(
        modifier = modifier
            .size(24.dp)
            .singleClick { onCheckedChange(!checked) }
            .padding(1.dp)
    ) {


        // Background + border
        drawRoundRect(
            color = animatedFill,
            cornerRadius = CornerRadius(radius),
            size = this.size
        )
        drawRoundRect(
            color = animatedColor,
            cornerRadius = CornerRadius(radius),
            size = this.size,
            style = Stroke(width = strokePx)
        )

        // Checkmark
        if (scale > 0f) {
            val path = Path().apply {
                val w = this@Canvas.size.width
                val h = this@Canvas.size.height
                moveTo(w * 0.22f, h * 0.50f)
                lineTo(w * 0.42f, h * 0.70f)
                lineTo(w * 0.78f, h * 0.32f)
            }
            scale(scale, pivot = center) {
                drawPath(
                    path = path,
                    color = markColor,
                    style = Stroke(
                        width = strokePx * 2,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
        }
    }


}

@Preview
@Composable
private fun Pre() {
    TikonchaParentTheme() {
        AppCheckbox(
            checked = false,
            onCheckedChange = {}
        )
    }
}