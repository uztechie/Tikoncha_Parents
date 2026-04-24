@file:Suppress("DEPRECATION")

package uz.tikoncha_parent.presentation.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
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
import uz.tikoncha_parent.ui.theme.rememberScreenSystemBars

class LoginScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<LoginViewModel>()
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
    val isKeyboardOpen = rememberIsKeyboardOpen()

    val systemBars = rememberScreenSystemBars(
        statusBarColor = AppColors.bg.secondary,
        navigationBarColor = AppColors.bg.secondary
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(AppColors.bg.secondary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(systemBars.modifier)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            LogoHeader()

            AnimatedVisibility(visible = !isKeyboardOpen) {
                Image(
                    painter = painterResource(Res.drawable.slider_normal),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4f / 3f),
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
                    if (state.isPhoneNumberValid) {
                        navigator?.push(OtpScreen(state.fullNumber))
                    }
                },
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .height(ButtonHeight),
                enabled = state.isPhoneNumberValid,
                text = stringResource(Res.string.keyingisi)
            )
            SpaceSmall()
        }
    }
}

@Composable
private fun rememberIsKeyboardOpen(): Boolean {
    val ime = WindowInsets.ime
    val density = LocalDensity.current
    val isOpen by remember {
        derivedStateOf { ime.getBottom(density) > 0 }
    }
    return isOpen
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