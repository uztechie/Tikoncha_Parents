@file:Suppress("DEPRECATION")

package uz.tikoncha_parent.presentation.add_child

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.LoadingDialog
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.presentation.base.CustomButton
import uz.tikoncha_parent.common.Util.format6DigitCode
import uz.tikoncha_parent.platform.copyPlainText
import uz.tikoncha_parent.presentation.base.CustomButtonNew
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.singleClick
import uz.tikoncha_parent.presentation.profile.children.ChildrenSelectScreen
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.presentation.video_tutorial.TutorialType
import uz.tikoncha_parent.presentation.video_tutorial.VideoTutorialScreen
import uz.tikoncha_parent.presentation.video_tutorial.VideoTutorialYoutubeScreen
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor
import uz.tikoncha_parent.ui.theme.rememberScreenSystemBars

class AddChildScreen : Screen {

    @Composable
    override fun Content() {

        val viewModel = koinScreenModel<ChildViewmodel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val navigator = LocalNavigator.current

        AddChildUi(
            navigator = navigator,
            state = state,
            event = event
        )
    }
}


@Composable
fun AddChildUi(
    navigator: Navigator?,
    state: ChildState,
    event: (ChildEvent) -> Unit
) {
    var showErrorDialog by remember { mutableStateOf(false) }

    val isLoading = state.responseState is ResponseState.Loading
    val errorText = state.responseState.errorText()
    val isSuccess = state.responseState is ResponseState.Success

    val scope = rememberCoroutineScope()
    val clipboard = LocalClipboard.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val formatted = remember(state.confirmCode) { format6DigitCode(state.confirmCode) }
    val canEditePhone by remember(state.confirmCode) { mutableStateOf(state.confirmCode.isEmpty()) }

    LoadingDialog(isLoading)
    LaunchedEffect(errorText) {
        showErrorDialog = errorText.isNotEmpty()
    }

    CustomDialog(
        painter = painterResource(Res.drawable.dialog_failed),
        show = showErrorDialog,
        title = stringResource(Res.string.xatolik),
        message = state.responseState.errorText(),
        buttonText = stringResource(Res.string.ok),
        onDismiss = {
            showErrorDialog = false
        },
        onButtonClick = {
            showErrorDialog = false
        }
    )

    CustomDialog(
        show = state.childJoined,
        buttonText = stringResource(Res.string.ok),
        title = stringResource(Res.string.muvaffaqiyatli),
        message = stringResource(Res.string.farzand_ulan_di),
        painter = painterResource(Res.drawable.dialog_success),
        onDismiss = {
            event(ChildEvent.OnSuccessDismissed)
        },
        onButtonClick = {
//            val addedPhone = state.fullNumber
            event(ChildEvent.OnSuccessDismissed)
            navigator?.replace(ChildrenSelectScreen())
//            navigator?.replace(ChildrenScreen(highlightPhone = addedPhone))
        }
    )

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            event(ChildEvent.Reset)
//            navigator?.push(ChildConfirmCodeScreen(confirmCode = state.confirmCode))
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        if (state.confirmCode.isNotEmpty() && !state.childJoined) {
            event(ChildEvent.StartWatching)
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_STOP) {
        event(ChildEvent.StopWatching)
    }

    LaunchedEffect(state.confirmCode) {
        if (state.confirmCode.isNotEmpty() && !state.childJoined) {
            event(ChildEvent.StartWatching)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            event(ChildEvent.StopWatching)
        }
    }

    var enableButton by remember { mutableStateOf(false) }
    LaunchedEffect(state.number) {
        enableButton = state.number.length >= 9
    }

    val systemBars = rememberScreenSystemBars(
        statusBarColor = AppColors.bg.secondary,
        navigationBarColor = AppColors.bg.secondary
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(systemBars.modifier)
            .imePadding()
            .background(AppColors.bg.secondary)
    ) {
        CustomHeader(
            title = stringResource(Res.string.farzand_qoshish),
            showBackButton = true,
            onBackClick = {
                navigator?.pop()
            },
            trailingIcon = {
                if (!state.showConnectChildTutorialCard){
                    IconButton(
                        onClick = {
                            navigator?.push(VideoTutorialYoutubeScreen(TutorialType.BIND_CHILD))
                        },
                        modifier = Modifier
                            .size(44.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = AppColors.bg.surfaceTertiary,
                            contentColor = AppColors.icon.accentPrimary
                        )
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.media_play),
                            contentDescription = "",
                            modifier = Modifier
                                .size(NormalIconSize)
                        )
                    }
                    SpaceUltraSmall()
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ContainerPadding)
                .verticalScroll(rememberScrollState())
        ) {


            if (state.showConnectChildTutorialCard){
                Space(12.dp)
                ConnectChildTutorialCard {
                    navigator?.push(VideoTutorialYoutubeScreen(TutorialType.BIND_CHILD))
                }
            }
            Space(16.dp)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColors.modal.primary, RoundedCornerShape(24.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(Res.string.farzandingiz_telefon_raqamini_kiriting),
                    style = AppTypography.titleSmSemiBold,
                    color = AppColors.text.primary
                )
                Space(16.dp)

                ChildPhoneInputField(
                    isAccepted = state.accept,
                    phoneNumber = state.number,
                    onPhoneNumberChange = { newNumber ->
                        if (canEditePhone) {
                            event(ChildEvent.OnNumberInsert(newNumber))
                        }
                    }
                )
            }
            SpaceLarge()
            SpaceLarge()

            if (state.confirmCode.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.extendedColor.cardColor,
                            RoundedCornerShape(TextFieldCornerRadius)
                        )
                        .height(TextFieldHeight),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.password_check),
                        contentDescription = null,
                        tint = MaterialTheme.extendedColor.hintColor,
                        modifier = Modifier
                            .size(22.dp)
                            .clickable {
                                scope.launch {
                                    copyPlainText(clipboard, formatted)
                                }
                            }
                    )
                    SpaceSmall()

                    Text(
                        text = formatted,
                        color = AppColors.text.primary,
                        style = AppTypography.headlineMdSemiBold
                    )
                }
                SpaceSmall()

                Text(
                    text = stringResource(Res.string.confirm_code_instruction),
                    textAlign = TextAlign.Center,
                    color = AppColors.text.secondary,
                    style = AppTypography.bodyLgRegular
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            if (canEditePhone) {
                CustomButtonNew(
                    onClick = {
                        event(ChildEvent.OnAddClicked)
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    },
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth()
                        .height(ButtonHeight),
                    enabled = enableButton,
                    text = stringResource(Res.string.qoshish)
                )
                SpaceLarge()
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ) {
        AddChildUi(
            navigator = null,
            state = ChildState(
                confirmCode = "123456"
            ),
            event = {}
        )
    }
}