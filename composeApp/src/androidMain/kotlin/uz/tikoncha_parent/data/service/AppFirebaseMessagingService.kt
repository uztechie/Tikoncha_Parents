package uz.tikoncha_parent.data.service

import android.app.PendingIntent
import android.net.Uri
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.presentation.push.FcmTokenRegister
import uz.tikoncha_parent.presentation.push.PushCoordinator


class AppFirebaseMessagingService : FirebaseMessagingService(), KoinComponent {

    private val TAG = "AppFcmService"
    private val coordinator: PushCoordinator by inject()


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "data=${remoteMessage.data}")
        coordinator.onMessage(remoteMessage.data["payload"])
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "onNewToken=$token")
        AppSettings.fcmToken = token
        FcmTokenRegister.submit(token)
    }
}