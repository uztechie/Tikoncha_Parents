package uz.tikoncha_parent.presentation.base

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.ui.ButtonCornerRadius
import uz.tikoncha_parent.ui.ButtonHeight
import uz.tikoncha_parent.ui.DisableButtonContentColor
import uz.tikoncha_parent.ui.OnPrimaryColor
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.SmallTextSize
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.theme.extendedColor


@Composable
fun CustomLoadingButton(
    text:String,
    fontSize:TextUnit = SmallTextSize,
    modifier: Modifier = Modifier,
    color: Color = PrimaryColor,
    textColor: Color = OnPrimaryColor,
    enabled:Boolean = true,
    onClick:()->Unit,
    shape: Shape = RoundedCornerShape(ButtonCornerRadius),
    loading:Boolean = false
) {



    val contentColor = if (enabled) textColor else textColor


    Button(
        onClick = onClick,
        modifier = modifier
            .height(ButtonHeight),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = contentColor,
            disabledContainerColor = MaterialTheme.extendedColor.buttonColor,
            disabledContentColor = DisableButtonContentColor
        ),
        shape = shape,
        enabled = enabled,
        contentPadding = PaddingValues(horizontal = 5.dp)
    ) {
        if (loading){
            CircularProgressIndicator(
                modifier = Modifier.size(25.dp),
                color = contentColor,
                strokeWidth = 3.dp,
            )
            SpaceMedium()
        }

        CustomText(
            color = contentColor,
            text = text,
            fontWeight = FontWeight.SemiBold,
            fontSize = fontSize
        )

    }
}

@Preview
@Composable
private fun Preview() {
    CustomLoadingButton(
        text = "Salom",
        onClick = {},
        loading = true,
        enabled = false
    )

}