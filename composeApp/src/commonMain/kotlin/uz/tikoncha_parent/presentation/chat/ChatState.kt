package uz.saidburxon.newedu.presentation.feature.chat

import uz.saidburxon.newedu.domain.model.ChatMessage
import uz.saidburxon.newedu.domain.model.ChatMessageItem

data class ChatState(
    val allMessages: List<ChatMessage> = emptyList(),
    val messages: List<ChatMessageItem> = emptyList(),
    val message: String = "",
    val isLoading: Boolean = false,
)
