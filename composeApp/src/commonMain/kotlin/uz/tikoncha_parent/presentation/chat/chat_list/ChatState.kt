package uz.tikoncha_parent.presentation.chat.chat_list

import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.presentation.model.ChatUi


data class ChatState(
    val loading: Boolean = false,
    val error: Outcome.Failure? = null,
    val chats: List<ChatUi> = emptyList(),
    val hasLoadedOnce: Boolean = false,
    val isRefreshing: Boolean = false,
    val hasChild: Boolean = false,
)
