package uz.tikoncha_parent.presentation.model

data class ChatMessageUi(
    val id: String,
    val isMine: Boolean,
    val message: String,
    val senderName: String,
    val senderAvatar: String,
    val createdAt: Long,
    val time: String,
    val isRead: Boolean = false,
)
