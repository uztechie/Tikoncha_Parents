package uz.saidburxon.newedu.domain.model

data class ChatMessage(
    val id: Long,
    val isMine: Boolean,
    val message: String,
    val createdAt: Long,
    val time: String,
)
