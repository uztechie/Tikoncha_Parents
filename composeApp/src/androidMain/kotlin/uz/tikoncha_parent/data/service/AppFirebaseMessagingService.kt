package uz.tikoncha_parent.data.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import uz.tikoncha_parent.domain.model.PushMessage
import uz.tikoncha_parent.presentation.push.PushBus

class AppFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "AppFirebaseMessagingSer"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "onMessageReceived: message=$remoteMessage")

        val title   = remoteMessage.notification?.title ?: remoteMessage.data["title"]
        val message = remoteMessage.notification?.body  ?: remoteMessage.data["message"]
        val dateMs  = remoteMessage.data["date"]?.toLongOrNull()
            ?: System.currentTimeMillis()

        PushBus.dispatchMessage(PushMessage(title, message, emptyMap()))

        AndroidNotificationHelper.show(this, title, message, dateMs)

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: token=$token")
    }
}