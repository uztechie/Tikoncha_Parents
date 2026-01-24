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
import uz.tikoncha_parent.ui.ButtonCornerRadius
import uz.tikoncha_parent.ui.ButtonHeight
import uz.tikoncha_parent.ui.DisableButtonContentColor
import uz.tikoncha_parent.ui.OnPrimaryColor
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.SmallTextSize
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor


@Composable
fun CustomLoadingButton(
    text:String,
    fontSize:TextUnit = SmallTextSize,
    modifier: Modifier = Modifier,
    color: Color = PrimaryColor,
    textColor: Color = Color.White,
    enabled:Boolean = true,
    onClick:()->Unit,
    shape: Shape = RoundedCornerShape(ButtonCornerRadius),
    loading:Boolean = false
) {



    val isEnabled = enabled && !loading

    val disabledBg = MaterialTheme.extendedColor.disabledBgColor
    val disabledContent = MaterialTheme.extendedColor.disabledContentColor

    val loadingDisabledBg = color.copy(alpha = 0.55f)
    val loadingDisabledContent = Color.White.copy(alpha = 0.8f)

    val containerColor = when{
        isEnabled -> color
        loading -> loadingDisabledBg
        else -> disabledBg
    }

    val contentColor = when{
        isEnabled -> textColor
        loading -> loadingDisabledContent
        else -> disabledContent
    }


    Button(
        onClick = onClick,
        modifier = modifier
            .height(ButtonHeight),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContentColor = contentColor,
            disabledContainerColor = containerColor,
        ),
        shape = shape,
        enabled = enabled,
        contentPadding = PaddingValues(horizontal = 5.dp)
    ) {
        if (loading){
            CircularProgressIndicator(
                color = contentColor,
                modifier = Modifier.size(25.dp),
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
    TikonchaParentTheme(mode = ThemeMode.LIGHT){
        CustomLoadingButton(
            text = "Salom",
            onClick = {},
            loading = true,
            enabled = false
        )
    }


}