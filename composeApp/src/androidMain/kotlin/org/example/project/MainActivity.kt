package org.example.project

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import org.example.project.platform.Localization
import org.example.project.presentation.profile.language.LanguagePrefs
import org.example.project.ui.theme.ThemeMode
import org.example.project.ui.theme.ThemePrefs
import ru.sulgik.mapkit.MapKit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        MapKit.initialize(this)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.Transparent.toArgb(), Color.Transparent.toArgb()),
            navigationBarStyle = SystemBarStyle.auto(Color.Transparent.toArgb(), Color.Transparent.toArgb())
        )

        if (Build.VERSION.SDK_INT >= 29) {
            // 3-button navda qo‘shiladigan xira fonni o‘chirib qo‘yish (xohishga ko‘ra)
            window.isNavigationBarContrastEnforced = false
        }


        super.onCreate(savedInstanceState)
        val saved = LanguagePrefs.loadOrDefault().languageCode
        // 2) UI dan oldin qo‘llang
        Localization(this).applyLanguage(saved)



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