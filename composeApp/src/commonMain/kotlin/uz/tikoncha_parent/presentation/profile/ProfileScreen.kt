package uz.tikoncha_parent.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import uz.tikoncha_parent.data.mapper.toUploadPart
import uz.tikoncha_parent.platform.decodeImageBitmapOrNull
import uz.tikoncha_parent.platform.rememberImagePicker
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.common.TransparentQrScreen
import uz.tikoncha_parent.presentation.profile.coins.CoinsScreen
import uz.tikoncha_parent.presentation.profile.personal_information.PersonalInformationScreen
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import qrgenerator.qrkitpainter.rememberQrKitPainter
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.domain.use_case.ChildrenUseCase
import uz.tikoncha_parent.domain.use_case.payment.GetCoinPackageListUseCase
import uz.tikoncha_parent.domain.use_case.chat.GetMyCoinsUseCase
import uz.tikoncha_parent.platform.getAppVersion
import uz.tikoncha_parent.presentation.add_child.AddChildScreen
import uz.tikoncha_parent.presentation.base.CustomBottomDialog
import uz.tikoncha_parent.presentation.base.CustomButtonDash
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.LoadingDialog
import uz.tikoncha_parent.presentation.login.LoginScreen
import uz.tikoncha_parent.presentation.profile.children.ChildrenScreen
import uz.tikoncha_parent.presentation.profile.coins.CoinsViewModel
import uz.tikoncha_parent.presentation.profile.language.LanguageScreen
import uz.tikoncha_parent.presentation.profile.settings.SettingsScreen
import uz.tikoncha_parent.presentation.profile.subscription.subscription_payment.SubscriptionPaymentScreen
import uz.tikoncha_parent.presentation.task.TaskEvent
import uz.tikoncha_parent.presentation.task.TaskScreen
import uz.tikoncha_parent.presentation.task.TaskViewModel
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor
import uz.tikoncha_parent.ui.theme.rememberScreenSystemBars

class ProfileScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        val viewModel = koinScreenModel<ProfileViewModel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val useCase: GetMyCoinsUseCase = koinInject()
        val coinCase: GetCoinPackageListUseCase = koinInject()
        val childCase: ChildrenUseCase = koinInject()
        val coinsViewModel = remember {
            CoinsViewModel(
                getMyCoinsUseCase = useCase,
                coinsPackageListUseCase = coinCase,
                childrenUseCase = childCase
            )
        }
        LaunchedEffect(Unit) {
            coinsViewModel.load()
        }
        val ui by coinsViewModel.state.collectAsStateWithLifecycle()
        val aiTokens = ui.myCoins

        val taskViewModel = koinScreenModel<TaskViewModel>()
        val taskState by taskViewModel.state.collectAsStateWithLifecycle()
        LaunchedEffect(Unit) {
            taskViewModel.onEvent(TaskEvent.LoadAllChildrenActiveTasks)
        }
        val activeTasksCount = taskState.allChildrenActiveTaskCount

        LaunchedEffect(Unit) {
            event(ProfileEvent.LoadAvatarFromServer)
        }

        ProfileUi(
            navigator = navigator,
            event = event,
            state = state.value,
            aiTokens = aiTokens ?: 0,
            activeTasksCount = activeTasksCount,
        )
    }
}

