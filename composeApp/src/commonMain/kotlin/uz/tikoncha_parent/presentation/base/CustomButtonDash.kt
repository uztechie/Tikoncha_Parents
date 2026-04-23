package uz.tikoncha_parent.presentation.base


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import kotlin.time.TimeSource
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
@Composable
fun CustomButtonDash(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    border: Boolean = true,
    borderWidth: Dp = 1.dp,
    dashLength: Dp = 6.dp,
    dashGap: Dp = 3.dp,
    height: Dp = DialogButtonHeight,
    style: TextStyle = AppTypography.titleSmMedium,
    color: Color = AppColors.bg.surface,
    textColor: Color = AppColors.text.accentEmphasis,
    borderColor: Color = AppColors.border.accentEmphasis,
    leadingIcon: (@Composable () -> Unit)? = null,
    endingIcon: (@Composable () -> Unit)? = null,
    shape: Shape = RoundedCornerShape(ButtonCornerRadius),
    disabledContainerColor: Color = AppColors.section.disabledTertiary,
    contentPadding: PaddingValues = PaddingValues(horizontal = 5.dp),
) {
    val contentColor = if (enabled) textColor else AppColors.text.disabledTertiary
    val actualBorderColor = if (border) borderColor else Color.Transparent
    val timSource = TimeSource.Monotonic
    var lastClickTime by remember { mutableStateOf(timSource.markNow()) }

    val debouncedClick = {
        if (lastClickTime.elapsedNow().inWholeMilliseconds > 600L) {
            lastClickTime = timSource.markNow()
            onClick()
        }
    }

    Button(
        onClick = debouncedClick,
        modifier = modifier
            .drawWithContent {
                drawContent() // Avval Button kontentini chizamiz

                // Keyin ustidan dashed border chizamiz
                val strokeWidthPx = borderWidth.toPx()
                val dashPx = dashLength.toPx()
                val gapPx = dashGap.toPx()
                val cornerRadius = size.height / 2
                val inset = strokeWidthPx / 2

                drawRoundRect(
                    color = actualBorderColor,
                    topLeft = Offset(inset, inset),
                    size = Size(
                        size.width - strokeWidthPx,
                        size.height - strokeWidthPx
                    ),
                    style = Stroke(
                        width = strokeWidthPx,
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(dashPx, gapPx),
                            phase = 0f
                        )
                    ),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                )
            }
            .height(height),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = AppColors.text.disabledTertiary
        ),
        shape = shape,
        enabled = enabled,
        contentPadding = contentPadding
    ) {
        if (leadingIcon != null) {
            leadingIcon()
            SpaceSmall()
        }
        Text(color = contentColor, style = style, text = text)
        if (endingIcon != null) {
            SpaceSmall()
            endingIcon()
        }
    }
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.LIGHT
    ) {
        CustomButtonDash(
            text = "OK",
            onClick = {}
        )
    }
}