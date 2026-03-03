package uz.tikoncha_parent.data.repository

import kotlinx.coroutines.flow.Flow
import uz.tikoncha_parent.data.remote.ChatApiService
import uz.tikoncha_parent.data.remote.ChatSocketService
import uz.tikoncha_parent.data.remote.model.ChatListResponse
import uz.tikoncha_parent.data.remote.model.ChatMessagesResponse
import uz.tikoncha_parent.data.remote.model.ChatStatusResponse
import uz.tikoncha_parent.data.remote.model.ChatUnreadCountResponse
import uz.tikoncha_parent.data.remote.model.ChatWsEvent
import uz.tikoncha_parent.data.remote.model.SendMessageRequest
import uz.tikoncha_parent.data.remote.model.SendMessageResponse
import uz.tikoncha_parent.data.remote.model.WSSendMessage
import uz.tikoncha_parent.domain.model.chat.DeleteMessageResponse
import uz.tikoncha_parent.domain.repository.ChatRepository
class ChatRepositoryImpl(
    private val socket: ChatSocketService,
    private val api: ChatApiService
): ChatRepository {
    override suspend fun chatList(): ChatListResponse {
        return api.chatList()
    }

    override suspend fun chatMessages(params: Map<String, Any>): ChatMessagesResponse {
        return api.chatMessages(params)
    }

    override suspend fun sendMessageApi(request: SendMessageRequest): SendMessageResponse {
        return api.chatSendMessage(request)
    }

    override suspend fun chatStatus(chatId: String): ChatStatusResponse {
        return api.chatStatus(chatId)
    }

    override suspend fun chatUnreadCount(): ChatUnreadCountResponse {
        return api.chatUnreadCount()
    }

    override suspend fun deleteMessage(messageId: String): DeleteMessageResponse {
        return api.deleteMessage(messageId)
    }

    override fun connect() {
        socket.connect()
    }

    override fun disconnect() {
        socket.disconnect()
    }

    override fun observeEvents(): Flow<ChatWsEvent> {
        return socket.events
    }

    override suspend fun sendMessage(
        message: WSSendMessage
    ) {
        socket.sendMessage(message)
    }

    override suspend fun editMessage(messageId: String, newText: String) {
        socket.editMessage(messageId, newText)
    }

    override suspend fun markRead(chatId: String, messageId: String) {
        socket.markRead(chatId, messageId)
    }

    override suspend fun markUnread(chatId: String, messageId: String) {
        socket.markUnread(chatId, messageId)
    }


}