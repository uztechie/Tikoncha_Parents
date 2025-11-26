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
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.presentation.common.CoinGeneratorTextField
import uz.tikoncha_parent.ui.AppIconInnerPadding
import uz.tikoncha_parent.ui.BackgroundColor
import uz.tikoncha_parent.ui.BorderColor
import uz.tikoncha_parent.ui.CoinTextFieldWidth
import uz.tikoncha_parent.ui.NormalIconButtonSize
import uz.tikoncha_parent.ui.NormalLargeTextSize
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.TextFieldCornerRadius
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.add
import tikoncha_parents.composeapp.generated.resources.subtruct_icon
import uz.tikoncha_parent.presentation.base.tripleShadow
import uz.tikoncha_parent.ui.CoinsCornerRadius
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun CoinAmountTextField(
    coinsAmount: String,
    onAddCoinClicked: (Int) -> Unit,
    onSubtractButtonClicked: (Int) -> Unit,
    onValueChange: (String) -> Unit
) {

    var textState by remember { mutableStateOf(coinsAmount.toString()) }

    var textFieldValueState by remember {
        mutableStateOf(
            TextFieldValue(
                text = coinsAmount,
                selection = TextRange(coinsAmount.length) // Place cursor at the end
            )
        )
    }

    LaunchedEffect(coinsAmount) {
        textState = coinsAmount.toString()
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

    Row() {
        Box(
            modifier = Modifier
                .size(NormalIconButtonSize)
                .tripleShadow(
                    shape = RoundedCornerShape(TextFieldCornerRadius),
                )
                .background(MaterialTheme.extendedColor.cardColor, RoundedCornerShape(TextFieldCornerRadius))
                .clickable {

                    val currentAmount = coinsAmount.toIntOrNull()
                    if (currentAmount != null && currentAmount > 1) {
                        onSubtractButtonClicked(currentAmount - 1)
                    }
                    if (currentAmount == null){
                        onSubtractButtonClicked(0)
                    }

                }
                .padding(AppIconInnerPadding)
                .background(MaterialTheme.extendedColor.backgroundColor),
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
            onValueChange = { newTextFieldValue ->

                val newText = newTextFieldValue.text

                if (newText.isEmpty() || newText.all { it.isDigit() }) {

                    if (newText.isEmpty()){
                        onValueChange("0")
                        textFieldValueState = newTextFieldValue.copy(
                            text = "0", selection = TextRange(newText.length + 1)
                        )
                    }else{
                        if ((newText.length == 4 && newText.toInt() < 9999)) {
                            textFieldValueState = newTextFieldValue.copy(
                                selection = TextRange(newText.length)
                            )
                            onValueChange(newText.toInt().toString())
                        }
                    }
                }

            },
            value = textFieldValueState,
            hasBorder = true,
            fonSize = NormalLargeTextSize,
            contentColor = PrimaryColor,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isCenteredText = true
        )


        SpaceSmall()

        Box(
            modifier = Modifier
                .size(NormalIconButtonSize)
                .tripleShadow(
                    shape = RoundedCornerShape(TextFieldCornerRadius),
                )
                .background(MaterialTheme.extendedColor.cardColor, RoundedCornerShape(TextFieldCornerRadius))
                .clickable {
                    if (coinsAmount.isNotEmpty()){
                        if (coinsAmount.all { it.isDigit() }){
                            val current = coinsAmount.toInt()
                            val maxAvailable = 50
                            if (current < maxAvailable) {
                                onAddCoinClicked(current + 1)
                            }
                        }
                    }
                }
                .padding(AppIconInnerPadding),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                painter = painterResource(Res.drawable.add),
                tint = BackgroundColor,
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