package org.example.project.presentation.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import org.example.project.ui.ButtonCornerRadius
import org.example.project.ui.ButtonHeight
import org.example.project.ui.DisableButtonColor
import org.example.project.ui.DisableButtonContentColor
import org.example.project.ui.OnPrimaryColor
import org.example.project.ui.PrimaryColor
import org.example.project.ui.SmallTextSize

@Composable
fun CustomButton(
    text:String,
    fontSize:TextUnit = SmallTextSize,
    modifier: Modifier = Modifier,
    color: Color = PrimaryColor,
    textColor: Color = OnPrimaryColor,
    enabled:Boolean = true,
    onClick:()->Unit,
    shape: Shape = RoundedCornerShape(ButtonCornerRadius),
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
            fontWeight = FontWeight.SemiBold,
            fontSize = fontSize
        )

    }
}