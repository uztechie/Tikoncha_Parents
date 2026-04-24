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
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    label: String = "",
    shadow: Boolean = true,
    onClick:() -> Unit = {},
    enabled: Boolean = true,
    readOnly: Boolean = false,
    hasBorder: Boolean = false,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit,
    contentColor: Color = AppColors.text.primary,
    containerColor: Color = AppColors.field.page,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    style: TextStyle = AppTypography.titleLgMedium,
    placeholderColor: Color = AppColors.text.placeholder,
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
    val borderColor  = if (enabled) MaterialTheme.extendedColor.borderColor else DisableButtonContentColor

    val newModifier = if (hasBorder){
        modifier
            .border(1.dp, borderColor, shape = shape)
    }else{
        modifier
    }

    val columnModifier = if (shadow) {
        modifier
            .fillMaxWidth()
            .background(MaterialTheme.extendedColor.cardColor, shape = shape)
    } else {
        modifier
            .fillMaxWidth()
            .background(Color.Transparent, shape = shape)
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
            textStyle = style.copy(
                color = contentColor
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
                            .weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = label,
                                color = placeholderColor,
                                style = style
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
            value = "",
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .height(48.dp)
        )
    }
}