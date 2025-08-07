package org.example.project

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.platform.AppEnvironment
import org.example.project.presentation.splash.SplashScreen


import org.example.project.presentation.child_confirm_cod.ChildConfirmCodScreen
import org.example.project.presentation.domain.model.LanguageType
import org.example.project.presentation.login.LoginScreen
import org.example.project.presentation.otp.OtpScreen
import org.example.project.presentation.profile.ProfileScreen
import org.example.project.presentation.profile.ProfileState
import org.example.project.presentation.profile.ProfileUi
import org.example.project.presentation.profile.language.AppLanguage
import org.example.project.presentation.profile.language.LanguageController
import org.example.project.presentation.profile.language.LanguagePrefs
import org.example.project.presentation.profile.language.LocalLanguageController
import org.example.project.ui.theme.BarConfig
import org.example.project.ui.theme.LocalBarsConfig
import org.example.project.ui.theme.NoteMarkTheme
import org.example.project.ui.theme.PlatformThemeBridge
import org.example.project.ui.theme.ThemeController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import ru.sulgik.mapkit.MapKit
import uz.saidburxon.newedu.presentation.feature.main.MainScreen


@Composable
@Preview
fun App() {

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


