package uz.tikoncha_parent.presentation.profile.settings.theme

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.ui.ButtonHeight
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.SpaceLarge
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.theme.ThemeController
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.ThemeSelectorWithImage
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.saqlash
import tikoncha_parents.composeapp.generated.resources.tema
import uz.tikoncha_parent.presentation.base.CustomButton
import uz.tikoncha_parent.presentation.new_home.NewHomeScreen
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.rememberScreenSystemBars

class ThemeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        ThemeUi(navigator = navigator)
    }
}

@Composable
fun ThemeUi(
    navigator: Navigator?
) {
    val savedMode by ThemeController.mode.collectAsState()
    val isSystemDark = isSystemInDarkTheme()
    var draftMode by remember(savedMode) { mutableStateOf(savedMode) }
    val highlightedMode = remember(draftMode, isSystemDark) {
        when (draftMode) {
            ThemeMode.SYSTEM -> {
                if (isSystemDark) ThemeMode.DARK else ThemeMode.LIGHT
            }

            else -> draftMode
        }
    }

    val hasChanged = highlightedMode != savedMode
    val systemBars = rememberScreenSystemBars(
        statusBarColor = AppColors.bg.page,
        navigationBarColor = AppColors.bg.page
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(systemBars.modifier)
            .background(AppColors.bg.page)
    ) {
        CustomHeader(
            title = stringResource(Res.string.tema),
            showBackButton = true,
            onBackClick = {
                navigator?.pop()
            }
        )
        SpaceMedium()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ContainerPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                ThemeMode.entries.filterNot { it == ThemeMode.SYSTEM }.forEach { mode ->
                    ThemeSelectorWithImage(
                        selectedTheme = mode,
                        modifier = Modifier.weight(1f),
                        selected = mode == highlightedMode,
                        onThemeSelected = { theme ->
                            draftMode = theme
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))

            CustomButton(
                text = stringResource(Res.string.saqlash),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonHeight),
                enabled = hasChanged,
                onClick = {
                    ThemeController.setMode(draftMode)
                    navigator?.replaceAll(NewHomeScreen())
                }
            )
            SpaceLarge()
        }
    }
}

@Preview
@Composable
private fun PreviewThemeScreen() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ) {
        ThemeUi(navigator = null)
    }
}