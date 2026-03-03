package uz.tikoncha_parent.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.tikoncha_parent.data.remote.model.ChatListResponse
import uz.tikoncha_parent.data.remote.model.ChatMessagesResponse
import uz.tikoncha_parent.data.remote.model.ChatStatusResponse
import uz.tikoncha_parent.data.remote.model.ChatUnreadCountResponse
import uz.tikoncha_parent.data.remote.model.ChatWsEvent
import uz.tikoncha_parent.data.remote.model.SendMessageRequest
import uz.tikoncha_parent.data.remote.model.SendMessageResponse
import uz.tikoncha_parent.data.remote.model.WSSendMessage
import uz.tikoncha_parent.domain.model.chat.DeleteMessageResponse


interface ChatRepository {

    //http API
    suspend fun chatList(): ChatListResponse
    suspend fun chatMessages(params: Map<String, Any>): ChatMessagesResponse
    suspend fun sendMessageApi(request: SendMessageRequest): SendMessageResponse

    suspend fun chatStatus(chatId: String): ChatStatusResponse

    suspend fun chatUnreadCount(): ChatUnreadCountResponse

    suspend fun deleteMessage(messageId: String): DeleteMessageResponse

    // WS control
    fun connect()
    fun disconnect()

    // WS events
    fun observeEvents(): Flow<ChatWsEvent>

    // WS actions
    suspend fun sendMessage(message: WSSendMessage)
    suspend fun editMessage(messageId: String, newText: String)
    suspend fun markRead(chatId: String, messageId: String)
    suspend fun markUnread(chatId: String, messageId: String)
}