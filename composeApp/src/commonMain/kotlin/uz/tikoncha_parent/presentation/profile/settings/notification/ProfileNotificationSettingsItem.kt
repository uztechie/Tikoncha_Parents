package uz.tikoncha_parent.presentation.profile.settings.notification

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.ui.DividerHorizontal
import uz.tikoncha_parent.ui.HintTextColor
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.SmallTextSize
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun ProfileNotificationItem(
    notificationStates: Map<NotificationType, Boolean>,
    onNotificationChanged: (NotificationType) -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.extendedColor.backgroundColor),
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

                    CustomText(
                        text = stringResource(notification.settingName),
                        fontSize = NormalTextSize,
                        fontWeight = FontWeight.Medium
                    )
                }

                Box(
                    modifier = Modifier
                        .size(width = 50.dp, height = 28.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isOn) PrimaryColor else MaterialTheme.extendedColor.buttonColor)
                        .clickable {
                            onNotificationChanged(notification)
                        },
                    contentAlignment = if (isOn) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(20.dp)
                            .background(MaterialTheme.extendedColor.backgroundColor, CircleShape)
                    )
                }
            }
            DividerHorizontal()
        }
    }
}


enum class NotificationType(
    val settingName: StringResource,
    val subtitle: StringResource,

    ) {
    E_MAIL(
        settingName = Res.string.pochta_orqali,
        subtitle = Res.string.reklama
    ),
    SMS(
        settingName = Res.string.sms_orqali,
        subtitle = Res.string.reklama
    ),
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        ProfileNotificationItem(
            notificationStates = mapOf(
                NotificationType.E_MAIL to true,
                NotificationType.SMS to false
            ),
            onNotificationChanged = {}
        )
    }

}