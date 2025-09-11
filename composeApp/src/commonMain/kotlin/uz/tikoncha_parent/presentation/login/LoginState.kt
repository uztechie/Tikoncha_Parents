package uz.tikoncha_parent.presentation.login

import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class LoginState(
    val number: String = "",
    val fullNumber: String = "",
    val accept: Boolean = false,
    val responseState: ResponseState<Nothing> = ResponseState.Idle
)
