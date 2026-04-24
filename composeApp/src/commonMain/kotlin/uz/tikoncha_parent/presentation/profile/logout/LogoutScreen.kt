package uz.tikoncha_parent.presentation.profile.logout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.*
import org.jetbrains.compose.resources.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.presentation.base.CustomButton
import uz.tikoncha_parent.AppRestartBus
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.presentation.base.*
import uz.tikoncha_parent.presentation.login.LoginScreen
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.theme.*

class LogoutScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        LogoutUi(
            navigator = navigator
        )
    }
}

@Composable
fun LogoutUi(
    navigator: Navigator?
) {
    var logout by remember {  mutableStateOf(false)}

    CustomDialog(
        title = stringResource(Res.string.chiqishni_xohlaysizmi),
        message = stringResource(Res.string.chiqishni_tasdiqlang),
        buttonText = stringResource(Res.string.tasdiqlash),
        show = logout,
        showCloseButton = true,
        onDismiss = { logout = false },
        onButtonClick = {
            AppSettings.clearSession()
            navigator?.replaceAll(LoginScreen())
            AppRestartBus.restart()
        }
    )

    val systemBars = rememberScreenSystemBars(
        statusBarColor = AppColors.bg.secondary,
        navigationBarColor = AppColors.bg.secondary
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(systemBars.modifier)
            .background(AppColors.bg.secondary)
    ) {
        CustomHeader(
            title = stringResource(Res.string.akkauntdan_chiqish),
            showBackButton = true,
            onBackClick = {
                navigator?.pop()
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ContainerPadding)
        ) {
            SpaceMedium()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(CardCornerRadius))
                    .border(1.dp, OtpErrorColor.copy(0.5f), RoundedCornerShape(CardCornerRadius))
                    .background(MaterialTheme.extendedColor.cardColor)
                    .padding(ContainerPadding)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.warning_1),
                    contentDescription = null,
                    tint = OtpErrorColor,
                    modifier = Modifier
                        .size(NormalIconButtonSize)
                        .align(Alignment.CenterVertically),
                )
                SpaceSmall()

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CustomText(
                        text = stringResource(Res.string.akkauntdan_chiqmoqchimisiz),
                        fontSize = LargeTextSize,
                    )
                    SpaceSmall()

                    CustomText(
                        text = stringResource(Res.string.qayta_kirish_telefon_tasdiq),
                        color = MaterialTheme.extendedColor.hintColor
                    )
                }
            }
            SpaceLarge()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(TextFieldCornerRadius))
                    .border(1.dp, SuccessColor.copy(0.3f), RoundedCornerShape(TextFieldCornerRadius))
                    .background(MaterialTheme.extendedColor.cardColor)
                    .padding(CardCornerPadding)
            ) {
                CustomText(
                    text = stringResource(Res.string.nimalar_saqlanib_qoladi),
                )
                SpaceUltraSmall()
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(0.6f)
                )
                SpaceSmall()

                BulletTex(text = stringResource(Res.string.profil_va_sozlamalar_saqlanadi))
                SpaceSmall()
                BulletTex(stringResource(Res.string.qayta_kirsangiz_tiklanadi))
            }
            Spacer(Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = ContainerPadding),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CustomOutlinedButton(
                    text = stringResource(Res.string.bekor_qilish),
                    onClick = {
                        navigator?.pop()
                    },
                    modifier = Modifier.weight(1f)
                )
                CustomButton(
                    text = stringResource(Res.string.chiqish),
                    onClick = {
                        logout = true
                    },
                    color = OtpErrorColor,
                    modifier = Modifier.weight(1f),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.logout),
                            contentDescription = null,
                            modifier = Modifier.size(NormalIconSize)
                        )
                    }
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewSettingsScreen() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ) {
        LogoutUi(
            navigator = null
        )
    }
}