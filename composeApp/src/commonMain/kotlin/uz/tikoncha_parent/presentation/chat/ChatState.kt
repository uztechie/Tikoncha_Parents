package uz.tikoncha_parent.presentation.chat

import uz.tikoncha_parent.domain.model.ChatMessageItem
import uz.tikoncha_parent.presentation.model.ChatMessageUi
import uz.tikoncha_parent.presentation.model.ChatType
import uz.tikoncha_parent.presentation.model.ChatUi
import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class ChatState(
    val allMessages: List<ChatMessageUi> = emptyList(),
    val messages: List<ChatMessageItem> = emptyList(),
    val message: String = "",
    val chatList: List<ChatUi> = emptyList(),

    val chatType: ChatType = ChatType.PARENT_CHILD,
    val chatId: String = "",
    val chatTitle: String = "",
    val chatAvatar: String = "",
    val chatMembersCount: Int = 0,
    val isUserOnline: Boolean = false,
    val lastTimeOnline: String = "",
    val bugun: String = "",
    val kecha: String = "",
    val lastMessage: ChatMessageUi? = null,

    val chatListResponseState:ResponseState<Nothing> = ResponseState.Idle,

    val chatMessagesResponseState:ResponseState<Nothing> = ResponseState.Idle
)
