package uz.tikoncha_parent.domain.model

import uz.tikoncha_parent.presentation.chat.model.ChatDateLabel
import uz.tikoncha_parent.presentation.model.ChatMessageUi

sealed class ChatMessageItem {
    data class DateHeader(
        val epochDay: Long,
        val dateLabel: ChatDateLabel
    ): ChatMessageItem()
    data class Message(val chatMessageUi: ChatMessageUi): ChatMessageItem()
}