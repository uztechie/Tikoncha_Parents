package uz.tikoncha_parent.platform.push

import uz.tikoncha_parent.presentation.push.PushNotification
import uz.tikoncha_parent.presentation.push.PushNotifier

class IosPushNotifier : PushNotifier {
    override fun show(notification: PushNotification) { /* no-op */ }
}