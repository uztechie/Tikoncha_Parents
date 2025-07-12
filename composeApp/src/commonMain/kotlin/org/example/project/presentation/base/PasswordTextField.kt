package org.example.project.presentation.base

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
import org.example.project.presentation.base.theme.NormalIconSize
import org.example.project.presentation.base.theme.NormalTextSize
import org.example.project.presentation.base.theme.PrimaryColor
import org.example.project.presentation.base.theme.TextColor
import org.example.project.presentation.base.theme.TextFieldCornerRadius
import org.example.project.presentation.base.theme.TextFieldHeight
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.eye
import tikoncha_parents.composeapp.generated.resources.eye_slash
import tikoncha_parents.composeapp.generated.resources.lock

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
                color = PrimaryColor,
                shape = RoundedCornerShape(TextFieldCornerRadius)
            )
            .height(TextFieldHeight),
        label = placeholder,
//        visualTransformation = PhoneNumberTransformation(),
        containerColor = Color.Transparent,
        contentColor = TextColor,
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
                    contentColor = TextColor
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