package uz.tikoncha_parent.presentation.push

import uz.tikoncha_parent.domain.model.DeepLink

data class PushNotification(
    val title: String?,
    val body: String?,
    val deepLink: DeepLink?,
)

interface PushNotifier {
    fun show(notification: PushNotification)
}