package uz.tikoncha_parent.data.service

import android.app.PendingIntent
import android.net.Uri
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.domain.model.PayloadType
import uz.tikoncha_parent.domain.use_case.chat.ParseFcmPayloadUseCase
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
        Log.d(TAG, "onMessageReceived: message=${remoteMessage.data}")



        val payloadRaw = remoteMessage.data["payload"]
        val payload = ParseFcmPayloadUseCase()(payloadRaw)
        val title = payload?.title
        val message  = payload?.message
        Log.d(TAG, "onMessageReceived: payload=$payload")


        FcmMessageRouter.handle(payloadRaw, title, message, uiReady = false)
        var pi: PendingIntent? = null

        when(payload?.type){
            PayloadType.TODO -> {}
            PayloadType.NEWS -> {
                val id = payload.body?.news?.id?.toLongOrNull()
                if (id != null) {
                    val uri = Uri.Builder().scheme("myapp").authority("news")
                        .appendQueryParameter("newsId", id.toString())
                        .build()
                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, uri, this, uz.tikoncha_parent.MainActivity::class.java)
                        .addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    pi = PendingIntent.getActivity(this, id.hashCode(), intent, flags)
                }
            }
            PayloadType.CHAT -> {
                val m = payload.body?.message
                val chatId = m?.chat_id
                val chatTitle = m?.chat_title
                if (chatId != null) {
                    pi = NotificationIntentFactory.chatPendingIntent(this, chatId, chatTitle, null)
                }
            }
            PayloadType.GENERAL -> {}
            PayloadType.CHILD_REQUEST -> {
                val uri = Uri.Builder().scheme("myapp").authority("child_request")
                    .build()
                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, uri, this, uz.tikoncha_parent.MainActivity::class.java)
                    .addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP)
                val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                pi = PendingIntent.getActivity(this, 20256, intent, flags)
            }
            null -> {}
        }


        AndroidNotificationHelper.ensureChannel(this)

        AndroidNotificationHelper.show(
            ctx = this,
            title = title,
            body = message,
            pendingIntent = pi
        )

    }



    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: token=$token")
        AppSettings.fcmToken = token
        FcmTokenRegister.submit(token)
    }
}