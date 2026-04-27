package uz.tikoncha_parent.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.attach
import tikoncha_parents.composeapp.generated.resources.send
import tikoncha_parents.composeapp.generated.resources.xabar_yozish
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.ui.BackgroundColor
import uz.tikoncha_parent.ui.ChatTextFieldCornerRadius
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.MyFontFamily
import uz.tikoncha_parent.ui.NormalIconButtonSize
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.SpaceUltraSmall
import uz.tikoncha_parent.ui.TextFieldHeight
import uz.tikoncha_parent.ui.TextFieldInnerPadding
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun ChatTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    label: String = "",
    onSend: () -> Unit,
    onFileClick: () -> Unit,
    onValueChange: (String) -> Unit,
    textColor: Color = AppColors.text.primary,
    containerColor: Color = AppColors.bg.secondarySurface,
    placeholderColor: Color = AppColors.text.placeholder,
    focusRequester: FocusRequester = remember { FocusRequester() },
    visualTransformation: VisualTransformation = VisualTransformation.None,
    shape: RoundedCornerShape = RoundedCornerShape(ChatTextFieldCornerRadius),
    keyboardOptions: KeyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
) {
    var texFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = value,
                selection = TextRange(value.length)
            )
        )
    }

    LaunchedEffect(value) {
        if (texFieldValue.text != value) {
            texFieldValue = TextFieldValue(
                text = value,
                selection = TextRange(value.length)
            )
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Bottom
    ) {
        Row(
            modifier = modifier
                .background(containerColor, shape = shape)
                .weight(1f)
                .padding(end = TextFieldInnerPadding),
            verticalAlignment = Alignment.Bottom
        )
        {
            SpaceUltraSmall()
            Box(
                modifier = Modifier.height(TextFieldHeight),
                contentAlignment = Alignment.Center

            ) {
                IconButton(
                    onClick = onFileClick,
                    modifier = Modifier.size(NormalIconButtonSize)
                        .padding(5.dp),
                    enabled = false
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.attach),
                        contentDescription = "",
                        tint = AppColors.icon.secondary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            SpaceUltraSmall()

            BasicTextField(
                cursorBrush = Brush.sweepGradient(listOf(textColor, textColor)),
                value = texFieldValue,
                onValueChange = {
                    texFieldValue = it
                    onValueChange(texFieldValue.text)
                },
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .focusRequester(focusRequester),
                singleLine = false,
                maxLines = 5,
                textStyle = AppTypography.titleSmRegular.copy(color = textColor,),
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
                            Text(
                                text = label,
                                style = AppTypography.titleSmRegular,
                                color = placeholderColor
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
        SpaceSmall()

        Box(
            modifier = Modifier.height(TextFieldHeight),
            contentAlignment = Alignment.Center

        ) {

            FilledTonalIconButton(
                enabled = value.isNotBlank(),
                onClick = onSend,
                modifier = Modifier.size(TextFieldHeight),
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = PrimaryColor,
                    contentColor = Color.White,
                    disabledContainerColor = AppColors.bg.secondarySurface,
                    disabledContentColor = AppColors.icon.secondary
                )

            ) {
                Icon(
                    painter = painterResource(Res.drawable.send),
                    contentDescription = "Send",
                    modifier = Modifier.padding(TextFieldHeight * 0.25f)
                        .size(24.dp)
                )
            }
        }
    }

}


@Preview
@Composable
private fun Preview() {

    TikonchaParentTheme(
        ThemeMode.LIGHT
    ) {
        ChatTextField(
            value = "",
            onValueChange = {

            },

            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = TextFieldHeight, max = TextFieldHeight * 5),
            label = stringResource(Res.string.xabar_yozish),
            shape = RoundedCornerShape(20.dp),
            onSend = {},
            onFileClick = {},
        )
    }
}
