package uz.tikoncha_parent

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.messaging.FirebaseMessaging
import com.yandex.mapkit.MapKit

import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.remote.telegram.TelegramConfig
import uz.tikoncha_parent.data.remote.telegram.TelegramLoginCoordinator
import uz.tikoncha_parent.presentation.push.AndroidDeepLinkParser
import uz.tikoncha_parent.presentation.push.DeepLinkBus
import uz.tikoncha_parent.presentation.push.FcmTokenRegister
import uz.tikoncha_parent.presentation.push.PendingDeepLinks
import uz.tikoncha_parent.presentation.push.PushPlatform

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        Log.d(TAG, "onCreate: ")


        super.onCreate(savedInstanceState)

        TelegramLoginCoordinator.bindActivity(this)
        handleTelegramIntent(intent)

        PushPlatform.bind(this)
        PushPlatform.initialize()
        PushPlatform.requestNotificationPermissionIfNeeded()

        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            Log.d("TAG", "onCreate: token=$token")
            AppSettings.fcmToken = token
            FcmTokenRegister.submit(token)
        }

        // Cold startda Intent’dan deep link kelsa — queue ga tashlaymiz

        AndroidDeepLinkParser.parse(intent)?.let { link ->
            Log.d(TAG, "enqueue link=$link")
            PendingDeepLinks.enqueue(link)   // UI tayyor bo‘lganda o‘qiydi
            DeepLinkBus.open(link)       // UI tayyor bo‘lsa darhol ochadi
        }

        Log.d(TAG, "onCreate: intent=${intent.data}")

        intent?.data = null
        setIntent(intent)


        setContent {
            App()
        }

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }


    override fun onDestroy() {
        TelegramLoginCoordinator.unbindActivity(this)
        super.onDestroy()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d(TAG, "onNewIntent: ")
        handleTelegramIntent(intent)
        // App ochiq bo'lsa bus orqali darhol navigate
        AndroidDeepLinkParser.parse(intent)?.let { link ->
            Log.d(TAG, "new intent enqueue link=$link")
            PendingDeepLinks.enqueue(link)   // UI tayyor bo‘lganda o‘qiydi
            DeepLinkBus.open(link)       // UI tayyor bo‘lsa darhol ochadi
        }
    }

    private fun handleTelegramIntent(intent: Intent?) {
        val uri: Uri = intent?.data ?: return
        if (uri.host == TelegramConfig.REDIRECT_HOST) {
            TelegramLoginCoordinator.handleResponse(uri)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}