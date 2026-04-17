package uz.tikoncha_parent.presentation.profile.settings.theme

import androidx.compose.foundation.background
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
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.ui.ButtonHeight
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.NormalLargeTextSize
import uz.tikoncha_parent.ui.SpaceLarge
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.theme.ThemeController
import uz.tikoncha_parent.ui.theme.ThemeController.mode
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.ThemeSelectorWithImage
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.saqlash
import tikoncha_parents.composeapp.generated.resources.tema
import uz.tikoncha_parent.presentation.base.CustomButton
import uz.tikoncha_parent.presentation.new_home.NewHomeScreen
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

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
//    val mode: ThemeMode = ThemeMode.SYSTEM
//
//    val dark = when (mode) {
//        ThemeMode.SYSTEM -> isSystemInDarkTheme()
//        ThemeMode.DARK   -> true
//        ThemeMode.LIGHT  -> false
//    }

    val current by mode.collectAsState()

    var selectedTheme by remember(current) {
        mutableStateOf(current)
    }

//    var selectedTheme by remember {
//        mutableStateOf(ThemeMode.SYSTEM)
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
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

            CustomButton(
                text = stringResource(Res.string.saqlash),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonHeight),
                enabled = true,
                onClick = {
                    ThemeController.setMode(selectedTheme)
                    navigator?.replaceAll(NewHomeScreen())
                }
            )
            SpaceLarge()
        }
    }
}

@Preview
@Composable
private fun PreviewThemeScreen(){
    TikonchaParentTheme(
        ThemeMode.DARK
    ) {
        ThemeUi(
            navigator = null
        )
    }
}