package uz.tikoncha_parent.presentation.chat_details

sealed interface ChatDetailEvent {

    data class SetDateStrings(
        val bugun: String,
        val kecha: String
    ):ChatDetailEvent
    data class SetChatData(
        val chatId: String,
        val chatAvatar: String,
        val chatTitle: String
    ): ChatDetailEvent
}