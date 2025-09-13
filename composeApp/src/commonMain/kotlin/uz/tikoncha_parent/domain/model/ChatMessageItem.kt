package uz.tikoncha_parent.domain.model

import uz.tikoncha_parent.presentation.model.ChatMessageUi

sealed class ChatMessageItem {
    data class DateHeader(val date: String): ChatMessageItem()
    data class Message(val chatMessageUi: ChatMessageUi): ChatMessageItem()
}