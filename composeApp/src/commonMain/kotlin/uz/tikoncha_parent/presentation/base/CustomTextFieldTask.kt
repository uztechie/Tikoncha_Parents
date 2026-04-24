package uz.tikoncha_parent.presentation.base

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun CustomTextFieldTask(
    modifier: Modifier = Modifier,
    value: String = "",
    label: String = "",
    enabled: Boolean = true,
    minLine: Boolean = true,
    onClick:() -> Unit = {},
    readOnly: Boolean = false,
    hasBorder: Boolean = false,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit,
    contentColor: Color = AppColors.text.primary,
    containerColor: Color = AppColors.field.page,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    textStyle: TextStyle = AppTypography.titleSmMedium,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    shape: RoundedCornerShape = RoundedCornerShape(TextFieldCornerRadius),
    keyboardOptions: KeyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed: Boolean by interactionSource.collectIsPressedAsState()

    LaunchedEffect(isPressed) {
        if (isPressed){
            onClick()
        }
    }

    val backgroundColor = if (enabled)  containerColor else DisableButtonColor
    val borderColor  = if (enabled) PrimaryColor else DisableButtonContentColor

    val newModifier = if (hasBorder){
        modifier
            .border(1.dp, borderColor, RoundedCornerShape(TextFieldCornerRadius))
    }else{
        modifier
    }


    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.extendedColor.cardColor, RoundedCornerShape(TextFieldCornerRadius))
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
            maxLines = if (singleLine) 1 else 8,
            minLines = if (minLine) 5 else 1,
            textStyle = textStyle.copy(
                color = contentColor
            ),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .padding(horizontal = TextFieldInnerPadding, vertical = 10.dp),
                ) {
                    if (leadingIcon != null) {
                        leadingIcon()
                        Spacer(Modifier.size(TextFieldInnerPadding))
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = label,
                                style = textStyle,
                                color = AppColors.text.placeholder
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
        CustomTextField(
            onValueChange = {},
            label = "Shopping",
            value = ""
        )
    }
}