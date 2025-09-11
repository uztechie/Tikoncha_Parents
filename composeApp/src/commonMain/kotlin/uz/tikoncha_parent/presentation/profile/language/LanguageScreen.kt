package uz.tikoncha_parent.presentation.profile.language

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.ui.ButtonHeight
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.NormalLargeTextSize
import uz.tikoncha_parent.ui.SpaceLarge
import uz.tikoncha_parent.ui.SpaceMedium
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.tikoncha_parent.ui.theme.extendedColor

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
    val controller = remember {
        LocalLanguageController
    }.current


    val current = controller.current.collectAsState().value
    var selectedLanguage by remember {
        mutableStateOf(current)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
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
                    selectedLanguage = type
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
                    controller.select(selectedLanguage)
                    navigator!!.pop()
                }
            )
            SpaceLarge()
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