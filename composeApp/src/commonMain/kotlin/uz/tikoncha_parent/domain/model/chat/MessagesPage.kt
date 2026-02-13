package uz.tikoncha_parent.domain.model.chat

import uz.tikoncha_parent.data.remote.model.ChatMessageDto


data class MessagesPage(
    val items: List<ChatMessageDto>,
    val sinceId: String? = null,
    val sinceTs: String? = null,
    val hasMore: Boolean
)
