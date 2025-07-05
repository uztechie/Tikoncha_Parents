package org.example.project.presentation.base

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.project.presentation.base.theme.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.lock
import uz.saidburxon.newedu.presentation.base.CustomText

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
                fontSize = SmallTextSize
            )

            if (endingIcon != null) {
                endingIcon()
            }

        }


    }
}

@Preview()
@Composable
private fun Pre() {
    CustomOutlinedButton(
        text = "smile",
        onClick = {},
        leadingIcon = {
            Icon(
                painter = painterResource(Res.drawable.lock),
                contentDescription = "",
                tint = PrimaryColor,
            )
        },
        enabled = false
    )
}
