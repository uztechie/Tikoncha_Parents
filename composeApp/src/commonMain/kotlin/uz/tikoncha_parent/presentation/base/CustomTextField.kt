package uz.tikoncha_parent.presentation.base

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.tikoncha_parent.ui.TextFieldCornerRadius
import uz.tikoncha_parent.ui.TextFieldInnerPadding
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    label: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    hasBorder: Boolean = false,
    singleLine: Boolean = true,
    onClick: () -> Unit = {},
    onValueChange: (String) -> Unit,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,

    // ===== Typography (theme-driven) =====
    style: TextStyle = AppTypography.titleSmMedium,

    // ===== Colors (theme-driven defaults) =====
    contentColor: Color = AppColors.text.primary,
    containerColor: Color = AppColors.field.page,
    placeholderColor: Color = AppColors.text.placeholder,

    // disabled
    disabledContainerColor: Color = AppColors.action.disabled,
    disabledContentColor: Color = AppColors.text.disabledTertiary,
    disabledBorderColor: Color = AppColors.border.disabled,

    // border
    borderColor: Color = AppColors.border.secondary,
    focusedBorderColor: Color = AppColors.border.accentEmphasis,
    errorBorderColor: Color = AppColors.button.accentDanger,
    errorTextColor: Color = AppColors.text.accentDanger,

    visualTransformation: VisualTransformation = VisualTransformation.None,
    shape: RoundedCornerShape = RoundedCornerShape(TextFieldCornerRadius),
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.Sentences
    ),
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isFocused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(isPressed) {
        if (isPressed) onClick()
    }

    // Resolved colors based on state
    val bgColor: Color = if (enabled) containerColor else disabledContainerColor

    val textColor: Color = when {
        !enabled -> disabledContentColor
        isError -> errorTextColor
        else -> contentColor
    }

    val resolvedPlaceholderColor: Color = when {
        !enabled -> disabledContentColor
        isError -> errorTextColor
        else -> placeholderColor
    }

    val resolvedBorderColor: Color = when {
        !enabled -> disabledBorderColor
        isError -> errorBorderColor
        isFocused -> focusedBorderColor
        else -> borderColor
    }

    // Apply border if hasBorder, error, or focused (focus ring)
    val borderModifier = if (hasBorder) {
        val resolvedBorderColor: Color = when {
            !enabled -> disabledBorderColor
            isError -> errorBorderColor
            isFocused -> focusedBorderColor
            else -> borderColor
        }
        Modifier.border(1.dp, resolvedBorderColor, shape)
    } else {
        Modifier
    }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        cursorBrush = Brush.sweepGradient(listOf(textColor, textColor)),
        readOnly = readOnly,
        interactionSource = interactionSource,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(bgColor, shape)
            .then(borderModifier),
        singleLine = singleLine,
        maxLines = if (singleLine) 1 else 5,
        textStyle = style.copy(color = textColor),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.padding(horizontal = TextFieldInnerPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) {
                    leadingIcon()
                    Spacer(Modifier.size(TextFieldInnerPadding))
                }
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = label,
                            color = resolvedPlaceholderColor,
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


/* =========================================================================
                                 PREVIEWS
   ========================================================================= */

@Composable
private fun PreviewContainer(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.bg.page)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = content
    )
}

@Composable
private fun PreviewLabel(text: String) {
    Text(
        text = text,
        style = AppTypography.bodyMdMedium,
        color = AppColors.text.tertiary
    )
}

/* ---------- DARK THEME ---------- */

@Preview
@Composable
private fun Preview_Empty_Dark() {
    TikonchaParentTheme(ThemeMode.DARK) {
        PreviewContainer {
            PreviewLabel("Empty (placeholder)")
            CustomTextField(
                value = "",
                label = "Shopping",
                onValueChange = {},
                modifier = Modifier.height(48.dp)
            )
        }
    }
}

@Preview
@Composable
private fun Preview_Filled_Dark() {
    TikonchaParentTheme(ThemeMode.DARK) {
        PreviewContainer {
            PreviewLabel("Filled")
            CustomTextField(
                value = "Akmal Karimov",
                label = "Full name",
                onValueChange = {},
                modifier = Modifier.height(48.dp)
            )
        }
    }
}

