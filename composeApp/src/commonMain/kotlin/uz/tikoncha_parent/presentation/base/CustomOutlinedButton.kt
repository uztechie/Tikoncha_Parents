package uz.tikoncha_parent.presentation.base

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun CustomOutlinedButton(
    text: String,
    fontSize: TextUnit = NormalTextSize,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.extendedColor.backgroundColor,
    textColor: Color = MaterialTheme.extendedColor.primaryColor,
    borderColor: Color = MaterialTheme.extendedColor.primaryColor,
    enabled: Boolean = true,
    onClick: () -> Unit,
    shape: Shape = RoundedCornerShape(TextFieldCornerRadius),
    leadingIcon: (@Composable () -> Unit)? = null,
    endingIcon: (@Composable () -> Unit)? = null
) {

    val borderColor = if (enabled) borderColor else MaterialTheme.extendedColor.disabledBgColor
    val contentColor = if (enabled) textColor else MaterialTheme.extendedColor.disabledContentColor

    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(ButtonHeight),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContainerColor = backgroundColor,
            disabledContentColor = contentColor
        ),
        border = BorderStroke(1.dp, borderColor),
        shape = shape,
        enabled = enabled,
        contentPadding = PaddingValues(horizontal = 5.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            modifier = Modifier
                .padding(horizontal = 10.dp)
        ) {

            if (leadingIcon != null) {
                leadingIcon()
            }

            CustomText(
                text = text,
                color = contentColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = fontSize
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
