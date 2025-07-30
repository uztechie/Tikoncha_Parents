package org.example.project.presentation.profile.settings.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.presentation.base.CustomHeader
import org.example.project.ui.ButtonHeight
import org.example.project.ui.ContainerPadding
import org.example.project.ui.LargeTextSize
import org.example.project.ui.NormalLargeTextSize
import org.example.project.ui.SpaceLarge
import org.example.project.ui.SpaceMedium
import org.example.project.ui.SpaceSmall
import org.example.project.ui.theme.ThemeController
import org.example.project.ui.theme.ThemeController.mode
import org.example.project.ui.theme.ThemeMode
import org.example.project.ui.theme.ThemePrefs
import org.example.project.ui.theme.ThemeSelectorWithImage
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.davom_etish
import tikoncha_parents.composeapp.generated.resources.tema

class ThemeScreen: Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        ThemeUi(
            navigator = navigator
        )
    }
}

@Composable
fun ThemeUi(
    navigator: Navigator?
){
    val mode: ThemeMode = ThemeMode.SYSTEM

    val dark = when (mode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.DARK   -> true
        ThemeMode.LIGHT  -> false
    }

    val current by ThemeController.mode.collectAsState()

    var selectedTheme by remember {
        mutableStateOf(ThemeMode.SYSTEM)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CustomHeader(
            title = stringResource(Res.string.tema),
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
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                ThemeMode.entries.filterNot { it == ThemeMode.SYSTEM }.forEach { mode ->

                    ThemeSelectorWithImage(
                        modifier = Modifier
                            .weight(1f),
                        selectedTheme = mode,
                        onThemeSelected = { theme ->
                            selectedTheme = theme
                        },
                        selected = mode == selectedTheme
                    )
                }
            }



            Spacer(
                modifier = Modifier
                    .weight(1f)
            )

            uz.saidburxon.newedu.presentation.base.CustomButton(
                text = stringResource(Res.string.davom_etish),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonHeight),
                enabled = true,
                fontSize = NormalLargeTextSize,
                onClick = {
                    ThemePrefs.save(selectedTheme)
                    ThemeController.setMode(selectedTheme)
                }
            )
            SpaceLarge()
        }
    }
}

@Preview
@Composable
private fun PreviewThemeScreen(){
    ThemeUi(
        navigator = null
    )
}