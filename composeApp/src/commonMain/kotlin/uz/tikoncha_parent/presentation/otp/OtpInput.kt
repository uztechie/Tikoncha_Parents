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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.tikoncha_parent.ui.BorderColor
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

// Zero-width space — har bir katakda "placeholder" sifatida turadi
// iOS da bo'sh katakda backspace ni aniqlash uchun kerak
private const val ZWSP = "\u200B"

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

    // Har bir katakda TextFieldValue saqlaymiz
    // Initial qiymat: faqat ZWSP (ko'rinmas belgi)
    val values = remember(otpLength) {
        mutableStateListOf(*Array(otpLength) {
            TextFieldValue(ZWSP, TextRange(ZWSP.length))
        })
    }

    var activeIndex by remember { mutableIntStateOf(0) }

    // Har bir katakdan faqat raqamni ajratib olish
    fun digitAt(index: Int): String = values[index].text.replace(ZWSP, "")

    // Butun OTP kodni yig'ish
    fun currentOtp(): String = (0 until otpLength).joinToString("") { digitAt(it) }

    // Katakka raqam yozish (ZWSP saqlab qolamiz)
    fun setDigit(index: Int, digit: String) {
        val text = ZWSP + digit
        values[index] = TextFieldValue(text, TextRange(text.length))
    }

    // Katakni tozalash (faqat ZWSP qoladi)
    fun clearDigit(index: Int) {
        values[index] = TextFieldValue(ZWSP, TextRange(ZWSP.length))
    }

    fun requestFocus(index: Int) {
        val idx = index.coerceIn(0, otpLength - 1)
        activeIndex = idx
        try {
            focusRequesters[idx].requestFocus()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun publish() {
        onOtpUpdateLatest(currentOtp())
    }

    // Autofocus
    LaunchedEffect(otpLength, autoFocus) {
        if (autoFocus) requestFocus(0)
    }

    // Tashqi sync (otpText parametri)
    LaunchedEffect(otpText, otpLength) {
        val incoming = otpText.orEmpty().filter(Char::isDigit).take(otpLength)
        if (incoming == currentOtp()) return@LaunchedEffect

        for (i in 0 until otpLength) {
            val ch = incoming.getOrNull(i)?.toString().orEmpty()
            if (ch.isNotEmpty()) setDigit(i, ch) else clearDigit(i)
        }
        publish()

        requestFocus(if (incoming.length < otpLength) incoming.length else otpLength - 1)
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
                        val newText = newValue.text
                        val newDigits = newText.replace(ZWSP, "").filter(Char::isDigit)
                        val oldDigit = digitAt(index)

                        // CASE 1: Backspace holati
                        // Yangi text ZWSP dan qisqa yoki ZWSP yo'q — bu delete bosilgan
                        val isDelete = newText.isEmpty() ||
                                (newDigits.isEmpty() && oldDigit.isNotEmpty()) ||
                                (newText.length < ZWSP.length)

                        if (isDelete) {
                            if (oldDigit.isNotEmpty()) {
                                // Shu katakda raqam bor edi — o'chiramiz, fokus shu yerda qoladi
                                clearDigit(index)
                                publish()
                            } else {
                                // Shu katak bo'sh edi — oldingi katakka o'tib, uni o'chiramiz
                                if (index > 0) {
                                    clearDigit(index - 1)
                                    publish()
                                    requestFocus(index - 1)
                                }
                            }
                            return@BasicTextField
                        }

                        // CASE 2: Paste (bir nechta raqam)
                        if (newDigits.length > 1) {
                            var writeIndex = index
                            for (ch in newDigits) {
                                if (writeIndex >= otpLength) break
                                setDigit(writeIndex, ch.toString())
                                writeIndex++
                            }
                            publish()
                            val nextEmpty = (0 until otpLength).firstOrNull { digitAt(it).isEmpty() }
                            requestFocus(nextEmpty ?: (otpLength - 1))
                            return@BasicTextField
                        }

                        // CASE 3: Bitta raqam kiritildi
                        if (newDigits.length == 1) {
                            setDigit(index, newDigits)
                            publish()
                            if (index < otpLength - 1) requestFocus(index + 1)
                            return@BasicTextField
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = if (index == otpLength - 1) ImeAction.Done else ImeAction.Next,
                        autoCorrectEnabled = false
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequesters[index])
                        .onFocusChanged { state ->
                            if (state.isFocused) {
                                activeIndex = index
                                // Cursor doimo oxirida bo'lsin
                                val t = values[index].text
                                if (values[index].selection != TextRange(t.length)) {
                                    values[index] = TextFieldValue(t, TextRange(t.length))
                                }
                            }
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

            // Separator — 3-katakdan keyin
            if (index == 2) {
                Text(
                    text = "-",
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


@Composable
@Preview
private fun Preview() {
    TikonchaParentTheme(ThemeMode.DARK) {
        OtpInput(onOtpUpdate = {})
    }
}