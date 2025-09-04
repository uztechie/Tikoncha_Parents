package uz.saidburxon.newedu.domain.model

sealed class ChatMessageItem {
    data class DateHeader(val date: String): ChatMessageItem()
    data class Message(val chatMessage: ChatMessage): ChatMessageItem()
}