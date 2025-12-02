package uz.tikoncha_parent.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.ui.BorderColor
import uz.tikoncha_parent.ui.DisableButtonColor
import uz.tikoncha_parent.ui.DisableButtonContentColor
import uz.tikoncha_parent.ui.HintTextColor
import uz.tikoncha_parent.ui.MyFontFamily
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.TextFieldCornerRadius
import uz.tikoncha_parent.ui.TextFieldInnerPadding
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.ui.CoinsCornerRadius
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun CoinGeneratorTextField(
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    value: TextFieldValue = TextFieldValue(""),
    onValueChange: (TextFieldValue) -> Unit,
    label: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    shadow: Boolean = true,
    containerColor: Color = MaterialTheme.extendedColor.cardColor,
    contentColor: Color = MaterialTheme.extendedColor.textColor,
    shape: RoundedCornerShape = RoundedCornerShape(CoinsCornerRadius),
    keyboardOptions: KeyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onClick:() -> Unit = {},
    hasBorder: Boolean = false,
    fonSize: TextUnit = NormalTextSize,
    isCenteredText: Boolean = false
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed: Boolean by interactionSource.collectIsPressedAsState()

    LaunchedEffect(isPressed) {
        if (isPressed){
            onClick()
        }
    }

    var backgroundColor = if (enabled)  containerColor else DisableButtonColor
    var borderColor  = if (enabled) MaterialTheme.extendedColor.borderColor else DisableButtonContentColor

    var newModifier = if (hasBorder){
        modifier
            .border(1.dp, borderColor, RoundedCornerShape(CoinsCornerRadius))
    }else{
        modifier
    }

    val columnModifier = if (shadow) {
        modifier
            .fillMaxWidth()
            .background(MaterialTheme.extendedColor.cardColor, RoundedCornerShape(TextFieldCornerRadius))
    } else {
        modifier
            .fillMaxWidth()
            .background(Color.Transparent, RoundedCornerShape(TextFieldCornerRadius))
    }

    Column(
        modifier = columnModifier

    ) {
        BasicTextField(
            cursorBrush = Brush.sweepGradient(listOf(contentColor, contentColor)),
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            readOnly = readOnly,
            interactionSource = interactionSource,
            enabled = enabled,
            modifier = newModifier
                .clip(shape)
                .fillMaxWidth()
                .background(backgroundColor),
            singleLine = singleLine,
            maxLines = if (singleLine) 1 else 5,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = contentColor,
                fontFamily = MyFontFamily(),
                fontSize = fonSize,
                textAlign = if (isCenteredText) {
                    TextAlign.Center
                } else {
                    TextAlign.Start
                }
            ),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,

            decorationBox = { innerTextField ->

                Row(
                    modifier = Modifier
                        .padding(horizontal = TextFieldInnerPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (leadingIcon != null) {
                        leadingIcon()
                        Spacer(Modifier.size(TextFieldInnerPadding))

                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        if (value.text.isEmpty()) {
                            CustomText(
                                text = label,
                                fontSize = fonSize,
                                color = HintTextColor
                            )
                        }
                        innerTextField()
                    }
                    if (trailingIcon != null) {
                        Spacer(Modifier.size(TextFieldInnerPadding))
                        trailingIcon()
                    }
                }
            }
        )
    }
}


@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        CoinGeneratorTextField(
            onValueChange = {},
            label = "Shopping",
            value = TextFieldValue("")
        )
    }
}