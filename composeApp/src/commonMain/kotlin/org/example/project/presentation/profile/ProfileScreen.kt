package org.example.project.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.example.project.data.mapper.toUploadPart
import org.example.project.platform.decodeImageBitmapOrNull
import org.example.project.platform.rememberImagePicker
import org.example.project.presentation.base.CustomHeader
import org.example.project.presentation.base.CustomOutlinedButton
import org.example.project.presentation.common.TransparentQrScreen
import org.example.project.presentation.profile.coins.CoinsScreen
import org.example.project.presentation.profile.language.LanguageScreen
import org.example.project.presentation.profile.personal_information.PersonalInformationScreen
import org.example.project.presentation.profile.settings.SettingsScreen
import org.example.project.presentation.profile.subscription.SubscriptionScreen
import org.example.project.ui.*
import org.example.project.ui.SpaceLarge
import org.example.project.ui.SpaceSmall
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import qrgenerator.qrkitpainter.rememberQrKitPainter
import tikoncha_parents.composeapp.generated.resources.*

class ProfileScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        val viewModel = koinViewModel<ProfileViewModel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        ProfileUi(
            event = event,
            state = state.value
        )
    }
}

@Composable
fun ProfileUi(
    state: ProfileState,
    event: (ProfileEvent) -> Unit
) {
    val navigator = LocalNavigator.current
    val rootNavigator = navigator?.parent

    var showQrCode by remember { mutableStateOf(false) }

    val sections = remember {
        mutableStateListOf(
            ProfileSectionItemData(
                painter = Res.drawable.profile,
                section = ProfileSection.PERSONAL_INFORMATION
            ),
            ProfileSectionItemData(
                painter = Res.drawable.global,
                section = ProfileSection.LANGUAGE
            ),
            ProfileSectionItemData(
                painter = Res.drawable.settings,
                section = ProfileSection.SETTINGS
            ),
            ProfileSectionItemData(
                painter = Res.drawable.telegrams_star,
                section = ProfileSection.SUBSCRIPTIONS
            ),
            ProfileSectionItemData(
                painter = Res.drawable.coins,
                section = ProfileSection.COINS
            ),
        )
    }

    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    val launchPicker = rememberImagePicker { picked ->
        event(ProfileEvent.OnAvatarPhotoSelected(picked.toUploadPart("avatar.jpg")))
        imageBitmap = decodeImageBitmapOrNull(picked.bytes)
    }

    val painter = rememberQrKitPainter(data = "There will be url or smth like this")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CustomHeader(
            title = stringResource(Res.string.profil),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ContainerPadding)
        ) {


            ProfileHeader(
                fullName = state.userInfo?.fullName ?: "",
                fathersName = "",
                image = imageBitmap,
                onSelectImageButtonClick = {
//                    event(ProfileEvent.OnChangeProfileImageClicked(null))
                    launchPicker()
                },
                state = state
            )

            SpaceLarge()

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {

                UserStatsItem(
                    title = stringResource(Res.string.farzandlaringiz_tangalari),
                    value = "0 ${stringResource(Res.string.ta)}",
                    icon = painterResource(Res.drawable.coin),
                    modifier = Modifier
                        .height(ProfileStatsContainerHeight)
                        .weight(1f)
                )

                SpaceSmall()

                UserStatsItem(
                    title = stringResource(Res.string.farzandingiz_bajarilmagan_vazifalari),
                    value = "0 ${stringResource(Res.string.ta)}",
                    icon = painterResource(Res.drawable.file),
                    modifier = Modifier
                        .height(ProfileStatsContainerHeight)
                        .weight(1f)
                )
            }

            SpaceLarge()

            sections.forEach { data ->

                SpaceSmall()

                ProfileSectionItem(
                    icon = painterResource(data.painter),
                    section = data.section,
                    onItemClick = { section ->
                        when (section) {

                            ProfileSection.PERSONAL_INFORMATION -> {
                                rootNavigator?.push(PersonalInformationScreen())
                            }

                            ProfileSection.LANGUAGE -> {
                                rootNavigator!!.push(LanguageScreen())
                            }

                            ProfileSection.SETTINGS -> {
                                rootNavigator!!.push(SettingsScreen())
                            }

                            ProfileSection.SUBSCRIPTIONS -> {
                                rootNavigator!!.push(SubscriptionScreen())
                            }

                            ProfileSection.COINS -> {
                                rootNavigator!!.push(CoinsScreen())
                            }
                        }
                    }
                )
            }

            SpaceLarge()
            SpaceLarge()

            CustomOutlinedButton(
                text = stringResource(Res.string.qr_kod),
                onClick = {
                    showQrCode = true
                },
                textColor = PrimaryColor,
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.scan),
                        contentDescription = ""
                    )
                },
                modifier = Modifier.width(130.dp)
            )

            if (showQrCode) {
                TransparentQrScreen(
                    painter = painter,
                    onDismissRequest = {
                        showQrCode = false
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewProfileScreen() {
    1
    ProfileUi(
        state = ProfileState(),
        event = {}
    )
}
