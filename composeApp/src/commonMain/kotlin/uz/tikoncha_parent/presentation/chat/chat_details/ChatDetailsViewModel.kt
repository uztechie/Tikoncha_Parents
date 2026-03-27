package uz.tikoncha_parent.presentation.chat.chat_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import uz.tikoncha_parent.common.DateTimeUtil
import uz.tikoncha_parent.data.mapper.toChatMemberUi
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.chat.ChatStatusUseCase
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class ChatDetailsViewModel(
    private val chatStatusUseCase: ChatStatusUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ChatDetailState>(ChatDetailState())
    val state = _state.asStateFlow()


    private var chatStatusJob: Job? = null

    init {

    }

    fun onEvent(event: ChatDetailEvent) {
        when (event) {
            is ChatDetailEvent.SetChatData -> {
                _state.update {
                    it.copy(
                        chatId = event.chatId,
                        chatAvatar = event.chatAvatar,
                        chatTitle = event.chatTitle
                    )
                }
                getChatStatus()
            }

            is ChatDetailEvent.SetDateStrings -> {
                _state.update {
                    it.copy(
                        bugun = event.bugun,
                        kecha = event.kecha
                    )
                }
            }
        }
    }


    private fun getChatStatus() {

        _state.update {
            it.copy(
                responseState = ResponseState.Loading
            )
        }

        chatStatusJob?.cancel()
        chatStatusJob = viewModelScope.launch {

            while (isActive) {
                val chatId = state.value.chatId
                val result = chatStatusUseCase.invoke(chatId)
                when (result) {
                    is Resource.Loading -> {}
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                               responseState = ResponseState.Error(
                                   res = result.resId,
                                   message = result.message
                               )
                            )
                        }
                    }
                    is Resource.Success -> {
                        val lastTimeOnlineMillis =
                            DateTimeUtil.toMillisUtc(result.data.firstOrNull()?.last_seen)
                        val lastTimeOnline = DateTimeUtil.formatDateTimeForChatUserStatus(
                            millis = lastTimeOnlineMillis,
                            bugun = state.value.bugun,
                            kecha = state.value.kecha
                        )

                        val members = result.data.map { member ->

                            val millis = DateTimeUtil.toMillisUtc(member.last_seen)
                            val memberLastSeen = DateTimeUtil.formatDateTimeForChatUserStatus(
                                millis = millis,
                                bugun = state.value.bugun,
                                kecha = state.value.kecha
                            )
                            member.toChatMemberUi()
                            .copy(lastSeen = memberLastSeen)
                        }

                        _state.update {
                            it.copy(
                                members = members,
                                memberCount = result.data.count(),
                                responseState = ResponseState.Success()
                            )
                        }
                    }
                }
                delay(15000)
            }


        }
    }
}