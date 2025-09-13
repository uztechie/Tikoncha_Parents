package uz.tikoncha_parent.presentation.monitoring

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.CustomMultiLineTextField
import uz.tikoncha_parent.presentation.base.CustomSelectionButton
import uz.tikoncha_parent.presentation.base.DividedButton
import uz.tikoncha_parent.presentation.chat.ChatScreen
import uz.tikoncha_parent.presentation.common.CustomListDialog
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.CustomLoadingButton
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.theme.extendedColor


class MonitorScreen : Screen {
    @Composable
    override fun Content() {

        val viewModel = koinViewModel<MonitorViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        MonitorUi(
            state = state,
            event = event
        )
    }
}


val items = listOf<String>("Alijonov Karimjon", "Alijonova Gulmira")

@Preview
@Composable
private fun MonitorUi(
    state: MonitorState,
    event: (MonitorEvent) -> Unit
) {

    val mainNavigator = LocalNavigator.current
    val rootNavigator = mainNavigator?.parent



    var showDialog by remember {
        mutableStateOf(false)
    }

    val sendMessageLoading = state.sendMessageResponseState is ResponseState.Loading
    val sendMessageErrorText = state.sendMessageResponseState.errorText()
    val sendMessageSuccess = state.sendMessageResponseState is ResponseState.Success

    val chatListLoading = state.chatListResponseState is ResponseState.Loading
    val chatListErrorText = state.chatListResponseState.errorText()
    val chatListSuccess = state.chatListResponseState is ResponseState.Success

    var showSendMessageError by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(sendMessageErrorText) {
        if (sendMessageErrorText.isNotEmpty()) {
            showSendMessageError = true
        }
    }


    CustomDialog(
        onDismiss = {showSendMessageError = false},
        show = showSendMessageError,
        title = stringResource(Res.string.xatolik),
        message = sendMessageErrorText,
        onButtonClick = {
            showSendMessageError = false
        }
    )




    CustomListDialog(
        title = stringResource(Res.string.farzandlaringiz),
        items = state.chatList,
        show = showDialog,
        onItemSelected = {
            event(MonitorEvent.SelectChild(it))
        },
        onDismiss = {
            showDialog = false
        },
        loading = chatListLoading,
        errorMessage = chatListErrorText
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {

        CustomHeader(
            title = stringResource(Res.string.kuzatuv)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(ContainerPadding)
                    .verticalScroll(rememberScrollState())
                    .align(Alignment.TopStart)
            )
            {
                CustomText(
                    text = stringResource(Res.string.farzandingiz),
                    fontSize = SmallTextSize,
                )

                SpaceUltraSmall()

                CustomSelectionButton(
                    onClick = {
                        showDialog = true
                    },
                    label = stringResource(Res.string.tanlang),
                    text = state.selectedChild?.title?:"",
                    painter = painterResource(Res.drawable.person)
                )

                SpaceLarge()
                SpaceLarge()

                DividedButton(
                    title = stringResource(Res.string.ekranni_kuzatish),
                    icon = painterResource(Res.drawable.monitor_screen),
                    onItemClick = {},
                    isPermission = false
                )

                SpaceSmall()

                DividedButton(
                    title = stringResource(Res.string.yon_atrofni_kuzatish),
                    icon = painterResource(Res.drawable.permission_camera),
                    onItemClick = {},
                    isPermission = false
                )

                SpaceSmall()

                DividedButton(
                    title = stringResource(Res.string.yon_atrofni_eshitish),
                    icon = painterResource(Res.drawable.microphonee),
                    onItemClick = {},
                    isPermission = false
                )

                SpaceSmall()

                DividedButton(
                    title = stringResource(Res.string.bolaning_ilovasini_sozligini_korish),
                    icon = painterResource(Res.drawable.permission_adminstration),
                    onItemClick = {
                        rootNavigator?.push(ClientPermissionStateScreen())
                    },
                    isPermission = false
                )

                SpaceSmall()

                DividedButton(
                    title = stringResource(Res.string.farzandingiz_bilan_suhbat),
                    icon = painterResource(Res.drawable.dialogg),
                    onItemClick = {
                        rootNavigator?.push(ChatScreen())
                    },
                    isPermission = false
                )
                SpaceSmall()

                DividedButton(
                    title = stringResource(Res.string.internetdagi_tarix),
                    icon = painterResource(Res.drawable.clock),
                    onItemClick = {

                    },
                    isPermission = false
                )

                SpaceLarge()
                SpaceLarge()
                CustomMultiLineTextField(
                    value = state.message,
                    onValueChange = {
                        event(MonitorEvent.OnMessageChange(it))
                    },
                    hasBorder = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(TextFieldHeight, TextFieldHeight * 5),
                    singleLine = false,
                    label = stringResource(Res.string.xabar_yuborish),
                )

                SpaceMedium()

                CustomLoadingButton(
                    text = stringResource(Res.string.yuborish),
                    onClick = {
                        event(MonitorEvent.OnSendMessageClick)
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    loading = sendMessageLoading,
                    enabled = !sendMessageLoading
                )
                SpaceLarge()
                SpaceLarge()
            }


        }
    }
}
