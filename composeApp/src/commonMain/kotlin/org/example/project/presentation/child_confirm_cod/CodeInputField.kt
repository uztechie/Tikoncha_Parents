package org.example.project.presentation.child_confirm_cod

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.ui.CardCornerRadius
import org.example.project.ui.ContainerCornerRadius
import org.example.project.ui.DisableTextColor
import org.example.project.ui.HintTextColor
import org.example.project.ui.PrimaryColor
import org.example.project.ui.TextFieldIconSize
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.kodni_kiriting
import tikoncha_parents.composeapp.generated.resources.kodni_qaytadan_yuborish
import tikoncha_parents.composeapp.generated.resources.password_check

@Composable
fun CodeInputField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, PrimaryColor, RoundedCornerShape(ContainerCornerRadius)),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = TextStyle(fontSize = 18.sp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            errorBorderColor = Color.Transparent,
            focusedPlaceholderColor = HintTextColor,
            unfocusedPlaceholderColor = HintTextColor
        ),
        leadingIcon = {
            Icon(
                painter = painterResource(Res.drawable.password_check),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 15.dp)
                    .size(TextFieldIconSize),
                tint = PrimaryColor
            )
        },
        placeholder = {
            Text(stringResource(Res.string.kodni_kiriting))
        }
    )
}

fun formatCode(raw: String): String {
    val digits = raw.filter { it.isDigit() }.take(6)
    return if (digits.length <= 3) digits else "${digits.take(3)}-${digits.drop(3)}"
}


