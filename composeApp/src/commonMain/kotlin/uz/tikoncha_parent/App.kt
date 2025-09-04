package uz.tikoncha_parent

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import uz.tikoncha_parent.platform.AppEnvironment
import uz.tikoncha_parent.presentation.splash.SplashScreen


import uz.tikoncha_parent.presentation.profile.language.LanguageController
import uz.tikoncha_parent.presentation.profile.language.LocalLanguageController
import uz.tikoncha_parent.ui.theme.BarConfig
import uz.tikoncha_parent.ui.theme.LocalBarsConfig
import uz.tikoncha_parent.ui.theme.NoteMarkTheme
import uz.tikoncha_parent.ui.theme.PlatformThemeBridge
import uz.tikoncha_parent.ui.theme.ThemeController
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.sulgik.mapkit.MapKit
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun getCurrentIsoDateTime(): String {
    val nowInstant = Clock.System.now()
    val localDateTime = nowInstant.toLocalDateTime(TimeZone.UTC)

    val year = localDateTime.year.toString().padStart(4, '0')
    val month = localDateTime.monthNumber.toString().padStart(2, '0')
    val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
    val hour = localDateTime.hour.toString().padStart(2, '0')
    val minute = localDateTime.minute.toString().padStart(2, '0')
    val second = localDateTime.second.toString().padStart(2, '0')
    val millis = (localDateTime.nanosecond / 1_000_000).toString().padStart(3, '0')

    return "$year-$month-${day}T$hour:$minute:$second.${millis}"
}

@Composable
@Preview
fun App() {


    println("ASASASAS=${getCurrentIsoDateTime()}")

    val langController = remember { LanguageController() }

    val mode by ThemeController.mode.collectAsState()
    SideEffect { PlatformThemeBridge.onModeChanged(mode) }
    val barsConfig = remember { mutableStateOf(BarConfig()) }

    AppEnvironment {

        CompositionLocalProvider(
            LocalLanguageController provides langController,
            LocalBarsConfig provides barsConfig
        )
        {
            NoteMarkTheme(
                mode = mode
            ) {
                val cfg = barsConfig.value
                val lang = langController.current.collectAsState()



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


