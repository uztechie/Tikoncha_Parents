package org.example.project.presentation.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.project.ui.ButtonCornerRadius
import org.example.project.ui.ButtonHeight
import org.example.project.ui.DisableButtonColor
import org.example.project.ui.DisableButtonContentColor
import org.example.project.ui.LargeTextSize
import org.example.project.ui.NormalTextSize
import org.example.project.ui.PrimaryColor
import org.example.project.ui.SmallTextSize
import org.example.project.ui.TextColor
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CustomOutlinedButton(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    textColor: Color = TextColor,
    borderColor: Color = PrimaryColor,
    enabled: Boolean = true,
    onClick: () -> Unit,
    shape: Shape = RoundedCornerShape(ButtonCornerRadius),
    leadingIcon: (@Composable () -> Unit)? = null,
    endingIcon: (@Composable () -> Unit)? = null
) {

    val borderColor = if (enabled) borderColor else DisableButtonContentColor
    val contentColor = if (enabled) textColor else DisableButtonContentColor

    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(ButtonHeight),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = backgroundColor,
            contentColor = textColor,
            disabledContainerColor = DisableButtonColor,
            disabledContentColor = DisableButtonContentColor
        ),
        border = BorderStroke(1.dp, borderColor),
        shape = shape,
        enabled = enabled,
        contentPadding = PaddingValues(horizontal = 5.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
        ) {

            if (leadingIcon != null) {
                leadingIcon()
            }

            CustomText(
                text = text,
                color = contentColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = LargeTextSize
            )

            if (endingIcon != null) {
                endingIcon()
            }

        }


    }
}

@Preview
@Composable
private fun Pre() {
    CustomOutlinedButton(
        text = "smile",
        onClick = {},
        enabled = false
    )
}