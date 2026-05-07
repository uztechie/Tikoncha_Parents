package uz.tikoncha_parent.presentation.register

import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class RegisterState(
    val name: String = "",
    val lastName: String = "",
    val middleName: String = "",
    val idNumber: String = "",
    val genderIndex: Int = 0,
    val accept: Boolean = false,
    val registerResponseState: ResponseState<Nothing> = ResponseState.Idle
) {
    val isFromValid: Boolean get() = name.trim().length >= 2 && registerResponseState !is ResponseState.Loading
}
