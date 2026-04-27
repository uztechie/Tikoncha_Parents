package uz.tikoncha_parent.presentation.chat.chat_list

import uz.tikoncha_parent.presentation.model.ChatUi


data class ChatState(
    val loading: Boolean = false,
    val error: String? = null,
    val chats: List<ChatUi> = emptyList(),
    val hasLoadedOnce: Boolean = false,
    val isRefreshing: Boolean = false,
)
