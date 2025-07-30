package org.example.project

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.presentation.splash.SplashScreen
import dev.burnoo.compose.remembersetting.rememberStringSetting
import org.example.project.platform.AppEnvironment
import org.example.project.platform.Localization
import org.example.project.platform.customAppLocale
import org.example.project.presentation.domain.model.LanguageType
import org.example.project.presentation.profile.ProfileScreen
import org.example.project.presentation.profile.language.AppLanguage
import org.example.project.presentation.profile.language.LanguageController
import org.example.project.presentation.profile.language.LanguageManager
import org.example.project.presentation.profile.language.LanguagePrefs
import org.example.project.presentation.profile.language.LocalLanguageController
import org.example.project.ui.theme.NoteMarkTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import ru.sulgik.mapkit.MapKit
import uz.saidburxon.newedu.presentation.feature.main.MainScreen


@Composable
@Preview
fun App() {
    val langController = remember { LanguageController() }

    LaunchedEffect(Unit) {
        customAppLocale = langController.current.languageCode
    }

    AppEnvironment {

        CompositionLocalProvider(LocalLanguageController provides langController) {
            NoteMarkTheme {
                Surface(
                    modifier = Modifier
                        .statusBarsPadding()
                        .navigationBarsPadding()
                ) {
                    key(customAppLocale) {
                        Navigator(SplashScreen())
                    }
                }
            }
        }


    }

}

fun initMapKit() {
    val MAP_KEY: String = "21612db3-4394-4fde-b579-d2e7a1f9afa3"
    MapKit.setApiKey(MAP_KEY)
}


