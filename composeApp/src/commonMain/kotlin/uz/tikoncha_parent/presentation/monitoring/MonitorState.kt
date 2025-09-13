package uz.tikoncha_parent.presentation.monitoring

import uz.tikoncha_parent.presentation.model.ChatUi
import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class MonitorState(
    val chatListResponseState: ResponseState<Nothing> = ResponseState.Idle,
    val sendMessageResponseState: ResponseState<Nothing> = ResponseState.Idle,
    val message: String = "",
    val selectedChild: ChatUi? = null,
    val chatList: List<ChatUi> = emptyList()

)
