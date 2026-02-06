package uz.tikoncha_parent.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.dialog_failed
import tikoncha_parents.composeapp.generated.resources.ok
import tikoncha_parents.composeapp.generated.resources.suhbat
import tikoncha_parents.composeapp.generated.resources.xatolik
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.model.ChatType
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.DividerHorizontal
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor


class ChatScreen: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current?:return

        val viewModel = koinScreenModel<ChatViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        ChatUi(
            navigator = navigator,
            state = state,
            event = event
        )
    }
}


@Composable
fun ChatUi(
    navigator: Navigator?,
    state: ChatState,
    event: (ChatEvent) -> Unit,
) {

    LaunchedEffect(true) {
        ChatUnreadEventBus.tryEmit(
            ChatUnreadEventBus.ChatUnreadEvent.MessageReceived(state.lastMessage?.id?:"")
        )
    }

    DisposableEffect(Unit) {
       event(ChatEvent.Open("ChatScreen"))
        onDispose {
            event(ChatEvent.Close("ChatScreen"))
        }
    }


    LaunchedEffect(Unit) {
        event(ChatEvent.GetChatList)
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    val chatListLoading = state.chatListResponseState is ResponseState.Loading
    val chatListErrorText = state.chatListResponseState.errorText()
    val chatListSuccess = state.chatListResponseState is ResponseState.Success



    LaunchedEffect(chatListErrorText) {
        showDialog = chatListErrorText.isNotEmpty()
    }

    CustomDialog(
        painter = painterResource(Res.drawable.dialog_failed),
        show = showDialog,
        title = stringResource(Res.string.xatolik),
        message = chatListErrorText,
        buttonText = stringResource(Res.string.ok),
        showCloseButton = false,
        onDismiss = {
            showDialog = false
        },
        onButtonClick = {
            showDialog = false
        }
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {

        CustomHeader(
            showBackButton = true,
            onBackClick = {
                navigator?.pop()
            },
            title = stringResource(Res.string.suhbat),
        )

        Column(
            modifier = Modifier.padding(horizontal = ContainerPadding)
        )
        {
            LazyColumn {

                items(state.chatList){ item->
                    ChatListItem(
                        chatUi = item,
                        onClick = {

                            if(item.type == ChatType.BOT){
                                navigator?.push(
                                    ChatMessageAiScreen(
                                        chatId = item.chatId,
                                        chatTitle = item.title,
                                    )
                                )
                            }
                            else{
                                navigator?.push(
                                    ChatMessageScreen(
                                        chatId = item.chatId,
                                        chatAvatar = item.avatar,
                                        chatTitle = item.title,
                                        chatType = item.type
                                    )
                                )
                            }
                        }
                    )
                    DividerHorizontal()
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(mode = ThemeMode.LIGHT) {
        ChatUi(
            navigator = null,
            state = ChatState(),
            event = {}
        )
    }
}