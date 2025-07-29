package org.example.project.presentation.profile.language

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import dev.burnoo.compose.remembersetting.rememberStringSetting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.project.platform.Localization
import org.example.project.presentation.base.theme.NormalTextSize
import org.example.project.presentation.common.CustomButton
import org.example.project.presentation.domain.model.LanguageType
import org.example.project.presentation.profile.CustomHeader
import org.example.project.ui.BackgroundColor
import org.example.project.ui.ButtonHeight
import org.example.project.ui.ContainerPadding
import org.example.project.ui.NormalLargeTextSize
import org.example.project.ui.SpaceMedium
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*

class LanguageScreen: Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        LanguageUi(
            navigator = navigator
        )

    }
}

@Composable
fun LanguageUi(
    navigator: Navigator?
){

    val localization = koinInject<Localization>()
    var languageCode by remember {
        mutableStateOf(LanguagePrefs.loadOrDefault().languageCode)
    }

    val selectedLanguage by remember(languageCode) {
        derivedStateOf {
            LanguageType.entries.firstOrNull { it.languageCode == languageCode } ?: LanguageType.UZ
        }
    }

    LaunchedEffect(Unit) {
        // Agar applyLanguage og‘ir ish qilsa, main bloklamaslik uchun
        withContext(Dispatchers.Main) {
            localization.applyLanguage(languageCode)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        CustomHeader(
            title = stringResource(Res.string.til),
            showBackButton = true,
            onBackClick = {
                navigator!!.pop()
            }
        )

        SpaceMedium()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ContainerPadding)
        ) {

            LanguageSelection(
                selectedLanguage = selectedLanguage,
                onLanguageSelected = { type ->
                    // ✅ 1) Local state yangilanadi
                    val newCode = type.languageCode
                    languageCode = newCode
                    // ✅ 2) Diskka saqlanadi (Android/iOS)
                    LanguagePrefs.saveCode(newCode)
                    // ✅ 3) Darhol qo‘llanadi
                    localization.applyLanguage(newCode)
                }
            )

            Spacer(
                modifier = Modifier
                    .weight(1f)
            )

            CustomButton(
                text = stringResource(Res.string.davom_etish),
                enabled = true,
                fontSize = NormalLargeTextSize,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonHeight),
                onClick = {
                    navigator!!.pop()
                }
            )

        }
    }
}

@Preview
@Composable
private fun PreviewLanguageScreen(){
    LanguageUi(
        navigator = null
    )
}