package org.example.project.presentation.profile.language

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
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
    var languageIos by rememberStringSetting(
        key = "savedLanguageIos",
        defaultValue = LanguageType.UZ.languageCode
    )
    val selectedLanguage by derivedStateOf {
        LanguageType.entries.first{it.languageCode == languageIos}
    }


//    var selectedLanguage by remember {
//        mutableStateOf(LanguageType.UZ)
//    }

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
                onLanguageSelected = {
                    languageIos = if (it == LanguageType.UZ) AppLanguage.UZ.ios
                    else AppLanguage.RU.ios
                    localization.applyLanguage(languageIos)
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