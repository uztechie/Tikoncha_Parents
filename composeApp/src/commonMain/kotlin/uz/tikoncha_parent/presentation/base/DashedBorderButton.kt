package uz.tikoncha_parent.presentation.base

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun DashedBorderButton(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = AppColors.button.accentEmphasisPressed,
    contentColor: Color = AppColors.text.accentEmphasis,
    enabled: Boolean = true,
    borderColor: Color = AppColors.border.accentEmphasis,
    leadingIcon: (@Composable () -> Unit)? = null,
    endingIcon: (@Composable () -> Unit)? = null,
    onClick: () -> Unit,
    shape: Shape = CircleShape,
    style: TextStyle = AppTypography.titleSmMedium,
    disabledContainerColor: Color = AppColors.action.disabled,
    contentPadding: PaddingValues = PaddingValues(horizontal = 5.dp),
) {


    Button(
        onClick = onClick,
        modifier = modifier
            .drawBehind {
                val cornerRadius = size.height / 2
                drawRoundRect(
                    color = borderColor,
                    style = Stroke(
                        width = 3f,
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(20f, 8f),
                            phase = 0f
                        )
                    ),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                )
            }
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = AppColors.text.disabled
        ),
        shape = shape,
        enabled = enabled,
        contentPadding = contentPadding
    ) {
        if (leadingIcon != null) {
            leadingIcon()
        }

        Text(
            style = style,
            text = text,
            color = contentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 2.dp),
        )

        if (endingIcon != null) {
            SpaceSmall()
            endingIcon()
        }

    }
}

@Preview
@Composable
private fun Preview() {

    TikonchaParentTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            DashedBorderButton(
                text = "Salom",
                onClick = {}
            )
        }
    }
}