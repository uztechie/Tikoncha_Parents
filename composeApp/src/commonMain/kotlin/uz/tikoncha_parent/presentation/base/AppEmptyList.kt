package uz.tikoncha_parent.presentation.base

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.DialogButtonHeight
import uz.tikoncha_parent.ui.LargeTextSize
import uz.tikoncha_parent.ui.SpaceLarge
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.UltraLargeTextSize
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun AppEmptyList(
    title: String = "Title",
    message: String = "Message",
    buttonText: String? = null,
    onActionButton: (() -> Unit)? = null,
    onBackClick: (() -> Unit)? = null,
    outlineText: String? = null,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(ContainerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (icon != null){
            icon()
            SpaceMedium()
        } else {
            SpaceMedium()
        }

        CustomText(
            text = title,
            textAlign = TextAlign.Center,
            fontSize = UltraLargeTextSize,
            color = MaterialTheme.extendedColor.hintColor
        )
        SpaceMedium()

        CustomText(
            text = message,
            textAlign = TextAlign.Center,
            fontSize = LargeTextSize,
            color = MaterialTheme.extendedColor.hintColor
        )
        SpaceLarge()

        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (outlineText != null && onBackClick != null){
                CustomOutlinedButton(
                    text = outlineText,
                    onClick = onBackClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .height(DialogButtonHeight)
                )
                SpaceSmall()
            }
            if (buttonText == null || onActionButton == null) return@Row
            CustomButton(
                text = buttonText,
                onClick = onActionButton,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .height(DialogButtonHeight)
            )
        }
    }
}

@Preview
@Composable
private fun Pre() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ) {
        AppEmptyList()
    }
}