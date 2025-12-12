package uz.tikoncha_parent.presentation.new_home.logout

import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class ParentRequestState(
    val createResponseState: ResponseState<Nothing> = ResponseState.Idle,
    val deleteResponseState: ResponseState<Nothing> = ResponseState.Idle,
    val listResponseState: ResponseState<Nothing> = ResponseState.Idle,
    val selectedRequest: ParentRequestUi? = null,
    val items: List<ParentRequestUi> = emptyList(),
    val currentType: ParentRequestType = ParentRequestType.LOGOUT
)
