package uz.tikoncha_parent.presentation.chat.chat_details

import uz.tikoncha_parent.presentation.model.ChatMemberUi
import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class ChatDetailState(
    val bugun: String = "",
    val kecha: String = "",
    val chatId: String = "",
    val chatAvatar: String = "",
    val chatTitle: String = "",
    val memberCount: Int = 0,
    val members: List<ChatMemberUi> = emptyList(),
    val responseState: ResponseState<Nothing> = ResponseState.Idle
)
