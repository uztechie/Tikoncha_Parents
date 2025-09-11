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
import uz.tikoncha_parent.ui.*


import uz.tikoncha_parent.ui.TextFieldCornerRadius
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun CustomButton(
    text:String,
    fontSize:TextUnit = NormalTextSize,
    fontWeight: FontWeight = FontWeight.Normal,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.extendedColor.primaryColor,
    textColor: Color = MaterialTheme.extendedColor.onBackgroundColor,
    enabled:Boolean = true,
    onClick:()->Unit,
    shape: Shape = RoundedCornerShape(TextFieldCornerRadius),
) {

    val contentColor = if (enabled) textColor else DisableButtonContentColor


    Button(
        onClick = onClick,
        modifier = modifier
            .height(ButtonHeight),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = contentColor,
            disabledContainerColor = DisableButtonColor,
            disabledContentColor = DisableButtonContentColor
        ),
        shape = shape,
        enabled = enabled,
        contentPadding = PaddingValues(horizontal = 5.dp)
    ) {
        CustomText(
            color = contentColor,
            text = text,
            fontWeight =fontWeight,
            fontSize = fontSize
        )

    }
}