@Composable
fun ProfileUi(
    navigator: Navigator?,
    state: ProfileState,
    aiTokens: Int,
    activeTasksCount: Int,
    event: (ProfileEvent) -> Unit
) {
    var showQrCode by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showLogoutErrorDialog by remember { mutableStateOf(false) }

    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    val launchPicker = rememberImagePicker { picked ->
        val bitmap = decodeImageBitmapOrNull(picked.bytes)
        imageBitmap = bitmap
        event(ProfileEvent.OnAvatarPreviewSelected(bitmap))
        event(ProfileEvent.OnAvatarPhotoSelected(picked.toUploadPart("avatar.jpg")))
    }

    val logoutLoading = state.logoutState is ResponseState.Loading
    val logoutError = state.logoutState.errorText()
    val logoutSuccess = state.logoutState is ResponseState.Success

    LaunchedEffect(logoutError){
        if (logoutError.isNotEmpty()){
            showLogoutErrorDialog = true
        }
    }

    LaunchedEffect(logoutSuccess){
        if (logoutSuccess){
            navigator?.replaceAll(LoginScreen())
        }
    }



    LoadingDialog(
        logoutLoading
    )

    CustomDialog(
        show = showLogoutErrorDialog,
        title = stringResource(Res.string.xatolik),
        message = logoutError,
        buttonText = stringResource(Res.string.ok),
        painter = painterResource(Res.drawable.dialog_failed),
        onDismiss = {
            event(ProfileEvent.Clear)
            showLogoutErrorDialog = false
        },
        onButtonClick = {
            event(ProfileEvent.Clear)
            showLogoutErrorDialog = false
        }
    )

    val painter = rememberQrKitPainter(data = "There will be url or smth like this")

    if (showQrCode) {
        TransparentQrScreen(
            painter = painter,
            onDismissRequest = {
                showQrCode = false
            }
        )
    }

    CustomBottomDialog(
        show = showLogoutDialog,
        title = stringResource(Res.string.chiqishni_xohlaysizmi),
        message = stringResource(Res.string.hisobdan_chiqishni_tasdiqlaysizmi),
        showCancelButton = true,
        confirmButtonText = stringResource(Res.string.chiqish),
        dismissButtonText = stringResource(Res.string.bekor_qilish),
        confirmButtonColor = AppColors.button.accentDanger,
        onConfirm = {
            showLogoutDialog = true
            event(ProfileEvent.RequestLogout)
        },
        onDismiss = {showLogoutDialog = false}
    )

    val systemBars = rememberScreenSystemBars(
        statusBarColor = AppColors.bg.page,
        navigationBarColor = AppColors.bg.page
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(systemBars.modifier)
            .background(AppColors.bg.page)
    ) {
        CustomHeader(
            showBackButton = true,
            title = stringResource(Res.string.profil),
            onBackClick = {
                navigator?.pop()
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ContainerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(16.dp))

            ProfileHeader(
                state = state,
                firstName = state.userInfo?.name ?: "",
                lastName = state.userInfo?.lastName ?: "",
                fathersName = state.userInfo?.patronymic ?: "",
                onSelectImageButtonClick = {
                    launchPicker()
                }
            )
            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                UserStatsItem(
                    icon = painterResource(Res.drawable.coin_3d),
                    title = stringResource(Res.string.tangachalaringiz),
                    value = "$aiTokens ${stringResource(Res.string.ta)}",
                    modifier = Modifier
                        .height(ProfileStatsContainerHeight)
                        .weight(1f),
                    onClick = {
                        navigator?.push(CoinsScreen())
                    }
                )
                Spacer(Modifier.width(12.dp))

                UserStatsItem(
                    icon = painterResource(Res.drawable.file_3d),
                    title = stringResource(Res.string.faol_vazifalar),
                    value = "$activeTasksCount ${stringResource(Res.string.ta)}",
                    modifier = Modifier
                        .height(ProfileStatsContainerHeight)
                        .weight(1f),
                    onClick = {
                        navigator?.push(TaskScreen())
                    }
                )
            }
            Spacer(Modifier.height(12.dp))

            CustomButtonDash(
                text = stringResource(Res.string.farzand_qo_shish),
                modifier = Modifier.fillMaxWidth(),
                onClick = { navigator?.push(AddChildScreen()) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.add),
                        contentDescription = "",
                        tint = AppColors.icon.accentPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            )
            Spacer(Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColors.bg.surface, RoundedCornerShape(24.dp))
                    .padding(horizontal = 8.dp)
            ) {
                ProfileSectionItem(
                    title = stringResource(Res.string.shaxsiy_malumotlar),
                    icon = painterResource(Res.drawable.person),
                    onItemClick = {
                        navigator?.push(PersonalInformationScreen())
                    }
                )

                ProfileSectionItem(
                    title = stringResource(Res.string.farzandlaringiz),
                    icon = painterResource(Res.drawable.person),
                    onItemClick = {
                        navigator?.push(ChildrenScreen())
                    }
                )

                ProfileSectionItem(
                    title = stringResource(Res.string.sozlamalar),
                    icon = painterResource(Res.drawable.settings),
                    onItemClick = {
                        navigator?.push(SettingsScreen())
                    }
                )

                ProfileSectionItem(
                    title = stringResource(Res.string.til),
                    icon = painterResource(Res.drawable.global),
                    onItemClick = {
                        navigator?.push(LanguageScreen())
                    }
                )

                ProfileSectionItem(
                    title = stringResource(Res.string.obuna),
                    icon = painterResource(Res.drawable.telegrams_star),
                    onItemClick = {
                        navigator?.push(SubscriptionPaymentScreen())
                    }
                )

                ProfileSectionItem(
                    title = stringResource(Res.string.tangachalar),
                    icon = painterResource(Res.drawable.coins_profile),
                    onItemClick = {
                        navigator?.push(CoinsScreen())
                    }
                )

                ProfileSectionItem(
                    title = stringResource(Res.string.biz_haqimizda),
                    icon = painterResource(Res.drawable.info_profile_us),
                    onItemClick = { }
                )

                ProfileSectionItem(
                    divider = false,
                    title = stringResource(Res.string.chiqish),
                    icon = painterResource(Res.drawable.logout),
                    onItemClick = {
                        showLogoutDialog = true
                    }
                )
            }

            SpaceMedium()
            val appVersion = if (LocalInspectionMode.current) {
                "1.0.0"
            } else {
                getAppVersion()
            }
            Text(
                text = "${stringResource(Res.string.versiya)}: $appVersion",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.extendedColor.textColor,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            SpaceMedium()
        }
    }
}

@Preview
@Composable
private fun PreviewProfileScreen() {
    TikonchaParentTheme(
        ThemeMode.LIGHT
    ) {
        ProfileUi(
            navigator = null,
            state = ProfileState(),
            aiTokens = 50,
            activeTasksCount = 20,
            event = {}
        )
    }
}
