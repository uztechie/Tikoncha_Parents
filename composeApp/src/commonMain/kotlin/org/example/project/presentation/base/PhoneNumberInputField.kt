package org.example.project.presentation.base

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.example.project.presentation.base.theme.NormalIconSize
import org.example.project.presentation.base.theme.NormalTextSize
import org.example.project.presentation.base.theme.PrimaryColor
import org.example.project.presentation.base.theme.TextColor
import org.example.project.presentation.base.theme.TextFieldCornerRadius
import org.example.project.presentation.base.theme.TextFieldHeight
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.call
import uz.saidburxon.newedu.presentation.base.CustomText

@Composable
fun PhoneNumberInputField(
    modifier: Modifier = Modifier,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit
) {

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = PrimaryColor,
                    shape = RoundedCornerShape(TextFieldCornerRadius)
                )
                .padding(horizontal = 20.dp, vertical = 0.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.call),
                contentDescription = "Phone Icon",
                tint = PrimaryColor,
                modifier = Modifier.padding(end = 8.dp).size(NormalIconSize)
            )
            CustomText(
                text = "+998",
                fontSize = NormalTextSize,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            CustomTextField(
                value = phoneNumber,
                onValueChange = { input ->
                    val digits = input.filter { it.isDigit() }
                    if (digits.length <= 9) {
                        onPhoneNumberChange(digits)
                    }

                },
                label = "Telefon raqam",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = modifier
                    .fillMaxWidth()
                    .height(TextFieldHeight),
                visualTransformation = PhoneNumberTransformation(),
                containerColor = Color.Transparent,
                contentColor = TextColor,

            )
        }
    }
}


class PhoneNumberTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.take(9)
        val formatted = buildString {
            for ((index, char) in trimmed.withIndex()) {
                append(char)
                if (index == 1 || index == 4 || index == 6) append(" ")
            }
        }

        val offsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var newOffset = offset
                if (offset > 1) newOffset += 1
                if (offset > 4) newOffset += 1
                if (offset > 6) newOffset += 1
                return newOffset.coerceAtMost(formatted.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                var newOffset = offset
                if (offset > 2) newOffset -= 1
                if (offset > 6) newOffset -= 1
                if (offset > 9) newOffset -= 1
                return newOffset.coerceAtMost(trimmed.length)
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetTranslator)
    }
}


@Preview
@Composable
private fun Preview() {
    PhoneNumberInputField(
        phoneNumber = ""
    ) { }
}
