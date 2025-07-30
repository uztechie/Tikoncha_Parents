package org.example.project.presentation.profile.settings.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.presentation.base.CustomHeader
import org.example.project.ui.BackgroundColor
import org.example.project.ui.ButtonHeight
import org.example.project.ui.ContainerPadding
import org.example.project.ui.LargeTextSize
import org.example.project.ui.NormalLargeTextSize
import org.example.project.ui.SpaceLarge
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.bildirishnomalar
import tikoncha_parents.composeapp.generated.resources.davom_etish
import uz.saidburxon.newedu.presentation.base.CustomButton
import kotlin.text.set

class NotificationSettingsScreen: Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        NotificationSettingsUi(
            navigator = navigator
        )

    }
}

@Composable
fun NotificationSettingsUi(
    navigator: Navigator?
){

    val notificationStates = remember {
        mutableStateMapOf<NotificationType, Boolean>().apply {
            NotificationType.values().forEach { this[it] = false }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CustomHeader(
            title = stringResource(Res.string.bildirishnomalar),
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

            ProfileNotificationItem(
                notificationStates = notificationStates,
                onNotificationChanged = {notificationType ->
                    notificationStates[notificationType] = !notificationStates[notificationType]!!
                }
            )

            Spacer(
                modifier = Modifier
                    .weight(1f)
            )

            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonHeight),
                text = stringResource(Res.string.davom_etish),
                fontSize = NormalLargeTextSize,
                onClick = {
                    navigator!!.pop()
                }
            )
            SpaceLarge()
        }
    }
}

@Preview
@Composable
private fun PreviewNotificationsettingsScreen(){
    NotificationSettingsUi(
        navigator = null
    )
}