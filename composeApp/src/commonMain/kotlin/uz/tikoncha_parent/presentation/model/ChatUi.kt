package uz.tikoncha_parent.presentation.model

data class ChatUi(
    val title: String,
    val chatId: String,
    val avatar: String,
    val type: ChatType,
    val lastMessage: String,
    val unreadCount: Int,
    val dateTime: String,
    val lastMessageIsMine: Boolean = false,
    val lastMessageIsRead: Boolean = false,
)
