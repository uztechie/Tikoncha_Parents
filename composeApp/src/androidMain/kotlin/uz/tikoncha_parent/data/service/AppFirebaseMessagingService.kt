package uz.tikoncha_parent.data.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.presentation.push.FcmMessageRouter
import uz.tikoncha_parent.presentation.push.FcmTokenRegister

class AppFirebaseMessagingService : FirebaseMessagingService() {

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: eeeee")

    }
    private val TAG = "AppFirebaseMessagingSer"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "onMessageReceived: message=$remoteMessage")

        val title   = remoteMessage.notification?.title ?: remoteMessage.data["title"]
        val message = remoteMessage.notification?.body  ?: remoteMessage.data["message"]


        val payloadRaw = remoteMessage.data["payload"]
        val notifTitle = remoteMessage.notification?.title
        val notifBody  = remoteMessage.notification?.body

        FcmMessageRouter.handle(payloadRaw, notifTitle, notifBody)

        AndroidNotificationHelper.show(this, title, message)

    }



    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: token=$token")
        AppSettings.fcmToken = token
        FcmTokenRegister.submit(token)
    }
}