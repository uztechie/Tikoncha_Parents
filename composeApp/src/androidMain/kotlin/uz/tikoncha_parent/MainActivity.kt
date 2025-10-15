package uz.tikoncha_parent

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.messaging.FirebaseMessaging

import ru.sulgik.mapkit.MapKit
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.presentation.push.FcmTokenRegister
import uz.tikoncha_parent.presentation.push.PushPlatform

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        MapKit.initialize(this)
        enableEdgeToEdge()


        super.onCreate(savedInstanceState)

        PushPlatform.bind(this)
        PushPlatform.initialize()
        PushPlatform.requestNotificationPermissionIfNeeded()

        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            Log.d("TAG", "onCreate: token=$token")
            AppSettings.fcmToken = token
            FcmTokenRegister.submit(token)
        }




        setContent {
            App()
        }

    }

    override fun onStart() {
        super.onStart()
        MapKit.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        MapKit.getInstance().onStop()
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}