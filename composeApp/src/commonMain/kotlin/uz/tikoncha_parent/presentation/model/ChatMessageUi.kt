package uz.tikoncha_parent.presentation.model

import uz.tikoncha_parent.presentation.chat.model.DeliveryStatus

data class ChatMessageUi(
    val id: String,
    val isMine: Boolean,
    val message: String,
    val senderName: String,
    val senderAvatar: String,
    val createdAt: Long,
    val updatedAt: Long? = null,
    val time: String,
    val isRead: Boolean = false,
    val amplitudes: List<Float> = emptyList(),
    val duration: Long? = null,
    val progress: Int = 0,
    val clientMsgId: String? = null,
    val replyToId: String? = null,
    val repliedMessageOwner: String? = null,
    val repliedMessageText: String? = null,
    val status: DeliveryStatus = DeliveryStatus.SENT,

    val messageType: ChatMessageType = ChatMessageType.TEXT,
)
