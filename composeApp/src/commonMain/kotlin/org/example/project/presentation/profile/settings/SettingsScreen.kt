package org.example.project.presentation.profile.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.presentation.common.CustomButton
import org.example.project.presentation.profile.CustomHeader
import org.example.project.presentation.profile.settings.notification.NotificationSettingsScreen
import org.example.project.presentation.profile.settings.theme.ThemeScreen
import org.example.project.ui.BackgroundColor
import org.example.project.ui.ButtonHeight
import org.example.project.ui.ContainerPadding
import org.example.project.ui.LargeTextSize
import org.jetbrains.compose.ui.tooling.preview.Preview

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
            .background(BackgroundColor)
    ) {
        CustomHeader(
            title = "Sozlamalar",
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
                text = "Davom etish",
                fontSize = LargeTextSize,
                onClick = {
                    navigator!!.pop()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonHeight),
                enabled = true
            )

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