package org.example.project.presentation.profile.settings.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.project.ui.BackgroundColor
import org.example.project.ui.DividerHorizontal
import org.example.project.ui.HintTextColor
import org.example.project.ui.NormalLargeTextSize
import org.example.project.ui.NormalTextSize
import org.example.project.ui.PrimaryColor
import org.example.project.ui.SmallTextSize
import org.example.project.ui.TextColor
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ProfileNotificationItem(
    notificationStates: Map<NotificationType, Boolean>,
    onNotificationChanged: (NotificationType) -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BackgroundColor),
    ) {
        NotificationType.values().forEach { notification ->
            val isOn = notificationStates[notification] == true

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                    ) { onNotificationChanged(notification) }
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = notification.settingName,
                        fontSize = NormalTextSize,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = notification.subtitle,
                        fontSize = SmallTextSize,
                        color = HintTextColor
                    )
                }

                Box(
                    modifier = Modifier
                        .size(width = 50.dp, height = 28.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isOn) PrimaryColor else HintTextColor)
                        .clickable {
                            onNotificationChanged(notification)
                        },
                    contentAlignment = if (isOn) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(20.dp)
                            .background(Color.White, CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            DividerHorizontal()
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}


enum class NotificationType(
    val settingName: String,
    val subtitle: String,

    ) {
    E_MAIL(
        settingName = "Pochta orqali",
        subtitle = "Reklamalar"
    ),
    SMS(
        settingName = "Sms orqali",
        subtitle = "Reklamalar"),
}

@Preview
@Composable
private fun Preview() {

    ProfileNotificationItem(
        notificationStates = mapOf(
            NotificationType.E_MAIL to true,
            NotificationType.SMS to false
        ),
        onNotificationChanged = {}
    )

}