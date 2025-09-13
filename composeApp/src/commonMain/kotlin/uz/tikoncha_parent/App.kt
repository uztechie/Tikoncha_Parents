package uz.tikoncha_parent

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import uz.tikoncha_parent.platform.AppEnvironment
import uz.tikoncha_parent.presentation.splash.SplashScreen


import uz.tikoncha_parent.presentation.profile.language.LanguageController
import uz.tikoncha_parent.presentation.profile.language.LocalLanguageController
import uz.tikoncha_parent.ui.theme.BarConfig
import uz.tikoncha_parent.ui.theme.LocalBarsConfig
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.PlatformThemeBridge
import uz.tikoncha_parent.ui.theme.ThemeController
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.sulgik.mapkit.MapKit
import uz.tikoncha_parent.ui.theme.ThemeMode


@Composable
@Preview
fun App() {


    val langController = remember { LanguageController() }
    val barsConfig = remember { mutableStateOf(BarConfig()) }

    val mode by ThemeController.mode.collectAsState(initial = ThemeMode.LIGHT)

    val inPreview = androidx.compose.ui.platform.LocalInspectionMode.current
    if (!inPreview) {
        SideEffect { PlatformThemeBridge.onModeChanged(mode) }
    }

    AppEnvironment {

        CompositionLocalProvider(
            LocalLanguageController provides langController,
            LocalBarsConfig provides barsConfig
        )
        {
            TikonchaParentTheme(
                mode = mode
            ) {
                val cfg = barsConfig.value

                Surface(
                    modifier = Modifier
                        .then(if (cfg.paddingEnabled) Modifier.statusBarsPadding() else Modifier)
                        .then(if (cfg.paddingEnabled) Modifier.navigationBarsPadding() else Modifier)
                ) {
                    
                    Navigator(SplashScreen())
                }

            }
        }
    }


}

fun initMapKit() {
    val MAP_KEY: String = "21612db3-4394-4fde-b579-d2e7a1f9afa3"
    MapKit.setApiKey(MAP_KEY)
}


