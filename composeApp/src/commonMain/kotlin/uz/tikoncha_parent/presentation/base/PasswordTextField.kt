package uz.tikoncha_parent.presentation.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    placeholder: String,
    value: String,
    fontWeight: FontWeight = FontWeight.Normal,
    fonSize: TextUnit = NormalTextSize,
    onShowPassword: (Boolean) -> Unit,
    onValueChange:(String)-> Unit
) {

    var showPassword by remember {
        mutableStateOf(false)
    }

    CustomTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.extendedColor.borderColor,
                shape = RoundedCornerShape(TextFieldCornerRadius)
            )
            .height(TextFieldHeight),
        label = placeholder,
//        visualTransformation = PhoneNumberTransformation(),
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.extendedColor.onBackgroundColor,
        leadingIcon = {
           Image(
               painter = painterResource(Res.drawable.lock),
               contentDescription = "lock",
               modifier = Modifier
                   .padding(start = 5.dp)
                   .size(NormalIconSize)
           )
        },
        trailingIcon = {
            val icon =
                if (showPassword) {
                    painterResource(Res.drawable.eye)
                }
                else {
                    painterResource(Res.drawable.eye_slash)
                }
            IconButton(
                onClick = {
                    showPassword = !showPassword
                    onShowPassword(showPassword)

                },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.extendedColor.onBackgroundColor
                )
            ) {
                Icon(
                    painter = icon,
                    contentDescription = "Password",
                    modifier = Modifier.size(NormalIconSize)
                )
            }
        },
        singleLine = true,
        visualTransformation =
        if (showPassword){
            VisualTransformation.None
        }
        else{
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        fontWeight = fontWeight,
        fonSize = fonSize
    )
}

@Preview
@Composable
private fun Preview() {
    PasswordTextField(
        placeholder = "Phone",
        value = "0123",
        onShowPassword = {},
        onValueChange = {}
    )
}