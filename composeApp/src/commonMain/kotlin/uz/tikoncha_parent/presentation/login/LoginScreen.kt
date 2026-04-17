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
import androidx.compose.runtime.getValue
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
import uz.tikoncha_parent.presentation.base.LogoHeader
import uz.tikoncha_parent.presentation.base.PhoneNumberInputField
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.presentation.otp.OtpScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.presentation.base.CustomButton
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

class LoginScreen : Screen {

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
    event: (LoginEvent) -> Unit
) {

    val isPhoneNumberValid = state.number.length == 9 && state.number.all { it.isDigit() }
    val isKeyboardOpen = KeyboardAsState().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.bg.page)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            LogoHeader()

            val animatedRatioHeight by animateFloatAsState(
                targetValue = if (isKeyboardOpen) 2f else 3f,
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
                    if (isPhoneNumberValid) {
                        event(LoginEvent.OnConfirmClicked)
                        navigator?.push(OtpScreen(phoneNumber = state.fullNumber))
                    }
                },
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .height(ButtonHeight),
                enabled = isPhoneNumberValid,
                text = stringResource(Res.string.keyingisi)
            )
            SpaceSmall()
        }
    }
}

@Composable
@Preview
private fun Preview() {
    TikonchaParentTheme(ThemeMode.DARK) {
        LoginUi(
            navigator = null,
            state = LoginState(),
            event = {}
        )
    }
}