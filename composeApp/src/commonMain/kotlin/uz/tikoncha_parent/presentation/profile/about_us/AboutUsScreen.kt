package uz.tikoncha_parent.presentation.profile.about_us

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.about_text_html
import tikoncha_parents.composeapp.generated.resources.biz_haqimizda
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.htmlToAnnotatedString
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor
import uz.tikoncha_parent.ui.theme.rememberScreenSystemBars


class AboutUsScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current ?: return

        AboutUsUi(navigator = navigator,)

    }
}
@Composable
fun AboutUsUi(
    navigator: Navigator?
) {
    val systemBars = rememberScreenSystemBars(
        statusBarColor = MaterialTheme.extendedColor.backgroundColor,
        navigationBarColor = MaterialTheme.extendedColor.backgroundColor,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(systemBars.modifier)
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {
        CustomHeader(
            showBackButton = true,
            title = stringResource(Res.string.biz_haqimizda),
            onBackClick = {
                navigator?.pop()
            }
        )
        val html = stringResource(Res.string.about_text_html)

        val spanned: AnnotatedString = htmlToAnnotatedString(html)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.extendedColor.backgroundColor)
                .padding(ContainerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            CustomText(
                text = spanned,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@Preview
@Composable
private fun PRE() {
    TikonchaParentTheme(
        ThemeMode.LIGHT
    ) {
        AboutUsUi(
            navigator = null
        )
    }
}