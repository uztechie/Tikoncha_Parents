package uz.tikoncha_parent.presentation.profile.coins

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import uz.tikoncha_parent.presentation.common.CoinGeneratorTextField
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.add
import tikoncha_parents.composeapp.generated.resources.subtruct_icon
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun CoinAmountTextField(
    coinsAmount: String,
    onAddCoinClicked: (Int) -> Unit,
    onSubtractButtonClicked: (Int) -> Unit,
    onValueChange: (String) -> Unit,
    allowManualInput: Boolean = true
) {

    val maxAvailable = 100

    var textFieldValueState by remember {
        mutableStateOf(
            TextFieldValue(
                text = coinsAmount,
                selection = TextRange(coinsAmount.length) // Place cursor at the end
            )
        )
    }

    LaunchedEffect(coinsAmount) {
        // Only update if the text is different, to avoid resetting cursor during typing
        if (textFieldValueState.text != coinsAmount) {
            textFieldValueState = TextFieldValue(
                text = coinsAmount,
                selection = TextRange(coinsAmount.length) // Keep cursor at the end
            )
        }
    }

    Row {
        Box(
            modifier = Modifier
                .size(NormalIconButtonSize)
                .background(MaterialTheme.extendedColor.cardColor, RoundedCornerShape(TextFieldCornerRadius))
                .clip(RoundedCornerShape(TextFieldCornerRadius))
                .clickable {

                    val current = coinsAmount.toIntOrNull() ?: 0
                    if (current > 0) {
                        onSubtractButtonClicked(current - 1)
                    }
                }
                .padding(AppIconInnerPadding),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.subtruct_icon),
                tint = PrimaryColor,
                contentDescription = ""
            )
        }

        SpaceSmall()

        CoinGeneratorTextField(
            modifier = Modifier
                .width(CoinTextFieldWidth)
                .height(NormalIconButtonSize),
            singleLine = true,
            readOnly = !allowManualInput,
            onValueChange = { newTextFieldValue ->
                if (!allowManualInput) return@CoinGeneratorTextField

                val digest = newTextFieldValue.text.filter { it.isDigit() }

                val intValue = digest.toIntOrNull() ?: 0
                val clampedValue = intValue.coerceIn(0, maxAvailable)
                val finalText = if (digest.isEmpty()) "" else clampedValue.toString()
                textFieldValueState = newTextFieldValue.copy(
                    text = finalText,
                    selection = TextRange(finalText.length)
                )
                onValueChange(finalText.ifEmpty {"0"})
            },
            value = textFieldValueState,
            fonSize = NormalLargeTextSize,
            contentColor = PrimaryColor,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isCenteredText = true
        )


        SpaceSmall()

        Box(
            modifier = Modifier
                .size(NormalIconButtonSize)
                .background(MaterialTheme.extendedColor.cardColor, RoundedCornerShape(TextFieldCornerRadius))
                .clip(RoundedCornerShape(TextFieldCornerRadius))
                .clickable {
//                    if (coinsAmount.isNotEmpty()){
//                        if (coinsAmount.all { it.isDigit() }){
//                            val current = coinsAmount.toInt()
//                            val maxAvailable = 50
//                            if (current < maxAvailable) {
//                                onAddCoinClicked(current + 1)
//                            }
//                        }
//                    }
                    val current = coinsAmount.toIntOrNull() ?: 0
                    if (current < maxAvailable) {
                        onAddCoinClicked(current + 1)
                    }
                }
                .padding(AppIconInnerPadding),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                painter = painterResource(Res.drawable.add),
                tint = PrimaryColor,
                contentDescription = ""
            )

        }

    }

}

@Preview
@Composable
private fun PreviewCoinAmountTextField() {
    TikonchaParentTheme(
    ThemeMode.DARK
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
        ) {
            CoinAmountTextField(
                coinsAmount = "12",
                onAddCoinClicked = {},
                onSubtractButtonClicked = {},
                onValueChange = {}
            )
        }
    }
}