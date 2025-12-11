package uz.tikoncha_parent.presentation.new_home.logout

import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class LogoutState(
    val createResponseState: ResponseState<Nothing> = ResponseState.Idle,
    val deleteResponseState: ResponseState<Nothing> = ResponseState.Idle,
    val listResponseState: ResponseState<Nothing> = ResponseState.Idle,
    val items: List<LogoutUi> = emptyList(),
    val currentType: LogoutType = LogoutType.LOGOUT
)
