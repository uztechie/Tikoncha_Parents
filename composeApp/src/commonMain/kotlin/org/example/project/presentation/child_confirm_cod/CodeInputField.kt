package org.example.project.presentation.child_confirm_cod

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AssistChipDefaults.IconSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.example.project.platform.copyPlainText
import org.example.project.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.*


@Composable
fun CodeInputField(
    codeDigits: String,
    modifier: Modifier = Modifier,
    onCopiedToClip: (() -> Unit)? = null
) {
    val formatted = remember(codeDigits) { formatCode(codeDigits) }

    val clipboard = LocalClipboard.current          // Android’ga kerak bo‘ladi
    val scope     = rememberCoroutineScope()

    BasicTextField(
        value = formatted,
        onValueChange = {},
        readOnly = true,
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        ),
        modifier = modifier.fillMaxWidth(),
        decorationBox = { inner ->
            Box(
                modifier = Modifier
                    .background(Color.Transparent, RoundedCornerShape(TextFieldCornerRadius))
                    .border(1.dp, BorderColor, RoundedCornerShape(TextFieldCornerRadius))
                    .padding(horizontal = 14.dp, vertical = 20.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(Res.drawable.password_check),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable {
                                scope.launch {
                                    copyPlainText(clipboard, formatted)   // 🔑
                                    onCopiedToClip?.invoke()
                                }
                            }
                    )
                    Spacer(Modifier.width(8.dp))
                    inner()         // “123-456”
                }
            }
        }
    )
}

//@Composable
//fun CodeInputField(
//    value: TextFieldValue,
//    onValueChange: (TextFieldValue) -> Unit,
//    modifier: Modifier = Modifier,
//    fontSize: TextUnit = NormalTextSize,
//    fontWeight: FontWeight = FontWeight.Normal
//)
//{
//    OutlinedTextField(
//        value = value,
//        onValueChange = onValueChange,
//        modifier = modifier
//            .fillMaxWidth()
//            .border(1.dp, BorderColor, RoundedCornerShape(TextFieldCornerRadius)),
//        singleLine = true,
//        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//        textStyle = TextStyle(fontSize = fontSize, fontWeight = fontWeight),
//        colors = OutlinedTextFieldDefaults.colors(
//            focusedBorderColor = Color.Transparent,
//            unfocusedBorderColor = Color.Transparent,
//            disabledBorderColor = Color.Transparent,
//            errorBorderColor = Color.Transparent,
//            focusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
//            unfocusedPlaceholderColor = MaterialTheme.colorScheme.secondary
//        ),
//        leadingIcon = {
//            Icon(
//                painter = painterResource(Res.drawable.password_check),
//                contentDescription = null,
//                modifier = Modifier
//                    .padding(start = 15.dp)
//                    .size(TextFieldIconSize),
//                tint = PrimaryColor
//            )
//        },
//        placeholder = {
//            Text(stringResource(Res.string.kodni_kiriting))
//        },
//    )
//}

fun formatCode(raw: String): String {
    val digits = raw.filter { it.isDigit() }.take(6)
    return if (digits.length <= 3) digits else "${digits.take(3)}-${digits.drop(3)}"
}

@Composable
@Preview
fun Preview() {
    CodeInputField(
        codeDigits = "123-456"
    )
}


