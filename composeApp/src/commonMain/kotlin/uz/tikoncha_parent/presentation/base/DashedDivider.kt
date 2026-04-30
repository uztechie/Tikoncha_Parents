package uz.tikoncha_parent.presentation.base

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.ui.theme.AppColors

@Composable
fun DashedDivider(
    modifier: Modifier = Modifier,
    color: Color = AppColors.border.secondarySubtle,
    thickness: Dp = 1.dp,
    dashLength: Dp = 8.dp,
    gapLength: Dp = 4.dp
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(thickness)
    ) {
        val dashPx = dashLength.toPx()
        val gapPx = gapLength.toPx()
        drawLine(
            color = color,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = thickness.toPx(),
            pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(dashPx, gapPx), 0f
            )
        )
    }
}