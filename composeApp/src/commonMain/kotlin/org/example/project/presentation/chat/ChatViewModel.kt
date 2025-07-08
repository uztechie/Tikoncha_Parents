package uz.saidburxon.newedu.presentation.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.common.Util
import org.example.project.platform.reformatDateTime
import utils.getCurrentTimeMillis
import uz.saidburxon.newedu.domain.model.ChatMessage
import uz.saidburxon.newedu.domain.model.ChatMessageItem


class ChatViewModel : ViewModel() {

    private var sendMessageJob: Job? = null

    private val _state = MutableStateFlow(ChatState())
    val state = _state.asStateFlow()

    init {
        groupMessageByDate()
    }

    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.OnMessageChange -> {
                _state.update {
                    it.copy(
                        message = event.message
                    )
                }
            }

            ChatEvent.SendMessage -> {
                if (state.value.message.isNotBlank()){
                    sendMessage()
                }
            }
        }
    }


    fun groupMessageByDate() {
        val groupedMessages = state.value.allMessages
            .sortedBy { it.createdAt }
            .groupBy { chat ->
                Util.formatLocalDate(chat.createdAt)
            }

        val result = mutableListOf<ChatMessageItem>()
        for ((date, messagesOnDate) in groupedMessages) {
            result.add(ChatMessageItem.DateHeader(date))
            result.addAll(messagesOnDate.map { ChatMessageItem.Message(it) })
        }

        _state.update {
            it.copy(
                messages = result
            )
        }
    }

    private fun sendMessage() {
        sendMessageJob?.cancel()
        sendMessageJob = viewModelScope.launch {
            val chatMessage = ChatMessage(
                id = getCurrentTimeMillis(),
                isMine = true,
                message = state.value.message.trim(),
                createdAt = getCurrentTimeMillis(),
                time = Util.formatLocalDate(getCurrentTimeMillis())
            )

            _state.update {
                it.copy(
                    allMessages = it.allMessages + chatMessage,
                    message = ""
                )
            }

            groupMessageByDate()

            delay(2000)
            val chatMessageReplay = ChatMessage(
                id = getCurrentTimeMillis(),
                isMine = false,
                message = chatMessage.message,
                createdAt = getCurrentTimeMillis(),
                time = Util.formatLocalDate(getCurrentTimeMillis())
            )
            _state.update {
                it.copy(
                    allMessages = it.allMessages + chatMessageReplay
                )
            }
            groupMessageByDate()

        }
    }


}