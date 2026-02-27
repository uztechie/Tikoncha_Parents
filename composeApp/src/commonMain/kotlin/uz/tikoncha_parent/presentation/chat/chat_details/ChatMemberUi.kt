package uz.saidburxon.newedu.presentation.feature.chat.chat_details

data class ChatMemberUi(
    val id: String,
    val avatar: String,
    val name: String,
    val isOnline: Boolean = false,
    val lastSeen: String = "",
)
