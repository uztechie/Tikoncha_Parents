package uz.tikoncha_parent.presentation.base

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun CustomDialog(
    modifier: Modifier = Modifier,
    title: String,
    message: String,
    show: Boolean = true,
    buttonText: String = "Ok",
    showCloseButton: Boolean = false,
    onButtonClick: () -> Unit,
    onDismiss: () -> Unit
) {


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
                shape = RoundedCornerShape(TextFieldCornerRadius)
            )
            {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(ContainerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = {
                            onDismiss()
                        },
                        modifier = Modifier
                            .size(CloseButtonSize)
                            .padding(CloseButtonInnerPadding)
                            .align(Alignment.End)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.close_circle),
                            contentDescription = ""
                        )
                    }
                    CustomText(
                        text = title,
                        fontSize = NormalTextSize,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    SpaceLarge()

                    CustomText(
                        text = message,
                        fontSize = NormalTextSize,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    SpaceLarge()

                    CustomButton(
                        text = buttonText,
                        onClick = onButtonClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(DialogButtonHeight)
                    )
                }
            }
        }
    }
}