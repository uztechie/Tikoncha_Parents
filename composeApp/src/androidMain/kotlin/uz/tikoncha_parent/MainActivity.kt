package uz.tikoncha_parent

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

import ru.sulgik.mapkit.MapKit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        MapKit.initialize(this)
        enableEdgeToEdge()
//        enableEdgeToEdge(
//            statusBarStyle = SystemBarStyle.auto(Color.Transparent.toArgb(), Color.Transparent.toArgb()),
//            navigationBarStyle = SystemBarStyle.auto(Color.Transparent.toArgb(), Color.Transparent.toArgb())
//        )
//
//        if (Build.VERSION.SDK_INT >= 29) {
//            // 3-button navda qo‘shiladigan xira fonni o‘chirib qo‘yish (xohishga ko‘ra)
//            window.isNavigationBarContrastEnforced = false
//        }


        super.onCreate(savedInstanceState)




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