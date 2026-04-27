package uz.tikoncha_parent.presentation.chat.chat_list

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.common.DateTimeUtil
import uz.tikoncha_parent.data.mapper.toChatUi
import uz.tikoncha_parent.data.remote.model.ChatMessageDto
import uz.tikoncha_parent.data.remote.model.ChatWsEvent
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.chat.GetChatListFromServerUseCase
import uz.tikoncha_parent.domain.use_case.chat.ObserveChatEventUseCase
import uz.tikoncha_parent.presentation.chat.ChatConnectionManager
import uz.tikoncha_parent.presentation.chat.ChatDateTimeUtil
import uz.tikoncha_parent.presentation.profile.language.LanguagePrefs
import kotlin.collections.map

class ChatViewModel(
    private val chatListUseCase: GetChatListFromServerUseCase,
    private val observeEvents: ObserveChatEventUseCase,
    private val connectionManager: ChatConnectionManager
) : ScreenModel {

    private var chatListJob: Job? = null
    private var eventsJob: Job? = null
    val langType = LanguagePrefs.loadOrDefault()
    private val _state = MutableStateFlow(ChatState())
    val state = _state.asStateFlow()

    fun onEvent(event: ChatEvent) {
        when (event) {
            ChatEvent.Refresh -> {
                getChatList(isRefresh = true)
            }

            is ChatEvent.OnScreenOpened -> {
                connectionManager.acquire(event.screen)
                startObserveEvents()
                getChatList(isRefresh = false)
            }

            is ChatEvent.OnScreenClosed -> {
                connectionManager.release(event.screen)
            }

            ChatEvent.ClearError -> {
                _state.update {
                    it.copy(
                        error = null
                    )
                }
            }
        }
    }

    private fun startObserveEvents() {
        if (eventsJob?.isActive == true) return
        eventsJob = screenModelScope.launch {
            observeEvents().collect { ev ->
                when (ev) {
                    is ChatWsEvent.MessageCreated -> applyMessageCreated(ev.message)
                    is ChatWsEvent.ReadUpdate -> applyReadUpdate(ev.chatId, ev.messageId)
                    else -> Unit
                }
            }
        }
    }


    private fun getChatList(isRefresh: Boolean) {

        _state.update {
            if (isRefresh) {
                it.copy(
                    isRefreshing = true,
                    error = ""
                )
            }
            else {
                it.copy(
                    loading = true,
                    error = ""
                )
            }
        }

        chatListJob?.cancel()
        chatListJob = screenModelScope.launch {
            when (val result = chatListUseCase.invoke()) {
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            loading = false,
                            isRefreshing = false,
                            hasLoadedOnce = true,
                            error = result.message
                        )
                    }
                }

                is Resource.Success -> {
                    val chats = result.data?.map { it.toChatUi() }.orEmpty()
                    _state.update {
                        it.copy(
                            error = "",
                            chats = chats,
                            loading = false,
                            hasLoadedOnce = true,
                            isRefreshing = false
                        )
                    }
                }
            }
        }
    }

    private fun applyMessageCreated(dto: ChatMessageDto) {
        _state.update { s ->
            val updated = s.chats.map { chat ->
                if (chat.chatId != dto.chat_id) return@map chat

                val isMine = dto.is_mine == true
                val isRead = dto.is_read == true
                val newUnread = if (isMine) chat.unreadCount else chat.unreadCount + 1

                chat.copy(
                    lastMessage = dto.text,
                    lastMessageIsMine = isMine,
                    lastMessageIsRead = isRead,
                    unreadCount = newUnread,
                    dateTime = runCatching {
                        val millis = DateTimeUtil.toMillis(dto.created_at)
                        ChatDateTimeUtil.formatChatDate(millis = millis, langType = langType)
                    }.getOrDefault(chat.dateTime)
                )
            }
            s.copy(chats = updated)
        }
    }

    private fun applyReadUpdate(chatId: String, messageId: String) {
        // ChatList uchun odatda: unread_count=0 qilish yoki lastMessageIsRead=true qilish.
        // Backend read_update faqat bitta messageId bo‘lsa ham,
        // chatList’da "o‘qildi" belgisi last message uchun bo‘lsa yangilaymiz:
        _state.update { s ->
            s.copy(
                chats = s.chats.map { chat ->
                    if (chat.chatId == chatId) chat.copy(lastMessageIsRead = true) else chat
                }
            )
        }
    }
}