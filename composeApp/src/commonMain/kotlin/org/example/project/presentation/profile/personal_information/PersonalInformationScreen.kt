package org.example.project.presentation.profile.personal_information

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.example.project.presentation.add_child.AddChildScreen
import org.example.project.data.mapper.toUploadPart
import org.example.project.platform.decodeImageBitmapOrNull
import org.example.project.platform.rememberImagePicker
import org.example.project.presentation.add_child.AddChildScreen
import org.example.project.presentation.base.CustomHeader
import org.example.project.presentation.base.CustomOutlinedButton
import org.example.project.presentation.profile.ProfileEvent
import org.example.project.presentation.profile.ProfileHeader
import org.example.project.presentation.profile.ProfileState
import org.example.project.presentation.profile.ProfileViewModel
import org.example.project.ui.AppIconInnerPadding
import org.example.project.ui.ButtonHeight
import org.example.project.ui.ContainerPadding
import org.example.project.ui.NormalLargeTextSize
import org.example.project.ui.PrimaryColor
import org.example.project.ui.ShapeCornerRadius
import org.example.project.ui.SpaceLarge
import org.example.project.ui.SpaceMedium
import org.example.project.ui.SpaceSmall
import org.example.project.ui.SpaceUltraSmall
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.add_square
import tikoncha_parents.composeapp.generated.resources.calendar
import tikoncha_parents.composeapp.generated.resources.class_icon
import tikoncha_parents.composeapp.generated.resources.farzand_qo_shish
import tikoncha_parents.composeapp.generated.resources.farzandingiz
import tikoncha_parents.composeapp.generated.resources.id_card
import tikoncha_parents.composeapp.generated.resources.*
import tikoncha_parents.composeapp.generated.resources.maktab
import tikoncha_parents.composeapp.generated.resources.phone
import tikoncha_parents.composeapp.generated.resources.profile
import tikoncha_parents.composeapp.generated.resources.school_icon
import tikoncha_parents.composeapp.generated.resources.shaxsiy_malumotlar
import tikoncha_parents.composeapp.generated.resources.shift_clock
import tikoncha_parents.composeapp.generated.resources.two_users
import uz.saidburxon.newedu.presentation.base.CustomText

class PersonalInformationScreen : Screen {
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
            .background(MaterialTheme.colorScheme.background)
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
                    fullName = state.userInfo?.fullName ?: "",
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
                    text = stringResource(Res.string.farzandingiz),
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