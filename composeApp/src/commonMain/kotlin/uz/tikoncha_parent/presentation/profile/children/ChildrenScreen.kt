package uz.tikoncha_parent.presentation.profile.children

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.farzand_malumotlari_keyin_korinadi
import tikoncha_parents.composeapp.generated.resources.farzand_qoshilgandan_keyin_korinish
import tikoncha_parents.composeapp.generated.resources.farzand_qoshilmagan
import tikoncha_parents.composeapp.generated.resources.farzandlaringiz
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.presentation.base.AppEmptyList
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.profile.ProfileEvent
import uz.tikoncha_parent.presentation.profile.ProfileState
import uz.tikoncha_parent.presentation.profile.ProfileViewModel
import uz.tikoncha_parent.presentation.profile.child_user_edit.EditScreen
import uz.tikoncha_parent.presentation.profile.personal_information.PersonalInfoItem
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.SpaceLarge
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor


class   ChildrenScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        val viewModel = koinScreenModel<ProfileViewModel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        LaunchedEffect(Unit){
            viewModel.getChildren()
        }

        ChildrenUi(
            navigator = navigator,
            state = state.value,
            event = event
        )
    }
}

@Composable
fun ChildrenUi(
    navigator: Navigator?,
    state: ProfileState,
    event: (ProfileEvent) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {
        CustomHeader(
            title = stringResource(Res.string.farzandlaringiz),
            showBackButton = true,
            onBackClick = {
                navigator?.pop()
            }
        )

        if (state.children.isEmpty()){
            AppEmptyList(
                title = stringResource(Res.string.farzand_qoshilmagan),
                message = stringResource(Res.string.farzand_malumotlari_keyin_korinadi),
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(ContainerPadding),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        )
        {
            items(state.children, key = {it.userId}) { userInfo ->
                PersonalInfoItem(
                    userInfo,
                    onEdit = {
                        navigator?.push(EditScreen(userInfo))
                    }
                )
            }
        }
    }
}


@Preview
@Composable
private fun PreviewPersonalInformationScreen() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        ChildrenUi(
            navigator = null,
            state = ProfileState(),
            event = {}
        )
    }
}