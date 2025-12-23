package uz.tikoncha_parent.presentation.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.data.mapper.toUi
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.domain.model.LanguageType
import uz.tikoncha_parent.presentation.profile.language.LanguagePrefs
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.NormalLargeTextSize
import uz.tikoncha_parent.ui.SmallTextSize
import uz.tikoncha_parent.ui.theme.extendedColor

class NotificationVerifyScreen(
    private val newsId: Long
) : Screen {

    @Composable
    override fun Content() {
        NotificationVerifyUi(newsId = newsId)
    }
}
@Composable
fun NotificationVerifyUi(
    newsId: Long,
) {

    val navigator: Navigator? = LocalNavigator.current
    val viewModel: NotificationViewModel = koinViewModel()

    val uiState by viewModel.state.collectAsStateWithLifecycle()


    val language = remember {
        if (LanguagePrefs.loadOrDefault() == LanguageType.UZ) "uz" else "ru"
    }

    val ui = remember(uiState.items, newsId, language) {
        uiState.items.firstOrNull { it.id == newsId }?.toUi(language)
    }

    LaunchedEffect(newsId) {
        if (ui == null) viewModel.load()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {
        CustomHeader(
            title = ui?.title?:"",
            onBackClick = {
                navigator?.pop()
            },
            showBackButton = true
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ContainerPadding)
        ) {
            CustomText(
                text = ui?.message?:"",
                fontSize = NormalLargeTextSize,
                fontWeight = FontWeight.W500
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                CustomText(
                    text = formatTimeOrDayNumber(ui?.createdAt ?: 0L),
                    fontSize = SmallTextSize
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    NotificationVerifyScreen(
        newsId = 1
    )
}