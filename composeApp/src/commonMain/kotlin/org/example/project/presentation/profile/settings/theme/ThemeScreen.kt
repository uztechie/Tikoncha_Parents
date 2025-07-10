package org.example.project.presentation.profile.settings.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.presentation.common.CustomButton
import org.example.project.presentation.profile.CustomHeader
import org.example.project.ui.BackgroundColor
import org.example.project.ui.ButtonHeight
import org.example.project.ui.ContainerPadding
import org.example.project.ui.LargeTextSize
import org.example.project.ui.SpaceMedium
import org.example.project.ui.SpaceSmall
import org.jetbrains.compose.ui.tooling.preview.Preview

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

    var selectedTheme by remember {
        mutableStateOf("light")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        CustomHeader(
            title = "Tema",
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

            ThemeSelector(
                selectedTheme = selectedTheme,
                onThemeSelected = { theme ->
                    selectedTheme = theme
                }
            )

            Spacer(
                modifier = Modifier
                    .weight(1f)
            )

            CustomButton(
                text = "Davom etish",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonHeight),
                enabled = true,
                fontSize = LargeTextSize,
                onClick = {
                    navigator!!.pop()
                }
            )

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