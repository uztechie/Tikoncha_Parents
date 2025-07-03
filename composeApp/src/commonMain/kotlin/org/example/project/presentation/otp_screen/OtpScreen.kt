package org.example.project.presentation.otp_screen

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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import com.yourpackage.utils.formatTwoDigits
import org.example.project.presentation.base.LogoHeader
import org.example.project.presentation.base.theme.BackgroundColor
import org.example.project.presentation.base.theme.ButtonHeight
import org.example.project.presentation.base.theme.HintTextColor
import org.example.project.presentation.base.theme.LargeTextSize
import org.example.project.presentation.base.theme.NormalTextSize
import org.example.project.presentation.base.theme.PrimaryColor
import org.example.project.presentation.base.theme.SpaceLarge
import org.example.project.presentation.base.theme.SpaceMedium
import org.example.project.presentation.base.theme.SpaceSmall
import org.example.project.presentation.base.theme.TextColor
import org.example.project.presentation.register.RegisterScreen
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.keyingisi
import tikoncha_parents.composeapp.generated.resources.kodni_qaytadan_yuborish
import tikoncha_parents.composeapp.generated.resources.ro_yxatdan_o_tish_uchun_kodni_kiriting
import tikoncha_parents.composeapp.generated.resources.sekund
import tikoncha_parents.composeapp.generated.resources.siz_noto_g_ri_kodni_kirittingiz
import tikoncha_parents.composeapp.generated.resources.xush_kelibsiz
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText


class OtpScreen : Screen {

    @Composable
    override fun Content() {

        val viewModel = koinViewModel<OtpViewmodel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val navigator = LocalNavigator.current

        Otp(
            navigator = navigator,
            state = state.value,
            event = event
        )
    }
}

@Composable
fun Otp(
    navigator: Navigator?,
    state: OtpState,
    event: (OtpEvent) -> Unit
) {
    val isOtpCodeValid = state.otpCode.length == 6 && state.otpCode.all { it.isDigit() }

    val formattedTime = formatTwoDigits(state.timeLife % 60)
    var previousFormattedTime by remember { mutableStateOf(formattedTime) }
    var finishedTime by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        event(OtpEvent.TimeStart)
    }

    LaunchedEffect(formattedTime) {
        if (formattedTime == "00" && previousFormattedTime != "00") {
            finishedTime = true
        }
        previousFormattedTime = formattedTime
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(horizontal = 20.dp)
            .imePadding()
    ) {
        LogoHeader()
        CustomText(
            text = stringResource(Res.string.xush_kelibsiz),
            fontSize = LargeTextSize,
            fontWeight = FontWeight.Bold,
        )
        SpaceMedium()
        CustomText(
            text = stringResource(Res.string.ro_yxatdan_o_tish_uchun_kodni_kiriting),
            fontSize = NormalTextSize,
            fontStyle = FontStyle.Normal,
            color = HintTextColor,
            fontWeight = FontWeight.W500,
        )
        SpaceLarge()
        OtpInput(
            otpLength = 6,
            onBorderColor = if (finishedTime) true else false,
            onOtpUpdate = {
                event(OtpEvent.OnOtpUpdate(it))
            }
        )
        SpaceMedium()
        if (finishedTime) {
            CustomText(
                text = stringResource(Res.string.siz_noto_g_ri_kodni_kirittingiz),
                fontSize = NormalTextSize,
                fontStyle = FontStyle.Normal,
                color = HintTextColor,
                fontWeight = FontWeight.W500,
            )
            CustomText(
                text = stringResource(Res.string.kodni_qaytadan_yuborish),
                fontSize = NormalTextSize,
                fontStyle = FontStyle.Normal,
                color = PrimaryColor,
                fontWeight = FontWeight.W500,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable { }
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
                    color = HintTextColor,
                    fontWeight = FontWeight.W500,
                )
            }
        }
        SpaceSmall()
        CustomText(
            text = stringResource(Res.string.sekund, formattedTime),
            fontSize = NormalTextSize,
            color = TextColor,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        CustomButton(
            onClick = {
                event(OtpEvent.OnConfirmClicked)
                navigator?.push(RegisterScreen())
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(ButtonHeight),
            enabled = isOtpCodeValid,
            text = stringResource(Res.string.keyingisi)
        )
        SpaceLarge()
    }
}


@Preview
@Composable
private fun Preview() {
    OtpScreen()
}