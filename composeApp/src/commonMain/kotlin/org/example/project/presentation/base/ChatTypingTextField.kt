package org.example.project.presentation.base


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.VisualTransformation

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import org.example.project.presentation.base.theme.ChatMessageBackgroundColor
import org.example.project.presentation.base.theme.DisableButtonColor
import org.example.project.presentation.base.theme.DisableButtonContentColor
import org.example.project.presentation.base.theme.HintTextColor
import org.example.project.presentation.base.theme.NormalIconButtonSize
import org.example.project.presentation.base.theme.NormalTextSize
import org.example.project.presentation.base.theme.PrimaryColor
import org.example.project.presentation.base.theme.SpaceSmall
import org.example.project.presentation.base.theme.TextColor
import org.example.project.presentation.base.theme.TextFieldCornerRadius
import org.example.project.presentation.base.theme.TextFieldHeight
import org.example.project.presentation.base.theme.TextFieldInnerPadding
import org.example.project.ui.MyFontFamily
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.chat_add
import tikoncha_parents.composeapp.generated.resources.chat_send
import tikoncha_parents.composeapp.generated.resources.xabar_yozish

import uz.saidburxon.newedu.presentation.base.CustomText

@Composable
fun ChatTypingTextField(
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    value: String = "",
    onValueChange: (String) -> Unit,
    label: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    containerColor: Color = Color.White,
    contentColor: Color = TextColor,
    shape: RoundedCornerShape = RoundedCornerShape(TextFieldCornerRadius),
    keyboardOptions: KeyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
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
    var borderColor  = if (enabled) PrimaryColor else DisableButtonContentColor

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
                modifier = Modifier.height(TextFieldHeight),
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
                fontFamily = MyFontFamily(),
                fontSize = fonSize
            ),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            decorationBox = { innerTextField ->



                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        CustomText(
                            text = label,
                            fontSize = fonSize,
                            color = HintTextColor
                        )
                    }
                    innerTextField()
                }
            }


        )
        if (trailingIcon != null){
            SpaceSmall()
            Box(
                modifier = Modifier.height(TextFieldHeight),
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
    ChatTypingTextField(
        value = "",
        onValueChange = {

        },
        singleLine = false,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = TextFieldHeight, max = TextFieldHeight*5),
        label = stringResource(Res.string.xabar_yozish),
        containerColor = ChatMessageBackgroundColor,
        shape = RoundedCornerShape(20.dp),
        leadingIcon = {
            IconButton(
                onClick = {},
                modifier = Modifier.size(NormalIconButtonSize)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.chat_add),
                    contentDescription = "",
                    modifier = Modifier,
                    tint = PrimaryColor
                )
            }


        },
        trailingIcon = {
            IconButton(
                onClick = {},
                modifier = Modifier.size(NormalIconButtonSize)

            ) {
                Icon(
                    painter = painterResource(Res.drawable.chat_send),
                    contentDescription = "",
                    modifier = Modifier,
                    tint = PrimaryColor
                )
            }

        }
    )
}