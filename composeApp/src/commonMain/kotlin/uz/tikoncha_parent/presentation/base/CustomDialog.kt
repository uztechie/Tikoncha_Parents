package uz.tikoncha_parent.presentation.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun CustomDialog(
    modifier: Modifier = Modifier,
    title: String,
    message: String,
    show: Boolean = true,
    painter: Painter = painterResource(Res.drawable.dialog_info),
    buttonText: String = "Ok",
    buttonText2: String = stringResource(Res.string.bekor_qilish),
    showCloseButton: Boolean = false,
    onButtonClick: () -> Unit,
    onDismiss: () -> Unit,
    isRow: Boolean = false
) {

    val dialogIcon = if (message == stringResource(Res.string.iltimos_internetga_ulang)) {
        painterResource(Res.drawable.dialog_internet)
    } else {
        painter
    }

    val dialogTitle = if (message == stringResource(Res.string.iltimos_internetga_ulang)) {
        stringResource(Res.string.aloqa_uzildi)
    } else {
        title
    }

    val dialogMessage = if (message == stringResource(Res.string.iltimos_internetga_ulang)) {
        stringResource(Res.string.internet_aloqa_uzildi)
    } else {
        message
    }

    if (show) {
        Dialog(
            onDismissRequest = onDismiss
        ) {


            Card(
                modifier = modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.extendedColor.backgroundColor
                ),
                shape = RoundedCornerShape(CardCornerRadius)
            )
            {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(ContainerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    SpaceLarge()
                    Image(
                        painter = dialogIcon,
                        contentDescription = "",
                        modifier = Modifier
                            .size(60.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    SpaceLarge()
                    CustomText(
                        text = dialogTitle,
                        fontSize = NormalTextSize,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    SpaceLarge()

                    CustomText(
                        text = dialogMessage,
                        fontSize = NormalTextSize,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )

                    SpaceLarge()

                    if (isRow) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (showCloseButton) {
                                CustomOutlinedButton(
                                    text = buttonText2,
                                    onClick = onDismiss,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(DialogButtonHeight)
                                )
                            }

                            CustomButton(
                                text = buttonText,
                                onClick = onButtonClick,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(DialogButtonHeight)
                            )
                        }

                    } else {
                        Column(modifier = Modifier.fillMaxWidth()) {

                            if (showCloseButton) {
                                CustomOutlinedButton(
                                    text = stringResource(Res.string.bekor_qilish),
                                    onClick = onDismiss,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(DialogButtonHeight)
                                )
                                SpaceSmall()
                            }

                            CustomButton(
                                text = buttonText,
                                onClick = onButtonClick,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(DialogButtonHeight)
                            )
                        }
                    }
                    SpaceLarge()

                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        CustomDialog(
            title = "Titel",
            message = "Message",
            show = true,
            buttonText = "Ok",
            showCloseButton = false,
            onButtonClick = {},
            onDismiss = {},
            isRow = true
        )
    }
}