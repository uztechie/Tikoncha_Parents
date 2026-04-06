@file:Suppress("DEPRECATION")

package uz.tikoncha_parent.presentation.login

import uz.tikoncha_parent.platform.KeyboardAsState
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.LoadingDialog
import uz.tikoncha_parent.presentation.base.LogoHeader
import uz.tikoncha_parent.presentation.base.PhoneNumberInputField
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.presentation.otp.OtpScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

class LoginScreen :Screen {

    @Composable
    override fun Content() {

        val viewModel = koinScreenModel<LoginViewmodel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val navigator = LocalNavigator.current

        LoginUi(
            navigator = navigator,
            state = state,
            event = event
        )
    }
}




@Composable
fun LoginUi(
    navigator: Navigator?,
    state: LoginState,
    event: (LoginEvent)-> Unit
) {

    val isPhoneNumberValid = state.number.length == 9 && state.number.filter { it.isDigit() }.length == 9
    val isKeyboardOpen = KeyboardAsState().value

    var showDialog by remember {
        mutableStateOf(false)
    }

    val otpLoading = state.responseState is ResponseState.Loading
    val otpErrorText = state.responseState.errorText()
    val otpSuccess = state.responseState is ResponseState.Success


    LaunchedEffect(otpErrorText) {
        showDialog = otpErrorText.isNotEmpty()
    }

    LoadingDialog(show = otpLoading)
    CustomDialog(
        painter = painterResource(Res.drawable.dialog_failed),
        show = showDialog,
        title = stringResource(Res.string.xatolik),
        message = otpErrorText,
        buttonText = stringResource(Res.string.ok),
        onDismiss = {
            showDialog = false
        },
        onButtonClick = {
            showDialog = false
        }
    )

    LaunchedEffect(otpSuccess) {
        if (otpSuccess){
            event(LoginEvent.Reset)
            navigator?.push(OtpScreen(phoneNumber = state.fullNumber))
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ){

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.bg.page)
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
                color = MaterialTheme.extendedColor.hintColor,
                fontWeight = FontWeight.W500,
            )
            SpaceMedium()
            PhoneNumberInputField(
                phoneNumber = state.number,
                onPhoneNumberChange = {
                    event(LoginEvent.OnNumberInsert(it))
                }
            )
            Spacer(modifier = Modifier.weight(1f))
            CustomButton(
                onClick = {
                    event(LoginEvent.OnConfirmClicked)
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
            SpaceSmall()

//            val annotatedText = buildAnnotatedString {
//
//                append("Авторизуясь, вы принимаете наши Условия использования и ")
//                pushStringAnnotation(
//                    tag = "POLICY",
//                    annotation = "policy",
//                )
//                withStyle(
//                    style = SpanStyle(
//                        color = MaterialTheme.extendedColor.primaryColor,
//                        textDecoration = TextDecoration.Underline,
//                        fontSize = NormalTextSize,
//                        fontWeight = FontWeight.W500
//                    )
//                ) {
//                    append("Политику конфиденциальности.")
//                }
//                pop()
//            }
//            ClickableText(
//                text = annotatedText,
//                style = TextStyle(fontSize = NormalTextSize, color = MaterialTheme.extendedColor.onBackgroundColor),
//                onClick = { offset ->
//                    annotatedText.getStringAnnotations(tag = "POLICY", start = offset, end = offset)
//                        .firstOrNull()?.let {
//                        }
//                }
//            )
//            SpaceSmall()
        }
    }
}

@Composable
@Preview
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        LoginUi(
            navigator = null,
            state = LoginState(),
            event = {}
        )
    }
}