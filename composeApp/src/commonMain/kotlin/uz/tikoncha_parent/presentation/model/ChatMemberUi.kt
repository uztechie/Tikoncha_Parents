package uz.tikoncha_parent.presentation.model

data class ChatMemberUi(
    val id: String,
    val avatar: String,
    val name: String,
    val isOnline: Boolean = false,
    val lastSeen: String = "",
)
