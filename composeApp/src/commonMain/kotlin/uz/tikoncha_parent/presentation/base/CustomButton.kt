package uz.saidburxon.newedu.presentation.base

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.ui.*


import uz.tikoncha_parent.ui.TextFieldCornerRadius
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun CustomButton(
    text:String,
    fontSize:TextUnit = NormalTextSize,
    fontWeight: FontWeight = FontWeight.SemiBold,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.extendedColor.primaryColor,
    textColor: Color = OnPrimaryColor,
    enabled:Boolean = true,
    onClick:()->Unit,
    leadingIcon: (@Composable () -> Unit)? = null,
    endingIcon: (@Composable () -> Unit)? = null,
    shape: Shape = RoundedCornerShape(TextFieldCornerRadius),
    disabledContainerColor: Color = MaterialTheme.extendedColor.disabledBgColor
) {

    val contentColor = if (enabled) textColor else MaterialTheme.extendedColor.disabledContentColor


    Button(
        onClick = onClick,
        modifier = modifier
            .height(ButtonHeight),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = MaterialTheme.extendedColor.disabledContentColor
        ),
        shape = shape,
        enabled = enabled,
        contentPadding = PaddingValues(horizontal = 5.dp)
    ) {
        if (leadingIcon != null) {
            leadingIcon()
            SpaceSmall()
        }

        CustomText(
            color = contentColor,
            text = text,
            fontWeight = fontWeight,
            fontSize = fontSize
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
    TikonchaParentTheme(
        ThemeMode.DARK
    ) {
        CustomButton(
            text = "OK",
            onClick = {}
        )
    }
}