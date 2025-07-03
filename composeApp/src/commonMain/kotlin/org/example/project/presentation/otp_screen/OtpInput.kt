@file:OptIn(ExperimentalMaterial3Api::class)

package org.example.project.presentation.otp_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.presentation.base.theme.PrimaryColor
import org.example.project.presentation.base.theme.TextColor
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun OtpInput(
    otpLength: Int = 6,
    onBorderColor: Boolean = false,
    onOtpUpdate: (String) -> Unit
) {

    val focusRequesters = remember { List(otpLength) { FocusRequester() } }
    val inputValues = remember { mutableStateListOf(*Array(otpLength) { TextFieldValue("") }) }

//    var onBorderColor by remember { mutableStateOf(false) }

    // Autofocus first box
    LaunchedEffect(Unit) {
        focusRequesters.first().requestFocus()
    }


    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterHorizontally),
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(otpLength) { index ->
            val value = inputValues[index]

            Box(
                modifier = Modifier
                    .aspectRatio(1f/1f)
                    .weight(1f)
                    .border(1.dp, if (onBorderColor){Color.Red} else PrimaryColor, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ){
                BasicTextField(
                    value = value,
                    onValueChange = { newValue ->
                        var filtered = newValue.text.filter { it.isDigit() }.lastOrNull().toString()
                        if (filtered == "null"){
                            filtered = ""
                        }
                        if (filtered != inputValues[index].text) {
                            inputValues[index] = TextFieldValue(
                                text = filtered,
                                selection = TextRange(filtered.length) // 👈 cursor at end
                            )
                            if (filtered.isNotEmpty() && index < otpLength - 1) {
                                focusRequesters[index + 1].requestFocus()
                            }

                            val currentOtp = inputValues.joinToString("") { it.text }
                            onOtpUpdate(currentOtp)
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = if (index == otpLength - 1) ImeAction.Done else ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { defaultKeyboardAction(ImeAction.Done) },
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequesters[index])
                        .onKeyEvent { keyEvent ->

                            if (
                                keyEvent.key == Key.Backspace
                            ) {
                                println("MMM index=$index")
                                if (inputValues[index].text.isEmpty() && index > 0) {
                                    focusRequesters[index - 1].requestFocus()
                                }
                                true
                            } else false
                        }
                        .background(Color.Transparent),
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = TextColor,
                    )
                )
            }
            if (index == 2) {
                Text(
                    "-",
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .align(Alignment.CenterVertically),
                    style = TextStyle(fontSize = 24.sp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    OtpInput {  }
}