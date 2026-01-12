package uz.tikoncha_parent.presentation.monitoring

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.mapper.toChatUi
import uz.tikoncha_parent.data.remote.model.SendMessageRequest
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.chat.GetChatListFromServerUseCase
import uz.tikoncha_parent.domain.use_case.chat.SendMessageApiUseCase
import uz.tikoncha_parent.domain.use_case.chat.SendMessageUseCase
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class MonitorViewModel(
    private val sendMessageApiUseCase: SendMessageApiUseCase,
    private val chatListFromServerUseCase: GetChatListFromServerUseCase
): ScreenModel {



    private var sendMessageJob: Job? = null
    private var chatListJob: Job? = null

    private val _state = MutableStateFlow(MonitorState())
    val state = _state.asStateFlow()

    init {
        loadChatList()
    }

    fun onEvent(event: MonitorEvent){
        when(event){
            is MonitorEvent.OnMessageChange -> {
                _state.value = _state.value.copy(
                    message = event.message
                )
            }
            MonitorEvent.OnSendMessageClick -> {
                if (state.value.message.isNotEmpty()){
                    sendMessage()
                }
            }

            is MonitorEvent.SelectChild -> {
                _state.value = _state.value.copy(
                    selectedChild = event.chatUi
                )
            }
        }
    }

    private fun sendMessage(){
        sendMessageJob?.cancel()
        sendMessageJob = screenModelScope.launch {
            _state.update {
                it.copy(
                    sendMessageResponseState = ResponseState.Loading
                )
            }
            val request = SendMessageRequest(
                chat_id = state.value.selectedChild?.chatId?:"",
                text = state.value.message,
                type = "TEXT"
            )
            val result = sendMessageApiUseCase(request)
            when(result){
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            sendMessageResponseState = ResponseState.Error(
                                message = result.message,
                                res = result.resId
                            )
                        )
                    }
                }
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            sendMessageResponseState = ResponseState.Success(),
                            message = ""
                        )
                    }

                }
            }
        }
    }
    private fun loadChatList(){
        chatListJob?.cancel()
        chatListJob = screenModelScope.launch {
            _state.update {
                it.copy(
                    chatListResponseState = ResponseState.Loading
                )
            }

            val result = chatListFromServerUseCase()
            when(result){
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            chatListResponseState = ResponseState.Error(
                                message = result.message,
                                res = result.resId
                            )
                        )
                    }
                }
                is Resource.Success -> {
                    val chatList = result.data.map { it.toChatUi() }
                    _state.update {
                        it.copy(
                            chatList = chatList,
                            chatListResponseState = ResponseState.Success()
                        )
                    }

                }
            }
        }
    }
}