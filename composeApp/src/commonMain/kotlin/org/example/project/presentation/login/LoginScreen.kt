@file:Suppress("DEPRECATION")

package org.example.project.presentation.login

import org.example.project.platform.KeyboardAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.presentation.base.LogoHeader
import org.example.project.presentation.base.PhoneNumberInputField
import org.example.project.presentation.base.theme.BackgroundColor
import org.example.project.presentation.base.theme.ButtonHeight
import org.example.project.presentation.base.theme.HintTextColor
import org.example.project.presentation.base.theme.PrimaryColor
import org.example.project.presentation.base.theme.SmallTextSize
import org.example.project.presentation.base.theme.SpaceLarge
import org.example.project.presentation.base.theme.SpaceMedium
import org.example.project.presentation.otp.OtpScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.keyingisi
import tikoncha_parents.composeapp.generated.resources.ro_yxatdan_o_tish
import tikoncha_parents.composeapp.generated.resources.slider_normal
import tikoncha_parents.composeapp.generated.resources.slider_small
import tikoncha_parents.composeapp.generated.resources.xush_kelibsiz
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText

class LoginScreen :Screen {

    @Composable
    override fun Content() {

        val viewModel = koinViewModel<LoginViewmodel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val navigator = LocalNavigator.current

        Login(
            navigator = navigator,
            enterPhoneState = state.value,
            enterPhoneEvent = event
        )
    }
}




@Composable
fun Login(
    navigator: Navigator?,
    enterPhoneState: LoginState,
    enterPhoneEvent: (LoginEvent)-> Unit
) {

    val isPhoneNumberValid = enterPhoneState.number.length == 9 && enterPhoneState.number.filter { it.isDigit() }.length == 9
    val isKeyboardOpen = KeyboardAsState().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(BackgroundColor)
    ){

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {

            LogoHeader()

            val animatedRatioHeight by animateFloatAsState(
                targetValue = if(isKeyboardOpen) 2f else 3f,
                label = "AspectRatioAnimation"
            )

            val sliderImage = if (isKeyboardOpen) {
                painterResource(Res.drawable.slider_small)
            } else {
                painterResource(Res.drawable.slider_normal)
            }
            if (!isKeyboardOpen) {
                Image(
                    painter = sliderImage,
                    contentDescription = "",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .aspectRatio(4f / animatedRatioHeight),
                    contentScale = ContentScale.FillBounds
                )
            }
            SpaceLarge()
            CustomText(
                text = stringResource(Res.string.xush_kelibsiz),
                fontSize = 28.sp,
                fontWeight = FontWeight.W600,
            )
            SpaceMedium()
            CustomText(
                text = stringResource(Res.string.ro_yxatdan_o_tish),
                fontSize = 16.sp,
                color = HintTextColor,
                fontWeight = FontWeight.W500
            )
            SpaceMedium()
            PhoneNumberInputField(
                phoneNumber = enterPhoneState.number,
                onPhoneNumberChange = {
                    enterPhoneEvent(LoginEvent.OnNumberInsert(it))
                }
            )
            Spacer(modifier = Modifier.weight(1f))
            CustomButton(
                onClick = {
                    enterPhoneEvent(LoginEvent.OnConfirmClicked)
                    navigator?.push(OtpScreen())
                },
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .height(ButtonHeight),
                enabled = isPhoneNumberValid,
                text = stringResource(Res.string.keyingisi),
                fontWeight = FontWeight.W600,
                fontSize = 18.sp
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
                        textDecoration = TextDecoration.Underline,
                        fontSize = SmallTextSize,
                        fontWeight = FontWeight.W500
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

@Preview
private fun Preview() {
    LoginScreen()
}