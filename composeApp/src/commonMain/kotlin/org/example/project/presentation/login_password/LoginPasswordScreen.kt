package uz.saidburxon.newedu.presentation.feature.login_password

import org.example.project.platform.KeyboardAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.presentation.base.LogoHeader
import org.example.project.presentation.base.PasswordTextField
import org.example.project.presentation.base.theme.BackgroundColor
import org.example.project.presentation.base.theme.ButtonHeight
import org.example.project.presentation.base.theme.HintTextColor
import org.example.project.presentation.base.theme.LargeTextSize
import org.example.project.presentation.base.theme.NormalTextSize
import org.example.project.presentation.base.theme.SpaceLarge
import org.example.project.presentation.base.theme.SpaceMedium
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText


class LoginPasswordScreen :Screen {

    @Composable
    override fun Content() {

        val viewModel = koinViewModel<LoginPasswordViewmodel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val navigator = LocalNavigator.current

        LoginPassword(
            navigator = navigator,
            loginPasswordState = state.value,
            loginPasswordEvent = event
        )
    }
}
@Composable
fun LoginPassword(
    navigator: Navigator?,
    loginPasswordState: LoginPasswordState,
    loginPasswordEvent: (LoginPasswordEvent)-> Unit
) {

    val TAG = "LoginPasswordScreen"

    var manageKeyboardVisibility by remember {
        mutableStateOf(false)
    }

    val isPhoneNumberValid = loginPasswordState.password.length >= 6
    val isKeyboardOpen = KeyboardAsState().value

    val keyboardController = LocalSoftwareKeyboardController.current

    var showPasswordImage by remember {
        mutableStateOf(false)
    }

    val shouldHideSlider = isKeyboardOpen == !showPasswordImage

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

            var sliderImage = painterResource(Res.drawable.password_slider_normal)

            var sliderRatioHeight = 0.5f

            if (isKeyboardOpen){
                sliderRatioHeight = 0f
            }
            else{
                sliderRatioHeight = 0.5f
            }

            if (showPasswordImage){
                sliderImage = painterResource(Res.drawable.password_slider_opened)
                shouldHideSlider
            }

            LaunchedEffect(manageKeyboardVisibility) {
                if (manageKeyboardVisibility){
                    keyboardController?.hide()
                    manageKeyboardVisibility = false
                }
            }

            Image(
                painter = sliderImage,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth(sliderRatioHeight)
                    .align(Alignment.CenterHorizontally)
            )

            SpaceLarge()

            CustomText(
                text = stringResource(Res.string.xush_kelibsiz),
                fontSize = LargeTextSize,
                fontWeight = FontWeight.Bold,
            )

            SpaceMedium()

            CustomText(
                text = stringResource(Res.string.kirish_uchun_parolingizni_kiriting),
                fontSize = NormalTextSize,
                color = HintTextColor,
            )

            SpaceMedium()

           PasswordTextField(
               value = loginPasswordState.password,
               onValueChange = {
                   loginPasswordEvent(LoginPasswordEvent.OnLoginPassword(it))
               },
               onShowPassword = {
                   if (it && isKeyboardOpen){
                       manageKeyboardVisibility = true
                   }
                   showPasswordImage = it
               },
               placeholder = "Parolni kiriting",
               modifier = Modifier
           )

            Spacer(modifier = Modifier.weight(1f))

            CustomButton(
                onClick = {
                    loginPasswordEvent(LoginPasswordEvent.OnConfirmClicked)
                },
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .height(ButtonHeight),
                enabled = isPhoneNumberValid,
                text = stringResource(Res.string.keyingisi)
            )
            SpaceLarge()
        }
    }
}

@Preview()
@Composable
private fun Prev() {
    LoginPasswordScreen ()
}