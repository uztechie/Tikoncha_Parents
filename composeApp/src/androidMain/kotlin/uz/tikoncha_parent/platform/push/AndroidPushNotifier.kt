package uz.tikoncha_parent.platform.push

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import uz.tikoncha_parent.MainActivity
import uz.tikoncha_parent.data.service.AndroidNotificationHelper
import uz.tikoncha_parent.domain.model.DeepLink
import uz.tikoncha_parent.presentation.push.PushNotification
import uz.tikoncha_parent.presentation.push.PushNotifier

class AndroidPushNotifier(private val context: Context): PushNotifier{

    override fun show(notification: PushNotification) {
        val pi = notification.deepLink?.let { pendingIntentFor(it) }
        AndroidNotificationHelper.show(
            ctx = context,
            title = notification.title,
            body = notification.body,
            pendingIntent = pi
        )
    }


    private fun pendingIntentFor(link: DeepLink): PendingIntent{
        val intent = Intent(Intent.ACTION_VIEW, DeepLinks.uri(link), context, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getActivity(context,link.hashCode(), intent, flags)
    }

}