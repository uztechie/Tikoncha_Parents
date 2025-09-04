package uz.tikoncha_parent.presentation.login

sealed class LoginEvent {
    data class OnNumberInsert(val number: String): LoginEvent()
    object OnConfirmClicked: LoginEvent()
    object Reset: LoginEvent()
}