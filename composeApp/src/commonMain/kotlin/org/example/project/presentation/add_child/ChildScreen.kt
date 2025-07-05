@file:Suppress("DEPRECATION")

package org.example.project.presentation.add_child

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.presentation.base.CustomTextField
import org.example.project.presentation.base.LogoHeader
import org.example.project.presentation.base.theme.BackgroundColor
import org.example.project.presentation.base.theme.ButtonCornerRadius
import org.example.project.presentation.base.theme.ButtonHeight
import org.example.project.presentation.base.theme.ContainerCornerRadius
import org.example.project.presentation.base.theme.HintTextColor
import org.example.project.presentation.base.theme.MainBorderColor
import org.example.project.presentation.base.theme.NormalIconSize
import org.example.project.presentation.base.theme.NormalTextSize
import org.example.project.presentation.base.theme.OnPrimaryColor
import org.example.project.presentation.base.theme.PaddingCornerRadius
import org.example.project.presentation.base.theme.PrimaryColor
import org.example.project.presentation.base.theme.SmallTextSize
import org.example.project.presentation.base.theme.SpaceLarge
import org.example.project.presentation.base.theme.SpaceMedium
import org.example.project.presentation.base.theme.TextColor
import org.example.project.presentation.base.theme.TextFieldCornerRadius
import org.example.project.presentation.base.theme.TextFieldHeight
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.saidburxon.newedu.presentation.feature.main.MainScreen

class ChildScreen : Screen {

    @Composable
    override fun Content() {

        val viewModel = koinViewModel<ChildViewmodel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val navigator = LocalNavigator.current

        Child(
            navigator = navigator,
            childState = state.value,
            childEvent = event
        )
    }
}


@Composable
fun Child(
    navigator: Navigator?,
    childState: ChildState,
    childEvent: (ChildEvent) -> Unit
) {

    var children by remember { mutableStateOf(listOf(ChildState())) }


    val areAllPhoneNumbersValid = children.all { child ->
        child.number.length == 9 && child.number.all { it.isDigit() }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(BackgroundColor)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {

            LogoHeader()

            CustomText(
                text = stringResource(Res.string.xush_kelibsiz),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            )

            SpaceMedium()

            CustomText(
                text = stringResource(Res.string.farzandlaringiz),
                fontSize = 16.sp,
                color = HintTextColor,
            )

            SpaceMedium()

// Barcha farzand telefon raqamlarini ko‘rsatish
            children.forEachIndexed { index, childState ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(ContainerCornerRadius))
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MainBorderColor)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(PaddingCornerRadius)
                    ) {
                        CustomText(
                            text = stringResource(Res.string.farzandingiz_telefon_raqamini_kiriting),
                            fontSize = 16.sp,
                            color = HintTextColor,
                        )

                        SpaceMedium()

                        ChildPhoneInputField(
                            phoneNumber = childState.number,
                            onPhoneNumberChange = { newNumber ->
                                children = children.toMutableList().also {
                                    it[index] = it[index].copy(number = newNumber)
                                }
                            }
                        )
                    }
                }
            }

            SpaceLarge()

// ➕ Farzand qo‘shish tugmasi
            TextButton(
                onClick = {
                    children = children + ChildState()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, PrimaryColor, RoundedCornerShape(ButtonCornerRadius))
                    .height(ButtonHeight),
            ) {
                Row {
                    Text(
                        text = stringResource(Res.string.farzand_qo_shish),
                        fontSize = NormalTextSize,
                        color = PrimaryColor
                    )

                    SpaceMedium()

                    Icon(
                        painter = painterResource(Res.drawable.add_square),
                        contentDescription = "",
                        tint = PrimaryColor
                    )
                }
            }


            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                onClick = { },
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth()
                    .border(1.dp, PrimaryColor, RoundedCornerShape(ButtonCornerRadius))
                    .height(ButtonHeight),
            ) {
                Row {

                    Text(
                        text = stringResource(Res.string.o_tkazib_yuborish),
                        fontSize = NormalTextSize,
                        color = PrimaryColor
                    )
                }
            }

            CustomButton(
                onClick = {
                    childEvent(ChildEvent.OnConfirmClicked)
                    navigator?.push(MainScreen())
                },
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth()
                    .height(ButtonHeight),
                enabled = areAllPhoneNumbersValid,
                text = stringResource(Res.string.keyingisi)
            )
            SpaceLarge()
            val annotatedText = buildAnnotatedString {

                append("Авторизуясь, вы принимаете наши Условия использования и ")
                pushStringAnnotation(
                    tag = "POLICY",
                    annotation = "policy"
                )
                withStyle(
                    style = SpanStyle(
                        color = PrimaryColor,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append("Политику конфиденциальности.")
                }
                pop()
            }

            ClickableText(
                text = annotatedText,
                style = TextStyle(fontSize = SmallTextSize, color = Color.Black),
                onClick = { offset ->
                    annotatedText.getStringAnnotations(tag = "POLICY", start = offset, end = offset)
                        .firstOrNull()?.let {
                        }
                }
            )
            SpaceLarge()
        }
    }
}

@Composable
fun ChildPhoneInputField(
    modifier: Modifier = Modifier,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit
) {

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(TextFieldCornerRadius))
                .background(OnPrimaryColor)
                .border(
                    width = 1.dp,
                    color = OnPrimaryColor,
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
                label = "00 000 00 00",
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
private fun Preview() {
    ChildScreen()
}