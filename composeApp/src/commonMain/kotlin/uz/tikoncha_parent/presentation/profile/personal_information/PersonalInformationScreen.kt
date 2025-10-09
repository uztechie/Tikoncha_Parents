package uz.tikoncha_parent.presentation.profile.personal_information

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import uz.tikoncha_parent.presentation.add_child.AddChildScreen
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
import uz.tikoncha_parent.ui.NormalLargeTextSize
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.SpaceLarge
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.SpaceSmall
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.add_square
import tikoncha_parents.composeapp.generated.resources.farzand_qo_shish
import tikoncha_parents.composeapp.generated.resources.farzandingiz
import tikoncha_parents.composeapp.generated.resources.farzandlaringiz
import tikoncha_parents.composeapp.generated.resources.shaxsiy_malumotlar
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.ui.theme.extendedColor

class   PersonalInformationScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current
        val rootNavigator = navigator?.parent

        val viewModel = koinViewModel<ProfileViewModel>()

        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

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
    val rootNavigator = LocalNavigator.current


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
                rootNavigator?.pop()
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(ContainerPadding)
        )
        {

            item {
                ProfileHeader(
                    fullName = state.userInfo?.name ?: "",
                    fathersName = "",
                    onSelectImageButtonClick = {
                        launchPicker()
                    },
                    image = null,
                    state = state
                )

                SpaceLarge()
            }

            item {
                PersonalInfoItem(state.userInfo)
            }
            item {
                SpaceSmall()

                CustomText(
                    text = stringResource(Res.string.farzandlaringiz),
                    fontSize = NormalLargeTextSize,
                    fontWeight = FontWeight.SemiBold
                )

                SpaceSmall()
            }

            items(state.children){userInfo->
                PersonalInfoItem(userInfo)
                SpaceMedium()
            }



            item {
                CustomOutlinedButton(
                    text = stringResource(Res.string.farzand_qo_shish),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(ButtonHeight),
                    endingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.add_square),
                            contentDescription = "",
                            tint = PrimaryColor
                        )
                    },
                    onClick = { rootNavigator?.push(AddChildScreen())},
                    textColor = PrimaryColor,
                    borderColor = PrimaryColor
                )
            }


        }
    }
}

@Preview
@Composable
private fun PreviewPersonalInformationScreen() {
    PersonalInformationUi(
        state = ProfileState(),
        event = {}
    )
}