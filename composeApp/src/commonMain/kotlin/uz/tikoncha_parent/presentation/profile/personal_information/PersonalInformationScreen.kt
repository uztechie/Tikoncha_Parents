package uz.tikoncha_parent.presentation.profile.personal_information

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import uz.tikoncha_parent.data.mapper.toUploadPart
import uz.tikoncha_parent.platform.decodeImageBitmapOrNull
import uz.tikoncha_parent.platform.rememberImagePicker
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.CustomOutlinedButton
import uz.tikoncha_parent.presentation.profile.ProfileEvent
import uz.tikoncha_parent.presentation.profile.ProfileHeader
import uz.tikoncha_parent.presentation.profile.ProfileState
import uz.tikoncha_parent.presentation.profile.ProfileViewModel
import uz.tikoncha_parent.ui.ButtonHeight
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.SpaceLarge
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.chiqishni_tasdiqlang
import tikoncha_parents.composeapp.generated.resources.chiqishni_xohlaysizmi
import tikoncha_parents.composeapp.generated.resources.hisobdan_chiqish
import tikoncha_parents.composeapp.generated.resources.logout
import tikoncha_parents.composeapp.generated.resources.shaxsiy_malumotlar
import tikoncha_parents.composeapp.generated.resources.tasdiqlash
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.login.LoginScreen
import uz.tikoncha_parent.presentation.profile.user_edit.UserEditScreen
import uz.tikoncha_parent.ui.LargeIconSize
import uz.tikoncha_parent.ui.OtpErrorColor
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

class   PersonalInformationScreen : Screen {
    @Composable
    override fun Content() {

        val viewModel = koinScreenModel<ProfileViewModel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        LaunchedEffect(state.value.userInfo){
            event(ProfileEvent.Refresh)
        }

        PersonalInformationUi(
            state = state.value,
            event = event
        )
    }
}

@Composable
fun PersonalInformationUi(
    state: ProfileState,
    event: (ProfileEvent) -> Unit
){
    val navigator = LocalNavigator.current

    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    val launchPicker = rememberImagePicker { picked ->
        event(ProfileEvent.OnAvatarPhotoSelected(picked.toUploadPart("avatar.jpg")))
        imageBitmap = decodeImageBitmapOrNull(picked.bytes)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {
        CustomHeader(
            title = stringResource(Res.string.shaxsiy_malumotlar),
            showBackButton = true,
            onBackClick = {
                navigator?.pop()
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(ContainerPadding)
        ) {
            item {
                ProfileHeader(
                    firstName = state.userInfo?.name ?: "",
                    lastName = state.userInfo?.lastName ?: "",
                    fathersName = state.userInfo?.patronymic ?: "",
                    onSelectImageButtonClick = {
                        launchPicker()
                    },
                    state = state
                )

                SpaceLarge()
            }

            item {
                PersonalInfoItem(
                   userInfo =  state.userInfo,
                    onEdit = {
                        state.userInfo?.let { user ->
                            navigator?.push(UserEditScreen(user))
                        }
                    }
                )
            }
        }
        SpaceMedium()
    }
}

@Preview
@Composable
private fun PreviewPersonalInformationScreen() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        PersonalInformationUi(
            state = ProfileState(),
            event = {}
        )
    }
}