@Preview
@Composable
private fun Preview_LeadingIcon_Dark() {
    TikonchaParentTheme(ThemeMode.DARK) {
        PreviewContainer {
            PreviewLabel("Leading icon")
            CustomTextField(
                value = "",
                label = "Search…",
                onValueChange = {},
                modifier = Modifier.height(48.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = AppColors.icon.secondary
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun Preview_TrailingIcon_Dark() {
    TikonchaParentTheme(ThemeMode.DARK) {
        PreviewContainer {
            PreviewLabel("Trailing icon (password)")
            CustomTextField(
                value = "••••••••",
                label = "Password",
                onValueChange = {},
                modifier = Modifier.height(48.dp),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        tint = AppColors.icon.secondary
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun Preview_BothIcons_Dark() {
    TikonchaParentTheme(ThemeMode.DARK) {
        PreviewContainer {
            PreviewLabel("Both icons")
            CustomTextField(
                value = "tikoncha",
                label = "Username",
                onValueChange = {},
                modifier = Modifier.height(48.dp),
                leadingIcon = {
                    Icon(Icons.Default.Search, null, tint = AppColors.icon.secondary)
                },
                trailingIcon = {
                    Icon(Icons.Default.Visibility, null, tint = AppColors.icon.secondary)
                }
            )
        }
    }
}

@Preview
@Composable
private fun Preview_WithBorder_Dark() {
    TikonchaParentTheme(ThemeMode.DARK) {
        PreviewContainer {
            PreviewLabel("With border")
            CustomTextField(
                value = "",
                label = "Email",
                onValueChange = {},
                hasBorder = true,
                modifier = Modifier.height(48.dp)
            )
        }
    }
}

@Preview
@Composable
private fun Preview_Error_Dark() {
    TikonchaParentTheme(ThemeMode.DARK) {
        PreviewContainer {
            PreviewLabel("Error state")
            CustomTextField(
                value = "wrong@",
                label = "Email",
                onValueChange = {},
                isError = true,
                modifier = Modifier.height(48.dp)
            )
        }
    }
}

@Preview
@Composable
private fun Preview_Disabled_Dark() {
    TikonchaParentTheme(ThemeMode.DARK) {
        PreviewContainer {
            PreviewLabel("Disabled empty")
            CustomTextField(
                value = "",
                label = "Disabled field",
                onValueChange = {},
                enabled = false,
                modifier = Modifier.height(48.dp)
            )
            PreviewLabel("Disabled filled")
            CustomTextField(
                value = "Cannot edit",
                label = "Disabled field",
                onValueChange = {},
                enabled = false,
                modifier = Modifier.height(48.dp)
            )
        }
    }
}

@Preview
@Composable
private fun Preview_Multiline_Dark() {
    TikonchaParentTheme(ThemeMode.DARK) {
        PreviewContainer {
            PreviewLabel("Multi-line")
            CustomTextField(
                value = "Bu ko'p qatorli text field. Foydalanuvchi uzunroq matn yozishi mumkin.",
                label = "Description",
                onValueChange = {},
                singleLine = false,
                modifier = Modifier.heightIn(min = 80.dp)
            )
        }
    }
}

/* ---------- LIGHT THEME ---------- */

@Preview
@Composable
private fun Preview_AllStates_Light() {
    TikonchaParentTheme(ThemeMode.LIGHT) {
        PreviewContainer {
            PreviewLabel("Empty")
            CustomTextField(
                value = "", label = "Shopping",
                onValueChange = {}, modifier = Modifier.height(48.dp)
            )

            PreviewLabel("Filled")
            CustomTextField(
                value = "Akmal Karimov", label = "Full name",
                onValueChange = {}, modifier = Modifier.height(48.dp)
            )

            PreviewLabel("With border")
            CustomTextField(
                value = "", label = "Email",
                onValueChange = {}, hasBorder = true,
                modifier = Modifier.height(48.dp)
            )

            PreviewLabel("Error")
            CustomTextField(
                value = "wrong@", label = "Email",
                onValueChange = {}, isError = true,
                modifier = Modifier.height(48.dp)
            )

            PreviewLabel("Disabled")
            CustomTextField(
                value = "Cannot edit", label = "Disabled",
                onValueChange = {}, enabled = false,
                modifier = Modifier.height(48.dp)
            )

            PreviewLabel("With leading icon")
            CustomTextField(
                value = "", label = "Search…",
                onValueChange = {}, modifier = Modifier.height(48.dp),
                leadingIcon = {
                    Icon(Icons.Default.Search, null, tint = AppColors.icon.secondary)
                }
            )

            PreviewLabel("Multi-line")
            CustomTextField(
                value = "Bu ko'p qatorli text field.",
                label = "Description",
                onValueChange = {}, singleLine = false,
                modifier = Modifier.heightIn(min = 80.dp)
            )
        }
    }
}