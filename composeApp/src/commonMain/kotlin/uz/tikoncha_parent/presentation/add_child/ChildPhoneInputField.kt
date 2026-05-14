package uz.tikoncha_parent.presentation.add_child

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.presentation.base.CustomTextField
import uz.tikoncha_parent.ui.NormalIconSize
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.TextFieldCornerRadius
import uz.tikoncha_parent.ui.TextFieldHeight
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.call
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun ChildPhoneInputField(
    modifier: Modifier = Modifier,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    isAccepted: Boolean = false
)
{

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.extendedColor.backgroundColor,RoundedCornerShape(TextFieldCornerRadius))
            .clip(RoundedCornerShape(TextFieldCornerRadius))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = if (isAccepted) MaterialTheme.extendedColor.primaryColor else Color.Transparent,
                    shape = RoundedCornerShape(TextFieldCornerRadius)
                )
                .background(Color.Transparent)
                .padding(horizontal = 20.dp, vertical = 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.call),
                contentDescription = "Phone Icon",
                tint = MaterialTheme.extendedColor.primaryColor,
                modifier = Modifier.padding(end = 8.dp).size(NormalIconSize)
            )
            Text(
                text = "+998",
                style = AppTypography.titleLgMedium,
                color = AppColors.text.tertiary,
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
                label = "00 000 00 00",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = modifier
                    .fillMaxWidth()
                    .height(TextFieldHeight),
                visualTransformation = PhoneNumberTransformation(),
                containerColor = MaterialTheme.extendedColor.backgroundColor,
                contentColor = if (isAccepted) PrimaryColor else MaterialTheme.extendedColor.onBackgroundColor
            )
        }
    }
}


class PhoneNumberTransformation : VisualTransformation
{
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

@Composable
@Preview
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        ChildPhoneInputField(
            phoneNumber = "",
            onPhoneNumberChange = {}
        )
    }
}
