package uz.tikoncha_parent.presentation.policy.app_site_selection

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.tikoncha_parent.presentation.base.dpToPx
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

enum class TripleCheckState {
    On, Indeterminate, Off
}

@Composable
fun AppTripleCheckbox(
    state: TripleCheckState,
    onStateChange: (TripleCheckState) -> Unit,
    modifier: Modifier = Modifier,
    borderWidthDp: Dp = 1.dp,
    cornerRadiusDp: Dp = 8.dp,
    checkedColor: Color = AppColors.bg.primary,
    uncheckedColor: Color = AppColors.border.secondary,
    iconColor: Color = AppColors.text.inverse,
    backgroundColor: Color = AppColors.bg.surface
) {
    val density = LocalDensity.current
    val cornerRadiusPx = cornerRadiusDp.dpToPx()
    val borderWidthPx = borderWidthDp.dpToPx()


    val isActive = state != TripleCheckState.Off

    val animatedBorderColor by animateColorAsState(
        targetValue = if (isActive) checkedColor else uncheckedColor,
        animationSpec = tween(300),
        label = "border"
    )

    val animatedFillColor by animateColorAsState(
        targetValue = if (isActive) checkedColor else backgroundColor,
        animationSpec = tween(300),
        label = "fill"
    )

    val iconScale by animateFloatAsState(
        targetValue = if (isActive) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "iconScale"
    )

    // Icon o'zgarishini animate qilish
    val checkProgress by animateFloatAsState(
        targetValue = when (state) {
            TripleCheckState.On -> 1f
            TripleCheckState.Indeterminate -> 0f
            TripleCheckState.Off -> 0f
        },
        animationSpec = tween(250),
        label = "checkProgress"
    )

    Canvas(
        modifier = modifier
            .padding(1.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                val next = when (state) {
                    TripleCheckState.Off -> TripleCheckState.On
                    TripleCheckState.On -> TripleCheckState.Indeterminate
                    TripleCheckState.Indeterminate -> TripleCheckState.Off
                }
                onStateChange(next)
            }
    ) {
        val w = size.width
        val h = size.height

        // Background
        drawRoundRect(
            color = animatedFillColor,
            cornerRadius = CornerRadius(cornerRadiusPx),
            size = size
        )

        // Border
        drawRoundRect(
            color = animatedBorderColor,
            cornerRadius = CornerRadius(cornerRadiusPx),
            size = size,
            style = Stroke(width = borderWidthPx)
        )

        // Icon
        if (iconScale > 0f) {
            scale(iconScale, pivot = center) {
                // Minus chiziq (har doim chiziladi active bo'lganda)
                val minusPath = Path().apply {
                    moveTo(w * 0.25f, h * 0.5f)
                    lineTo(w * 0.75f, h * 0.5f)
                }

                // Check chiziq (faqat On holatda)
                val checkPath = Path().apply {
                    moveTo(w * 0.22f, h * 0.50f)
                    lineTo(w * 0.42f, h * 0.70f)
                    lineTo(w * 0.78f, h * 0.32f)
                }

                val strokeWidth = borderWidthPx * 2
                val strokeStyle = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )

                // checkProgress: 0 = minus, 1 = check
                // Interpolate between minus and check
                if (checkProgress < 1f) {
                    drawPath(
                        path = minusPath,
                        color = iconColor.copy(alpha = 1f - checkProgress),
                        style = strokeStyle
                    )
                }
                if (checkProgress > 0f) {
                    drawPath(
                        path = checkPath,
                        color = iconColor.copy(alpha = checkProgress),
                        style = strokeStyle
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Pre() {
    TikonchaParentTheme() {
        AppTripleCheckbox(
            state = TripleCheckState.Off,
            onStateChange = {},
            modifier = Modifier
                .size(24.dp)
        )
    }
}