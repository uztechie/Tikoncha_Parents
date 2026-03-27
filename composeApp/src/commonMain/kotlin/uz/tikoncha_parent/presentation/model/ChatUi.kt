package uz.tikoncha_parent.presentation.model

import androidx.compose.runtime.Immutable
import uz.tikoncha_parent.presentation.chat.model.ChatDateLabel

@Immutable
data class ChatUi(
    val title: String,
    val chatId: String,
    val avatar: String,
    val type: ChatType,
    val lastMessage: String,
    val unreadCount: Int,
    val dateTime: ChatDateLabel,
    val lastMessageIsMine: Boolean = false,
    val lastMessageIsRead: Boolean = false,
){
    override fun toString(): String {
        return title
    }
}
