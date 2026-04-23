package uz.tikoncha_parent.presentation.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import com.yourpackage.utils.formatTwoDigits
import uz.tikoncha_parent.presentation.base.LoadingDialog
import uz.tikoncha_parent.presentation.base.LogoHeader
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.presentation.register.RegisterScreen
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.presentation.base.CustomButton
import uz.tikoncha_parent.common.Util.maskPhone
import uz.tikoncha_parent.platform.openTelegram
import uz.tikoncha_parent.platform.openUrl
import uz.tikoncha_parent.presentation.base.CustomBottomDialog
import uz.tikoncha_parent.presentation.base.CustomText
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
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        LaunchedEffect(phoneNumber) {
            event(OtpEvent.SetPhone(phoneNumber))
        }

        val navigator = LocalNavigator.current

        OtpUi(
            navigator = navigator,
            state = state,
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
    val maskedPhone = remember(state.phoneNumber) { maskPhone(state.phoneNumber) }
    val formattedTime = formatTwoDigits(state.timeLife)

    val borderColor = when {
        state.hasInputError -> OtpErrorColor
        isOtpCodeValid -> PrimaryColor
        else -> MaterialTheme.extendedColor.borderColor
    }

    val showLoading = state.responseState is ResponseState.Loading || state.isSendingOtp
    val errorText = state.responseState.errorText()
    val isSuccess = state.responseState is ResponseState.Success
    var showErrorDialog by remember { mutableStateOf(false) }
    // Ekranga kirgan zahoti method selection dialog ochiladi (SMS / Telegram / boshqa raqam)
    var showTelegramDialog by rememberSaveable { mutableStateOf(true) }
    var showMethodDialog by rememberSaveable { mutableStateOf(false) }
    var hasMadeSelection by rememberSaveable { mutableStateOf(false) }
    var hasNavigated by rememberSaveable { mutableStateOf(false) }

    val textSubTitle = if (state.isTelegram == true) {
        stringResource(Res.string.telegram_kod_kiritish, maskedPhone)
    } else {
        stringResource(Res.string.otp_enter_code_with_phone, maskedPhone)
    }

    LaunchedEffect(errorText) {
        if (errorText.isNotEmpty()) {
            showErrorDialog = true
        }
    }

    LoadingDialog(show = showLoading)

    CustomBottomDialog(
        show = showErrorDialog,
        title = stringResource(Res.string.xatolik),
        message = errorText,
        confirmButtonText = if (!state.deleteAccountUrl.isNullOrBlank()) {
            stringResource(Res.string.sahifaga_otish)
        } else {
            stringResource(Res.string.ok)
        },
        showCancelButton = !state.deleteAccountUrl.isNullOrBlank(),
        onDismiss = {
            showErrorDialog = false
            event(OtpEvent.ResetError)
        },
        onConfirm = {
            showErrorDialog = false
            val url = state.deleteAccountUrl
            if (!url.isNullOrBlank()){
                openUrl(url)
            } else {
                event(OtpEvent.ResetError)
            }
        }
    )


    LaunchedEffect(isSuccess) {
        if (isSuccess && !hasNavigated) {
            val data = (state.responseState as? ResponseState.Success)?.data
            if (data != null) {
                hasNavigated = true
                if (data.user_info == null) {
                    navigator?.push(RegisterScreen())
                } else {
                    navigator?.replaceAll(NewHomeScreen())
                }
                event(OtpEvent.Reset)
            }
        }
    }

    TelegramOtpDialog(
        show = showTelegramDialog,
        onDismiss = {
            showTelegramDialog = false
            showMethodDialog = true
        },
        onConfirm = {
            showTelegramDialog = false
            hasMadeSelection = true
            event(OtpEvent.SetTelegram(true))
            openTelegram(state.phoneNumber)
            event(OtpEvent.TimeStart)
        }
    )

    OtpMethodSelectionDialog(
        show = showMethodDialog,
        onDismiss = {
            showMethodDialog = false
            if (!hasMadeSelection) {
                navigator?.pop()
            }
        },
        onConfirmSMS = {
            showMethodDialog = false
            hasMadeSelection = true
            event(OtpEvent.SetTelegram(false))
            event(OtpEvent.SendOtp)
        },
        onOtherNumber = {
            showMethodDialog = false
            hasMadeSelection = true
            navigator?.pop()
        },
        onConfirmTelegram = {
            showMethodDialog = false
            hasMadeSelection = true
            event(OtpEvent.SetTelegram(true))
            openTelegram(state.phoneNumber)
            event(OtpEvent.TimeStart)
        }
    )

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
            text = textSubTitle,
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
            }
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SpaceLarge()

            when {
                state.isRunning -> {
                    if (state.hasInputError) {
                        CustomText(
                            text = stringResource(Res.string.xato_kod_kiritdingiz),
                            color = OtpErrorColor
                        )
                        SpaceUltraSmall()
                        CustomText(text = stringResource(Res.string.sekund, formattedTime))
                    } else {
                        CustomText(text = stringResource(Res.string.sekund, formattedTime))
                    }
                }

                state.hasInputError -> {
                    CustomText(
                        text = stringResource(Res.string.xato_kod_kiritdingiz),
                        color = OtpErrorColor
                    )
                    SpaceUltraSmall()
                    CustomText(
                        text = stringResource(Res.string.kod_olish_usulini_ozgartirish),
                        color = MaterialTheme.extendedColor.primaryColor,
                        modifier = Modifier.clickable(
                            interactionSource = null,
                            indication = null
                        ) { showMethodDialog = true }
                    )
                }
                state.isTelegram != null && !state.isRunning -> {
                    CustomText(
                        text = stringResource(Res.string.kod_olish_usulini_ozgartirish),
                        color = MaterialTheme.extendedColor.primaryColor,
                        modifier = Modifier.clickable(
                            interactionSource = null,
                            indication = null
                        ) { showMethodDialog = true }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        CustomButton(
            onClick = {
                if (!showLoading && !hasNavigated) {
                    event(OtpEvent.OnConfirmClicked)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(ButtonHeight),
            enabled = isOtpCodeValid && !showLoading,
            text = stringResource(Res.string.keyingisi)
        )
        SpaceLarge()
    }
}


@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(ThemeMode.DARK) {
        OtpUi(
            navigator = null,
            state = OtpState(),
            event = {}
        )
    }
}