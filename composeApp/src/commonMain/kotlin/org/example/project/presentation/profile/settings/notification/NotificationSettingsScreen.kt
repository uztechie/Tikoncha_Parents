package org.example.project.presentation.profile.settings.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.presentation.common.CustomButton
import org.example.project.presentation.profile.CustomHeader
import org.example.project.ui.BackgroundColor
import org.example.project.ui.ButtonHeight
import org.example.project.ui.ContainerPadding
import org.example.project.ui.LargeTextSize
import org.jetbrains.compose.ui.tooling.preview.Preview
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
            .background(BackgroundColor)
    ) {
        CustomHeader(
            title = "Bildirishnomalar",
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
                text = "Davom etish",
                fontSize = LargeTextSize,
                onClick = {
                    navigator!!.pop()
                }

            )

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