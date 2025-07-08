package org.example.project.presentation.profile.language

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import org.example.project.presentation.domain.model.LanguageType
import org.example.project.presentation.profile.CustomHeader
import org.example.project.ui.BackgroundColor
import org.example.project.ui.ContainerPadding
import org.example.project.ui.NormalLargeTextSize
import org.example.project.ui.SpaceMedium
import org.jetbrains.compose.ui.tooling.preview.Preview

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

    var selectedLanguage by remember {
        mutableStateOf(LanguageType.UZ)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        CustomHeader(
            title = "Til",
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
                onLanguageSelected = {selectedLanguage = it}
            )

            Spacer(
                modifier = Modifier
                    .weight(1f)
            )

            CustomButton(
                text = "Davom etish",
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    navigator!!.pop()
                },
                fontSize = NormalLargeTextSize
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