package uz.tikoncha_parent.presentation.profile.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.profile.settings.notification.NotificationSettingsScreen
import uz.tikoncha_parent.presentation.profile.settings.theme.ThemeScreen
import uz.tikoncha_parent.ui.ButtonHeight
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.NormalLargeTextSize
import uz.tikoncha_parent.ui.SpaceLarge
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.davom_etish
import tikoncha_parents.composeapp.generated.resources.sozlamalar
import uz.saidburxon.newedu.presentation.base.CustomButton

class SettingsScreen: Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        SettingsUi(
            navigator = navigator
        )
    }
}

@Composable
fun SettingsUi(
    navigator: Navigator?
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CustomHeader(
            title = stringResource(Res.string.sozlamalar),
            showBackButton = true,
            onBackClick = {
                navigator!!.pop()
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ContainerPadding)
        ) {

            ProfileSettingsItem(
                selectedSetting = SettingType.NOTIFICATION,
                onSettingSelected = {settings ->
                    when(settings){
                        SettingType.NOTIFICATION -> {
                            navigator!!.push(NotificationSettingsScreen())
                        }
                        SettingType.THEME -> {
                            navigator!!.push(ThemeScreen())
                        }
                        SettingType.DANGEROUS_ZONE -> {

                        }
                    }
                }
            )

            Spacer(
                modifier = Modifier
                    .weight(1f)
            )

            CustomButton(
                text = stringResource(Res.string.davom_etish),
                fontSize = NormalLargeTextSize,
                onClick = {
                    navigator!!.pop()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonHeight),
                enabled = true
            )
            SpaceLarge()
        }
    }
}

@Preview
@Composable
fun PreviewSettingsScreen(){
    SettingsUi(
        navigator = null
    )
}