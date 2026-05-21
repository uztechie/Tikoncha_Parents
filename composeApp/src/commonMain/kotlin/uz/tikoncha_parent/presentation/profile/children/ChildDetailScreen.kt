package uz.tikoncha_parent.presentation.profile.children

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import uz.tikoncha_parent.domain.model.GenderType
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.profile.ProfileState
import uz.tikoncha_parent.presentation.profile.ProfileViewModel
import uz.tikoncha_parent.presentation.profile.child_user_edit.ChildEditScreen
import uz.tikoncha_parent.presentation.profile.personal_information.PersonalInfoItem
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.rememberScreenSystemBars

class ChildDetailScreen(
    private val child: UserInfo
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel = koinScreenModel<ProfileViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) { viewModel.getChildren() }

        val current = state.children.firstOrNull { it.userId == child.userId } ?: child

        ChildDetailUi(
            navigator = navigator,
            child = current,
            onEdit = { navigator?.push(ChildEditScreen(current)) }
        )
    }
}

@Composable
fun ChildDetailUi(
    child: UserInfo,
    navigator: Navigator?,
    onEdit: () -> Unit = {},
) {
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
            title = child.name,
            showBackButton = true,
            onBackClick = { navigator?.pop() }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ContainerPadding),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            PersonalInfoItem(
                userInfo = child,
                onEdit = onEdit
            )
        }
    }
}

@Preview
@Composable
private fun PreviewChildrenSelectScreen() {
    TikonchaParentTheme(ThemeMode.LIGHT) {
        ChildDetailUi(
            navigator = null,
            child = UserInfo(
                userId = "1",
                phoneNumber = "+998901234567",
                fullName = "John Doe",
                name = "John",
                lastName = "Doe",
                patronymic = "Smith",
                genderType = GenderType.MALE,
                age = 18
            )
        )
    }
}