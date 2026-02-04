@file:OptIn(ExperimentalMaterial3Api::class)

package uz.tikoncha_parent.presentation.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.tikoncha_parent.ui.BorderColor
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.theme.extendedColor

@Stable
private fun String.onlyDigits(max: Int): String = filter(Char::isDigit).take(max)

@Composable
fun OtpInput(
    otpLength: Int = 6,
    otpText: String? = null,
    onBorderColor: Color = BorderColor,
    fontSize: TextUnit = NormalTextSize,
    autoFocus: Boolean = true,
    onOtpUpdate: (String) -> Unit,
) {
    val onOtpUpdateLatest by rememberUpdatedState(onOtpUpdate)

    val focusRequesters = remember(otpLength) { List(otpLength) { FocusRequester() } }
    val values = remember(otpLength) {
        mutableStateListOf(*Array(otpLength) { TextFieldValue("", TextRange(0)) })
    }

    var activeIndex by remember { mutableIntStateOf(0) }

    fun currentOtp(): String = values.joinToString("") { it.text }

    fun setCell(index: Int, text: String) {
        val t = text.onlyDigits(1)
        values[index] = TextFieldValue(t, TextRange(t.length)) // ✅ cursor always end
    }

    fun clearCell(index: Int) {
        values[index] = TextFieldValue("", TextRange(0))
    }

    fun requestFocus(index: Int) {
        val idx = index.coerceIn(0, otpLength - 1)
        activeIndex = idx
        focusRequesters[idx].requestFocus()
    }

    fun publish() {
        onOtpUpdateLatest(currentOtp())
    }

    // Autofocus
    LaunchedEffect(otpLength, autoFocus) {
        if (autoFocus) requestFocus(0)
    }

    // External sync
    LaunchedEffect(otpText, otpLength) {
        val incoming = otpText?.onlyDigits(otpLength).orEmpty()
        if (incoming == currentOtp()) return@LaunchedEffect

        for (i in 0 until otpLength) {
            val ch = incoming.getOrNull(i)?.toString().orEmpty()
            values[i] = TextFieldValue(ch, TextRange(ch.length))
        }
        publish()

        if (incoming.length < otpLength) requestFocus(incoming.length)
        else requestFocus(otpLength - 1)
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(otpLength) { index ->
            val value = values[index]

            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                    .border(1.dp, onBorderColor, RoundedCornerShape(12.dp))
                    .clickable { requestFocus(index) },
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = { newValue ->
                        val digits = newValue.text.onlyDigits(otpLength)

                        // ✅ Paste (bir nechta raqam)
                        if (digits.length > 1) {
                            var write = index
                            for (ch in digits) {
                                if (write >= otpLength) break
                                setCell(write, ch.toString())
                                write++
                            }
                            publish()

                            // first empty else last
                            val nextEmpty = (0 until otpLength).firstOrNull { values[it].text.isEmpty() }
                            requestFocus(nextEmpty ?: (otpLength - 1))
                            return@BasicTextField
                        }

                        // ✅ One digit typed => ALWAYS overwrite current cell
                        if (digits.length == 1) {
                            setCell(index, digits)
                            publish()

                            // move next (except last)
                            if (index < otpLength - 1) requestFocus(index + 1)
                            else requestFocus(index)
                            return@BasicTextField
                        }

                        // digits empty: IME delete case (some keyboards)
                        // bu yerda fokusni sakratmaymiz: faqat katakni tozalaymiz
                        if (values[index].text.isNotEmpty()) {
                            clearCell(index)
                            publish()
                            requestFocus(index)
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = if (index == otpLength - 1) ImeAction.Done else ImeAction.Next
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequesters[index])
                        .onFocusChanged { state ->
                            if (state.isFocused) {
                                activeIndex = index
                                // ✅ focus olganda cursor/selectionni doim oxiriga qo'yamiz
                                val t = values[index].text
                                if (values[index].selection != TextRange(t.length)) {
                                    values[index] = TextFieldValue(t, TextRange(t.length))
                                }
                            }
                        }
                        .onPreviewKeyEvent { event ->
                            if (event.type == KeyEventType.KeyDown && event.key == Key.Backspace) {
                                // ✅ Full bo‘lsa ham: delete + cursor movement barqaror
                                if (values[index].text.isNotEmpty()) {
                                    // shu katakni o‘chir, fokus shu yerda qolsin
                                    clearCell(index)
                                    publish()
                                    requestFocus(index)
                                } else {
                                    // bo‘sh bo‘lsa oldingiga borib o‘chir
                                    if (index > 0) {
                                        requestFocus(index - 1)
                                        if (values[index - 1].text.isNotEmpty()) {
                                            clearCell(index - 1)
                                            publish()
                                        }
                                    }
                                }
                                true
                            } else false
                        }
                        .background(Color.Transparent),
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = fontSize,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.extendedColor.textColor,
                    ),
                    cursorBrush = SolidColor(MaterialTheme.extendedColor.textColor)
                )
            }

            // ✅ Separator paramsiz
            if (index == 2) {
                Text(
                    "-",
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .align(Alignment.CenterVertically),
                    style = TextStyle(fontSize = 24.sp),
                    color = MaterialTheme.extendedColor.textColor
                )
            }
        }
    }
}
