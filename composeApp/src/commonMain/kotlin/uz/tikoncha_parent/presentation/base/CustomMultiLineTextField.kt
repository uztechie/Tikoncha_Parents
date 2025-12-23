package uz.tikoncha_parent.presentation.base



import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.ui.tooling.preview.Preview

import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun CustomMultiLineTextField(
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    value: String = "",
    onValueChange: (String) -> Unit,
    label: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    containerColor: Color = MaterialTheme.extendedColor.backgroundColor,
    contentColor: Color = MaterialTheme.extendedColor.textColor,
    shape: RoundedCornerShape = RoundedCornerShape(TextFieldCornerRadius),
    keyboardOptions: KeyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onClick:() -> Unit = {},
    hasBorder: Boolean = false,
    fonSize: TextUnit = NormalTextSize
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
            .border(1.dp, borderColor, RoundedCornerShape(TextFieldCornerRadius))
    }else{
        modifier
    }



    Row(
        modifier = newModifier
            .clip(shape)
            .background(backgroundColor)
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = TextFieldInnerPadding),
        verticalAlignment = Alignment.Bottom
    ) {

        if (leadingIcon != null){
            Box(
                modifier = Modifier
                    .height(TextFieldHeight),
                contentAlignment = Alignment.Center

            ){
                leadingIcon()
            }

            SpaceSmall()
        }


        BasicTextField(
            cursorBrush = Brush.sweepGradient(listOf(contentColor,contentColor)),
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            readOnly = readOnly,
            interactionSource = interactionSource,
            enabled = enabled,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            singleLine = singleLine,
            maxLines = if(singleLine) 1 else 5,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = contentColor,
                fontSize = fonSize,
                lineHeight = fonSize * 1.3
            ),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            decorationBox = { innerTextField ->



                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        CustomText(
                            text = label,
                            fontSize = fonSize,
                            color = MaterialTheme.extendedColor.hintColor
                        )
                    }
                    innerTextField()
                }
            }
        )
        if (trailingIcon != null){
            SpaceSmall()
            Box(
                modifier = Modifier
                    .height(TextFieldHeight),
                contentAlignment = Alignment.Center

            ){
                trailingIcon()
            }
        }
    }
}


@Preview
@Composable
private fun Preview() {

    var text by remember {
        mutableStateOf("")
    }

    CustomMultiLineTextField(
        value = text,
        onValueChange = {
            text = it
        },
        singleLine = false,
        modifier = Modifier
            .fillMaxWidth(),
        label = "Xabar yozish",
        containerColor = ChatMessageBackgroundColor,
        shape = RoundedCornerShape(20.dp),
    )
}