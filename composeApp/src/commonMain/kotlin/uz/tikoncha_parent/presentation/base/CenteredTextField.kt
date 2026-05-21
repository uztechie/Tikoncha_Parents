@file:OptIn(ExperimentalMaterial3Api::class)



import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextRange
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.ui.SpaceUltraSmall
import uz.tikoncha_parent.ui.TextFieldHeight
import uz.tikoncha_parent.ui.theme.AppTypography
import kotlin.math.max

/**
 * Reusable centered input built on BasicTextField (iOS cursor-jump friendly).
 *
 * - Uses TextFieldValue to preserve cursor/selection
 * - Provides Material-like container, padding, placeholder, error color support
 * - Can be reused for promo code, OTP, coupon, etc.
 */
@Composable
fun CenteredBasicInput(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    placeholder: String? = null,
    singleLine: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(14.dp),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    focusRequester: FocusRequester = remember { FocusRequester() },

    // Colors (Material3 friendly defaults)
    containerColor: Color = MaterialTheme.colorScheme.surface,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    cursorColor: Color = MaterialTheme.colorScheme.onSurface,
    errorContainerColor: Color = MaterialTheme.colorScheme.errorContainer,
    errorTextColor: Color = MaterialTheme.colorScheme.error,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 14.dp),

    // Optional slots
    leading: (@Composable (() -> Unit))? = null,
    trailing: (@Composable (() -> Unit))? = null,
) {
    val mergedTextStyle = textStyle.copy(color = if (isError) errorTextColor else textColor)

    Surface(
        modifier = modifier,
        shape = shape,
        color = if (isError) errorContainerColor else containerColor,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leading != null) {
                leading()
                Spacer(Modifier.width(10.dp))
            }

            // Centering trick: use Box with fillMaxWidth and center alignment,
            // but keep actual text input stable with TextFieldValue.
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    enabled = enabled,
                    readOnly = readOnly,
                    singleLine = singleLine,
                    textStyle = mergedTextStyle,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    visualTransformation = visualTransformation,
                    cursorBrush = SolidColor(if (isError) errorTextColor else cursorColor),
                    interactionSource = interactionSource,
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        // Placeholder (centered)
                        if (placeholder != null && value.text.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = mergedTextStyle.copy(
                                    color = mergedTextStyle.color.copy(alpha = 0.45f)
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                        innerTextField()
                    }
                )
            }

            if (trailing != null) {
                Spacer(Modifier.width(10.dp))
                trailing()
            }
        }
    }
}

/**
 * Convenience wrapper for your Promo Code use-case (String in state, but UI keeps TextFieldValue).
 *
 * - Keeps local TextFieldValue to preserve cursor.
 * - Syncs from external promoCode string only when it actually changes.
 * - Emits String to your event() as usual.
 */
@Composable
fun PromoCodeInput(
    promoCode: String,
    onPromoCodeChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    maxLength:Int = 20,
    errorMessage: String = "",
    placeholder: String = "PROMO",
    focusRequester: FocusRequester = remember { FocusRequester() },

    // Text & keyboard
    textStyle: TextStyle = AppTypography.titleLgMedium.copy(textAlign = TextAlign.Center),
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.Characters
    ),

    // 🎨 Colors (all configurable)
    containerColor: Color,
    textColor: Color,
    cursorColor: Color,
    placeholderColor: Color = textColor.copy(alpha = 0.45f),
    errorContainerColor: Color,
    errorTextColor: Color,

    shape: Shape = RoundedCornerShape(14.dp),
) {
    var tfv by remember { mutableStateOf(TextFieldValue(promoCode)) }

    // external -> internal sync (cursor-safe)
    LaunchedEffect(promoCode) {
        if (promoCode != tfv.text) {
            tfv = tfv.copy(
                text = promoCode,
                selection = TextRange(promoCode.length)
            )
        }
    }

    Column(modifier) {

        Surface(
            shape = shape,
            color = if (isError) errorContainerColor else containerColor,
            tonalElevation = 0.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .focusRequester(focusRequester),
                contentAlignment = Alignment.Center
            ) {

                BasicTextField(
                    value = tfv,
                    onValueChange = { newValue ->
                        // 1) limit
                        val limitedText =
                            if (newValue.text.length <= maxLength) newValue.text
                            else newValue.text.take(maxLength)

                        // 2) selection (cursor) ni ham limit ichida saqlash
                        val limitedSelection = TextRange(
                            start = newValue.selection.start.coerceIn(0, limitedText.length),
                            end = newValue.selection.end.coerceIn(0, limitedText.length),
                        )

                        val limitedTfv = newValue.copy(
                            text = limitedText,
                            selection = limitedSelection
                        )

                        tfv = limitedTfv
                        onPromoCodeChange(limitedText)
                    },
                    enabled = enabled,
                    singleLine = true,
                    textStyle = textStyle.copy(
                        color = if (isError) errorTextColor else textColor
                    ),
                    keyboardOptions = keyboardOptions,
                    cursorBrush = SolidColor(if (isError) errorTextColor else cursorColor),
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        if (tfv.text.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = textStyle.copy(
                                    color = placeholderColor
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                        innerTextField()
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PromoCodeInputPreview_Normal() {
    MaterialTheme {
        var text by remember { mutableStateOf("") }

        PromoCodeInput(
            promoCode = text,
            onPromoCodeChange = { text = it },

            containerColor = Color(0xFFF2F2F2),
            textColor = Color.Black,
            cursorColor = Color.Black,
            errorContainerColor = Color(0xFFFFE5E5),
            errorTextColor = Color.Red,
            modifier = Modifier
                .height(TextFieldHeight)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PromoCodeInputPreview_Error() {
    MaterialTheme {
        var text by remember { mutableStateOf("AB12") }

        PromoCodeInput(
            promoCode = text,
            onPromoCodeChange = { text = it },
            isError = true,
            errorMessage = "Promo code noto‘g‘ri",

            containerColor = Color(0xFFF2F2F2),
            textColor = Color.Black,
            cursorColor = Color.Black,
            errorContainerColor = Color(0xFFFFE5E5),
            errorTextColor = Color.Red,
            modifier = Modifier
                .height(TextFieldHeight)
        )
    }
}

