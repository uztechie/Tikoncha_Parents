package uz.tikoncha_parent.presentation.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import com.yourpackage.utils.formatTwoDigits
import org.jetbrains.compose.resources.painterResource
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.LoadingDialog
import uz.tikoncha_parent.presentation.base.LogoHeader
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.presentation.register.RegisterScreen
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.presentation.login.LoginViewmodel
import uz.tikoncha_parent.presentation.new_home.NewHomeScreen
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor


class OtpScreen(
    private val phoneNumber: String
) : Screen {

    @Composable
    override fun Content() {

        val viewModel = koinScreenModel<OtpViewmodel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        LaunchedEffect(Unit){
            event(OtpEvent.SetPhone(phoneNumber))
        }

        val logViewModel = koinScreenModel<LoginViewmodel>()
        val logState = logViewModel.state.collectAsStateWithLifecycle()
        val logEvent = logViewModel::onEvent

        val navigator = LocalNavigator.current

        OtpUi(
            navigator = navigator,
            state = state.value,
            event = event
        )
    }
}

@Composable
fun OtpUi(
    navigator: Navigator?,
    state: OtpState,
    event: (OtpEvent) -> Unit
) {
    val isOtpCodeValid = state.otpCode.length == 6 && state.otpCode.all { it.isDigit() }

    val formattedTime = formatTwoDigits(state.timeLife % 60)
    val finishedTime = state.timeLife <= 0

    val borderColor = when {
        finishedTime -> OtpErrorColor
        isOtpCodeValid -> PrimaryColor
        else -> MaterialTheme.extendedColor.borderColor
    }

    LaunchedEffect(Unit) {
        event(OtpEvent.TimeStart)
    }

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


    DisposableEffect(key1 = otpSuccess) {
        if (otpSuccess && state.responseState.data != null){


            if (state.responseState.data.user_info == null){
                navigator?.push(RegisterScreen())
            }
            else{
                navigator?.replaceAll(NewHomeScreen())
            }
        }

        onDispose {
            event(OtpEvent.Reset)
        }
    }




    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
            .padding(horizontal = 20.dp)
            .imePadding()
    ) {
        LogoHeader()
        CustomText(
            text = stringResource(Res.string.xush_kelibsiz),
            fontSize = LargeTextSize,
            fontWeight = FontWeight.W600,
        )
        SpaceMedium()
        CustomText(
            text = stringResource(Res.string.ro_yxatdan_o_tish_uchun_kodni_kiriting),
            fontSize = NormalTextSize,
            fontStyle = FontStyle.Normal,
            color = MaterialTheme.extendedColor.hintColor,
            fontWeight = FontWeight.W500,
        )
        SpaceLarge()
        OtpInput(
            otpLength = 6,
            onBorderColor = borderColor,
            onOtpUpdate = {
                event(OtpEvent.OnOtpUpdate(it))
            },
            fontWeight = FontWeight.W600
        )
        SpaceMedium()
        if (finishedTime) {
            CustomText(
                text = if (state.otpCode.isNotEmpty())stringResource(Res.string.siz_noto_g_ri_kodni_kirittingiz) else "",
                fontSize = NormalTextSize,
                fontStyle = FontStyle.Normal,
                color = MaterialTheme.extendedColor.hintColor,
                fontWeight = FontWeight.W500,
            )
            CustomText(
                text = stringResource(Res.string.kodni_qaytadan_yuborish),
                fontSize = NormalTextSize,
                fontStyle = FontStyle.Normal,
                color = MaterialTheme.extendedColor.primaryColor,
                fontWeight = FontWeight.W500,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable {
                        event(OtpEvent.ResendOtp)
                    }
            )

        } else {
            TextButton(
                onClick = {},
                modifier = Modifier.padding(0.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                CustomText(
                    text = stringResource(Res.string.kodni_qaytadan_yuborish),
                    fontSize = NormalTextSize,
                    fontStyle = FontStyle.Normal,
                    color = MaterialTheme.extendedColor.hintColor,
                    fontWeight = FontWeight.W500,
                )
            }
        }
        SpaceSmall()
        CustomText(
            text = stringResource(Res.string.sekund, formattedTime),
            fontSize = NormalTextSize,
            fontWeight = FontWeight.W600
        )
        Spacer(modifier = Modifier.weight(1f))
        CustomButton(
            onClick = {
                event(OtpEvent.OnConfirmClicked)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(ButtonHeight),
            enabled = isOtpCodeValid,
            text = stringResource(Res.string.keyingisi),
            fontSize = NormalTextSize,
            fontWeight = FontWeight.W600

        )
        SpaceLarge()
    }
}


@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.LIGHT
    ){
        OtpUi(
            navigator = null,
            state = OtpState(),
            event = {}
        )
    }
}