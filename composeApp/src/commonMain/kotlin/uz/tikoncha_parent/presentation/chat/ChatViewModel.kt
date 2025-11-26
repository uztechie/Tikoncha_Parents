@file:OptIn(ExperimentalTime::class)

package uz.tikoncha_parent.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import uz.tikoncha_parent.domain.model.ChatMessageItem
import uz.tikoncha_parent.common.DateTimeUtil
import uz.tikoncha_parent.data.mapper.toChatMessageUi
import uz.tikoncha_parent.data.mapper.toChatUi
import uz.tikoncha_parent.data.remote.model.ChatWsEvent
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.chat.ChatStatusUseCase
import uz.tikoncha_parent.domain.use_case.chat.GetChatListFromServerUseCase
import uz.tikoncha_parent.domain.use_case.chat.GetChatMessagesFromServerUseCase
import uz.tikoncha_parent.domain.use_case.chat.MarkReadUseCase
import uz.tikoncha_parent.domain.use_case.chat.ObserveChatEventUseCase
import uz.tikoncha_parent.domain.use_case.chat.SendMessageUseCase
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.presentation.domain.model.LanguageType
import uz.tikoncha_parent.presentation.profile.language.LanguagePrefs
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


class ChatViewModel(
    private val sendMessageUseCase: SendMessageUseCase,
    private val observeChatEventsUseCase: ObserveChatEventUseCase,
    private val getChatListFromServerUseCase: GetChatListFromServerUseCase,
    private val getChatMessagesFromServerUseCase: GetChatMessagesFromServerUseCase,
    private val readMessageUseCase: MarkReadUseCase,
    private val chatStatusUseCase: ChatStatusUseCase,
    private val connectionManager: ChatConnectionManager,
) : ViewModel() {

    private val TAG = "ChatViewModel"

    private var sendMessageJob: Job? = null
    private var readMessageJob: Job? = null
    private var chatListJob: Job? = null
    private var chatMessagesJob: Job? = null
    private var chatStatusJob: Job? = null



    private val _state = MutableStateFlow(ChatState())
    val state = _state.asStateFlow()

    init {
        observeChatEvents()
    }


    private fun observeChatEvents(){
        viewModelScope.launch {
            observeChatEventsUseCase().collectLatest {event->
                when(event){
                    is ChatWsEvent.MessageCreated -> {
                        Logger.d(TAG, "observeChatEvents: created= ${event.message.text}",)

                        val messageUi = event.message.toChatMessageUi()



                        _state.update { scopedState->
                            scopedState.copy(
                                allMessages = scopedState.allMessages+messageUi,
                                chatList = scopedState.chatList.map {
                                    if (it.chatId == event.message.chat_id){
                                        it.copy(
                                            lastMessage = messageUi.message,
                                            lastMessageIsMine = messageUi.isMine,
                                            unreadCount = it.unreadCount+1,
                                            lastMessageIsRead = messageUi.isRead
                                        )
                                    }
                                    else{
                                        it
                                    }

                                }
                            )
                        }
                        groupMessageByDate()
                        markMessageAsRead()
                    }
                    is ChatWsEvent.MessageUpdated -> {

                    }
                    is ChatWsEvent.ReadUpdate -> {


                        Logger.d(TAG, "observeChatEvents: readUpdate= ${event.messageId}",)
                        _state.update {
                            it.copy(
                                allMessages = it.allMessages.map { message->
                                    if (message.id == event.messageId){
                                        message.copy(isRead = true)
                                    }else{
                                        message
                                    }
                                }
                            )
                        }
                        markAllMessagesReadLocally()

                    }
                    is ChatWsEvent.Error -> {
                        Logger.e(TAG, "observeChatEvents: error= ${event.error}",)
                    }
                    is ChatWsEvent.Pong -> {
                        Logger.d(TAG, "observeChatEvents: Pong",)
                    }
                    else -> Unit
                }
            }
        }
    }

    fun onEvent(event: ChatEvent) {
        when (event) {



            is ChatEvent.Open -> {
                connectionManager.acquire(event.screen)
            }
            is ChatEvent.Close -> {
                connectionManager.release(event.screen)
            }
            ChatEvent.GetChatList -> {
                getChatList()
            }

            is ChatEvent.OnMessageChange -> {
                _state.update {
                    it.copy(
                        message = event.message
                    )
                }
            }
            ChatEvent.GetChatMessages -> {}
            ChatEvent.OnScrollLastMessage -> {
                markMessageAsRead()
            }
            ChatEvent.SendMessage -> {
                sendMessage()
            }
            is ChatEvent.SetChatData -> {
                _state.update {
                    it.copy(
                        chatTitle = event.chatTitle,
                        chatId = event.chatId,
                        chatAvatar = event.chatAvatar,
                        chatType = event.chatType,
                        bugun = event.bugun,
                        kecha = event.kecha
                    )
                }

                getChatMessages()
                getChatStatus()
            }
        }
    }

    private fun sendMessage(){
        sendMessageJob?.cancel()
        sendMessageJob = viewModelScope.launch {
            val message = state.value.message.trim()
            val result = sendMessageUseCase.invoke(
                chatId = state.value.chatId,
                text = message
            )
            when(result){
                is Resource.Error -> {
                    clearMessageText()
                }
                is Resource.Loading -> {
                    clearMessageText()
                }
                is Resource.Success -> {
                    clearMessageText()
                }
            }

        }
    }

    private fun getChatList(){

        _state.update {
            it.copy(
                chatListResponseState = ResponseState.Loading,
            )
        }

        chatListJob?.cancel()
        chatListJob = viewModelScope.launch {
            val result = getChatListFromServerUseCase.invoke()
            when(result){
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            chatListResponseState = ResponseState.Error(
                                res = result.resId,
                                message = result.message
                            ),
                        )
                    }
                }
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            chatListResponseState = ResponseState.Success(),
                            chatList = result.data.map {chat-> chat.toChatUi() }
                        )
                    }
                }
            }
        }
    }
    private fun getChatMessages(){

        _state.update {
            it.copy(
                chatMessagesResponseState = ResponseState.Loading,
            )
        }

        chatMessagesJob?.cancel()
        chatMessagesJob = viewModelScope.launch {
            val chatId = state.value.chatId
            val result = getChatMessagesFromServerUseCase.invoke(chatId)
            when(result){
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            chatMessagesResponseState = ResponseState.Error(
                                res = result.resId,
                                message = result.message
                            )
                        )
                    }
                }
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            chatMessagesResponseState = ResponseState.Success(),
                            allMessages = result.data.map {message-> message.toChatMessageUi() }
                        )
                    }
                    groupMessageByDate()
                }
            }
        }
    }

    private fun getChatStatus(){


        chatStatusJob?.cancel()
        chatStatusJob = viewModelScope.launch {

            while (isActive){
                val chatId = state.value.chatId
                val result = chatStatusUseCase.invoke(chatId)
                when(result){
                    is Resource.Loading -> {}
                    is Resource.Error -> {}
                    is Resource.Success -> {
                        val lastTimeOnlineMillis = DateTimeUtil.toMillisUtc(result.data.firstOrNull()?.last_seen)
                        val lastTimeOnline = DateTimeUtil.formatDateTimeForChatUserStatus(
                            millis = lastTimeOnlineMillis,
                           bugun = state.value.bugun,
                            kecha = state.value.kecha
                        )
                        _state.update {
                            it.copy(
                                isUserOnline = result.data.firstOrNull()?.online?:false,
                                lastTimeOnline = lastTimeOnline,
                                chatMembersCount = result.data.size
                            )
                        }
                    }
                }
                delay(15000)
            }


        }
    }

    private fun markMessageAsRead(){
        Logger.d(TAG, "markMessageAsRead: ${state.value.lastMessage}")
        readMessageJob?.cancel()
        readMessageJob = viewModelScope.launch {
            var lastMessage = state.value.lastMessage
            if (lastMessage == null){

                lastMessage = state.value.messages
                    .filterIsInstance<ChatMessageItem.Message>()
                    .maxByOrNull { it.chatMessageUi.createdAt }
                    ?.chatMessageUi

                if (lastMessage == null)

                return@launch
            }
            if (lastMessage.isMine || lastMessage.isRead) return@launch
            readMessageUseCase.invoke(
                chatId = state.value.chatId,
                messageId = lastMessage.id
            )
        }
    }


    fun markAllMessagesReadLocally() {
        val updated = _state.value.messages.map { item ->
            when (item) {
                is ChatMessageItem.DateHeader -> item
                is ChatMessageItem.Message -> item.copy(
                    chatMessageUi = item.chatMessageUi.copy(isRead = true)
                )
            }
        }

        _state.update { old ->
            old.copy(
                messages = updated,
                lastMessage = updated.filterIsInstance<ChatMessageItem.Message>()
                    .lastOrNull()?.chatMessageUi
            )
        }
    }

    private fun clearMessageText(){
        _state.update {
            it.copy(
                message = ""
            )
        }
    }

    fun groupMessageByDate(reverse: Boolean = true) {
        val all = state.value.allMessages
        if (all.isEmpty()) {
            _state.update { it.copy(messages = emptyList(), lastMessage = null) }
            return
        }

        val tz = TimeZone.currentSystemDefault()

        // 1) LocalDate bo‘yicha guruhlash (formatlangan string emas!)
        val grouped = all.groupBy { msg ->
            Instant
                .fromEpochMilliseconds(msg.createdAt)
                .toLocalDateTime(tz)
                .date
        }


        // 2) Sana tartibi (reverse => yangi kunlar birinchi)
        val dateKeys = if (reverse) grouped.keys.sortedDescending() else grouped.keys.sorted()


        // 3) Flatten: reverse bo‘lsa -> Messages (DESC) → Header
        val items = buildList<ChatMessageItem> {
            for (date in dateKeys) {
                val lanCode = LanguagePrefs.loadOrDefault().languageCode
                val lang = LanguageType.getLangType(lanCode)
                val msgs = grouped[date].orEmpty()
                if (reverse) {
                    val sortedMsgs = msgs.sortedByDescending { it.createdAt }
                    addAll(sortedMsgs.map { ChatMessageItem.Message(it) })
                    add(
                        ChatMessageItem.DateHeader(
                            DateTimeUtil.formatDayMonthLocalized(
                                localDate = date,
                                bugun = state.value.bugun,
                                kecha = state.value.kecha,
                                lang = lang
                            )
                        )
                    )
                } else {
                    add(
                        ChatMessageItem.DateHeader(
                            DateTimeUtil.formatDayMonthLocalized(
                                localDate = date,
                                bugun = state.value.bugun,
                                kecha = state.value.kecha,
                                lang = lang
                            )
                        )
                    )
                    val sortedMsgs = msgs.sortedBy { it.createdAt }
                    addAll(sortedMsgs.map { ChatMessageItem.Message(it) })
                }
            }
        }

        val lastMessage = all.maxByOrNull { it.createdAt }

        Logger.d("Message", "allMessages: $items")

        _state.update {
            it.copy(
                messages = items,
                lastMessage = lastMessage
            )
        }
    }


}