package uz.tikoncha_parent.domain.model.chat

import uz.tikoncha_parent.presentation.model.ChatType


data class Chat(
    val id: String,
    val type: ChatType,
    val title: String,
    val avatar: String?,
    val lastMessageText: String?,
    val lastMessageCreatedAt: String?, // hozircha String qolsin (keyin millis qilamiz)
    val lastMessageIsMine: Boolean,
    val lastMessageIsRead: Boolean,
    val unreadCount: Int
)