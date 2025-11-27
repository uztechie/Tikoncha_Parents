package uz.tikoncha_parent.presentation.base

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.composed
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import uz.tikoncha_parent.ui.theme.extendedColor

/**
 * Universal dropShadow modifier
 */
@Composable
fun Modifier.verticalShadow(
    shape: Shape = RoundedCornerShape(10.dp),
    radius: Dp = 5.dp,
    spread: Dp = 0.dp,
    offset: Dp = 2.dp,
    darkColor: Color = MaterialTheme.extendedColor.shadowColor,
) = composed {
    this
        .dropShadow(
            shape = shape,
            shadow = Shadow(
                color = darkColor,
                offset = DpOffset(0.dp, offset),
                radius = radius,
                spread = spread
            )
        )

}



@Composable
fun Modifier.tripleShadow(
    shape: Shape = RoundedCornerShape(10.dp),
    darkColor: Color = MaterialTheme.extendedColor.shadowColor,
) = composed {

    this
        .verticalShadow(
            shape = shape,
            offset = 8.dp,
            radius = 5.dp,
            spread = 0.dp,
            darkColor = darkColor.copy(0.05f)
        )
        .verticalShadow(
            shape = shape,
            offset = 3.dp,
            radius = 3.dp,
            spread = 0.dp,
            darkColor = darkColor.copy(0.09f)
        )
        .verticalShadow(
            shape = shape,
            offset = 1.dp,
            radius = 2.dp,
            spread = 0.dp,
            darkColor = darkColor.copy(0.1f)
        )

}

@Composable
fun Modifier.topShadow(
    shape: Shape = RoundedCornerShape(10.dp),
    radius: Dp = 10.dp,
    spread: Dp = 0.dp,
    lowerOffset: Dp = 10.dp,
    color: Color = MaterialTheme.extendedColor.backgroundColor,
) = composed {
    this
        .dropShadow(
            shape = shape,
            shadow = Shadow(
                color = color,
                offset = DpOffset(0.dp, lowerOffset),
                radius = radius,
                spread = spread
            )
        )

}



@Composable
fun Modifier.bottomShadow(
    shape: Shape = RoundedCornerShape(10.dp),
    radius: Dp = 30.dp,
    spread: Dp = 0.dp,
    lowerOffset: Dp = -30.dp,
    color: Color = MaterialTheme.extendedColor.backgroundColor,
) = composed {
    this
        .dropShadow(
            shape = shape,
            shadow = Shadow(
                color = color,
                offset = DpOffset(0.dp, lowerOffset),
                radius = radius,
                spread = spread
            )
        )

}



@Composable
fun Modifier.coverShadow(
    shape: Shape = RoundedCornerShape(10.dp),
    radius: Dp = 6.dp,
    spread: Dp = 0.dp,
    upperOffset: Dp = (-4).dp,
    lowerOffset: Dp = 4.dp,
    lightColor: Color = MaterialTheme.extendedColor.shadowLightColor.copy(0.7f),
    darkColor: Color = MaterialTheme.extendedColor.shadowColor,
) = composed {
    this
        .dropShadow(
            shape = shape,
            shadow = Shadow(
                color = darkColor,
                offset = DpOffset(0.dp, 0.dp),
                radius = radius,
                spread = spread
            )
        )

}
