package uz.tikoncha_parent.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import uz.tikoncha_parent.data.mapper.toUploadPart
import uz.tikoncha_parent.platform.decodeImageBitmapOrNull
import uz.tikoncha_parent.platform.rememberImagePicker
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.CustomOutlinedButton
import uz.tikoncha_parent.presentation.common.TransparentQrScreen
import uz.tikoncha_parent.presentation.profile.coins.CoinsScreen
import uz.tikoncha_parent.presentation.profile.language.LanguageScreen
import uz.tikoncha_parent.presentation.profile.personal_information.PersonalInformationScreen
import uz.tikoncha_parent.presentation.profile.settings.SettingsScreen
import uz.tikoncha_parent.presentation.profile.subscription.subscription_payment.SubscriptionPaymentScreen
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.SpaceLarge
import uz.tikoncha_parent.ui.SpaceSmall
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import qrgenerator.qrkitpainter.rememberQrKitPainter
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.domain.use_case.ChildrenUseCase
import uz.tikoncha_parent.domain.use_case.GetCoinPackagesUseCase
import uz.tikoncha_parent.domain.use_case.chat.MyCoinsUseCase
import uz.tikoncha_parent.presentation.profile.coins.MyCoinsViewModel
import uz.tikoncha_parent.presentation.task.TaskEvent
import uz.tikoncha_parent.presentation.task.TaskViewModel
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

class ProfileScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        val viewModel = koinViewModel<ProfileViewModel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val useCase: MyCoinsUseCase = koinInject()
        val coinCase: GetCoinPackagesUseCase = koinInject()
        val childCase: ChildrenUseCase = koinInject()
        val coinsViewModel = remember {
            MyCoinsViewModel(
                useCase = useCase,
                getCoinPackagesUseCase = coinCase,
                childrenUseCase = childCase
            )
        }
        LaunchedEffect(Unit) {
            coinsViewModel.load()
        }
        val ui by coinsViewModel.state.collectAsStateWithLifecycle()
        val aiTokens = ui.coins

        val taskViewModel = koinViewModel<TaskViewModel>()
        val taskState by taskViewModel.state.collectAsStateWithLifecycle()
        LaunchedEffect(Unit){
            taskViewModel.onEvent(TaskEvent.LoadAllChildrenActiveTasks)
        }
        val activeTasksCount = taskState.allChildrenActiveTaskCount

        ProfileUi(
            event = event,
            state = state.value,
            aiTokens = aiTokens?:0,
            activeTasksCount = activeTasksCount,
        )
    }
}

@Composable
fun ProfileUi(
    state: ProfileState,
    aiTokens: Int,
    activeTasksCount: Int,
    event: (ProfileEvent) -> Unit
) {

    val navigator = LocalNavigator.current
    val rootNavigator = navigator?:return

    var showQrCode by remember { mutableStateOf(false) }

    val sections = remember {
        mutableStateListOf(
            ProfileSectionItemData(
                painter = Res.drawable.profile_info,
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
                painter = Res.drawable.crown,
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
            .background(MaterialTheme.extendedColor.backgroundColor)
            .verticalScroll(rememberScrollState())
    ) {
        CustomHeader(
            showBackButton = true,
            onBackClick = {
                rootNavigator?.pop()
            },
            title = stringResource(Res.string.profil),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ContainerPadding)
        ) {
            SpaceSmall()
            ProfileHeader(
                fullName = state.userInfo?.name?:"",
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
                    title = stringResource(Res.string.tangachalaringiz),
                    value = "$aiTokens ${stringResource(Res.string.ta)}",
                    icon = painterResource(Res.drawable.coin),
                    modifier = Modifier
                        .height(ProfileStatsContainerHeight)
                        .weight(1f)
                )

                SpaceSmall()

                UserStatsItem(
                    title = stringResource(Res.string.faol_vazifalar),
                    value = "$activeTasksCount ${stringResource(Res.string.ta)}",
                    icon = painterResource(Res.drawable.file_png),
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
                    onItemClick = { section ->
                        when (section) {

                            ProfileSection.PERSONAL_INFORMATION -> {
                                navigator?.push(PersonalInformationScreen())
                            }

                            ProfileSection.LANGUAGE -> {
                                navigator?.push(LanguageScreen())
                            }

                            ProfileSection.SETTINGS -> {
                                navigator?.push(SettingsScreen())
                            }

                            ProfileSection.SUBSCRIPTIONS -> {
                                navigator?.push(SubscriptionPaymentScreen())
                            }

                            ProfileSection.COINS -> {
                                navigator?.push(CoinsScreen())
                            }
                        }
                    },
                    section = data.section
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

            if (showQrCode){
                TransparentQrScreen(
                    painter =  painter,
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
private fun PreviewProfileScreen(){
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        ProfileUi(
            state = ProfileState(),
            aiTokens = 50,
            activeTasksCount = 20,
            event = {}
        )
    }
}
