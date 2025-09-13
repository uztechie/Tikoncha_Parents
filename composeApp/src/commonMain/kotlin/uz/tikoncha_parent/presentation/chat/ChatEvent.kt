package uz.tikoncha_parent.presentation.chat

import uz.tikoncha_parent.presentation.model.ChatType

sealed interface ChatEvent {

    data class Open(val screen: String): ChatEvent
    data class Close(val screen: String): ChatEvent
    data object GetChatList: ChatEvent
    data object GetChatMessages: ChatEvent
    data class OnMessageChange(val message: String): ChatEvent
    data object SendMessage: ChatEvent
    data object OnScrollLastMessage: ChatEvent

    data class SetChatData(
        val chatId: String,
        val chatTitle: String,
        val chatAvatar: String,
        val chatType: ChatType,
        val bugun: String,
        val kecha: String
    ): ChatEvent
}