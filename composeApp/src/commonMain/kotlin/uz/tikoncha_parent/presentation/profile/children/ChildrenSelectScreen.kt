package uz.tikoncha_parent.presentation.profile.children

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.farzandlaringiz
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.profile.ProfileEvent
import uz.tikoncha_parent.presentation.profile.ProfileState
import uz.tikoncha_parent.presentation.profile.ProfileViewModel
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.rememberScreenSystemBars

class ChildrenSelectScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel = koinScreenModel<ProfileViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        LaunchedEffect(state.children) {
            event(ProfileEvent.Refresh)
        }

        ChildrenSelectUi(
            navigator = navigator,
            state = state,
            event = event
        )
    }
}

@Composable
fun ChildrenSelectUi(
    navigator: Navigator?,
    state: ProfileState,
    event: (ProfileEvent) -> Unit
) {
    var menuChild by remember { mutableStateOf<UserInfo?>(null) }
    val systemBars = rememberScreenSystemBars(
        statusBarColor = AppColors.bg.secondary,
        navigationBarColor = AppColors.bg.secondary
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(systemBars.modifier)
            .background(AppColors.bg.secondary)
    ) {
        CustomHeader(
            title = stringResource(Res.string.farzandlaringiz),
            showBackButton = true,
            onBackClick = { navigator?.pop() }
        )
        Space(32.dp)

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(state.children, key = { it.userId }) { child ->
                ChildrenItem(
                    name = child.name,
                    gadget = child.phoneNumber,
                    lastSeen = child.last_seen ?: "",
                    imageUrl = child.avatarUrl ?: "",
                    onClick = { navigator?.push(ChildDetailScreen(child)) },
                    onMenuClick = { menuChild = child }
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewChildrenSelectScreen() {
    TikonchaParentTheme(ThemeMode.LIGHT) {
        ChildrenSelectUi(
            navigator = null,
            state = ProfileState(),
            event = {}
        )
    }
}