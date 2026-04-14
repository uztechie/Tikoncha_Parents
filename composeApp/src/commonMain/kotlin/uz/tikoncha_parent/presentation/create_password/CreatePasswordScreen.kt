package uz.saidburxon.newedu.presentation.feature.create_password

import uz.tikoncha_parent.platform.KeyboardAsState
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import uz.tikoncha_parent.presentation.add_child.AddChildScreen
import uz.tikoncha_parent.presentation.base.LogoHeader
import uz.tikoncha_parent.presentation.base.PasswordTextField
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.keyingisi
import tikoncha_parents.composeapp.generated.resources.parol_kiriting
import tikoncha_parents.composeapp.generated.resources.parolni_qayta_kiriting
import tikoncha_parents.composeapp.generated.resources.password_eye_close
import tikoncha_parents.composeapp.generated.resources.password_eye_open
import tikoncha_parents.composeapp.generated.resources.ro_yxatdan_o_tish_parol_yaratish
import tikoncha_parents.composeapp.generated.resources.xush_kelibsiz
import uz.tikoncha_parent.presentation.base.CustomButton
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.presentation.create_password.CreatePasswordViewmodel
import uz.tikoncha_parent.ui.theme.extendedColor


class CreatePasswordScreen : Screen {

    @Composable
    override fun Content() {

        val viewModel = koinScreenModel<CreatePasswordViewmodel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val navigator = LocalNavigator.current

        CreatePassword(
            navigator = navigator,
            state = state.value,
            event = event
        )
    }
}

@Composable
fun CreatePassword(
    navigator: Navigator?,
    state: CreatePasswordState,
    event: (CreatePasswordEvent) -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    var manageKeyboardVisibility by remember {
        mutableStateOf(false)
    }

    val isPhoneNumberValid = state.password.isNotBlank() &&
            state.confirmPassword.isNotBlank() &&
            state.password == state.confirmPassword

    val isKeyboardOpen = KeyboardAsState().value

    var showPasswordImage by remember {
        mutableStateOf(false)
    }

    val shouldHideSlider = isKeyboardOpen == !showPasswordImage

    LaunchedEffect(manageKeyboardVisibility) {
        if (manageKeyboardVisibility){
            keyboardController?.hide()
            manageKeyboardVisibility = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.extendedColor.backgroundColor)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {

            LogoHeader()

            var sliderImage = painterResource(Res.drawable.password_eye_close)
            var sliderRatioHeight = 0.5f
            if (isKeyboardOpen) {
                sliderRatioHeight = 0f
            } else {
                sliderRatioHeight = 0.5f
            }
            if (showPasswordImage) {
                sliderImage = painterResource(Res.drawable.password_eye_open)
                shouldHideSlider
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
                fontWeight = FontWeight.W600,
            )

            SpaceMedium()

            CustomText(
                text = stringResource(Res.string.ro_yxatdan_o_tish_parol_yaratish),
                fontSize = NormalTextSize,
                color = MaterialTheme.extendedColor.hintColor,
                fontWeight = FontWeight.W500
            )

            SpaceMedium()

            PasswordTextField(
                value = state.password,
                onValueChange = {
                    event(CreatePasswordEvent.OnCreatePassword(it))
                },
                onShowPassword = {
                    if (it && isKeyboardOpen){
                        manageKeyboardVisibility = true
                    }
                    showPasswordImage = it
                },
                placeholder = stringResource(Res.string.parol_kiriting),
                fontWeight = FontWeight.W500
            )
            Spacer(Modifier.size(10.dp))
            PasswordTextField(
                value = state.confirmPassword,
                onValueChange = {
                    event(CreatePasswordEvent.OnCreateConfirmPassword(it))
                },
                onShowPassword = {
                    if (it && isKeyboardOpen){
                        manageKeyboardVisibility = true
                    }
                    showPasswordImage = it
                },
                placeholder = stringResource(Res.string.parolni_qayta_kiriting),
                fontWeight = FontWeight.W500
            )
            Spacer(modifier = Modifier.weight(1f))
            CustomButton(
                onClick = {
                    event(CreatePasswordEvent.OnConfirmClicked)
                    navigator?.push(AddChildScreen())
                },
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .height(ButtonHeight),
                enabled = isPhoneNumberValid ,
                text = stringResource(Res.string.keyingisi)
            )
            SpaceLarge()
        }
    }
}

@Preview()
@Composable
private fun Prev() {
    CreatePassword(
        navigator = null,
        state = CreatePasswordState(),
        event = {}
    )
}