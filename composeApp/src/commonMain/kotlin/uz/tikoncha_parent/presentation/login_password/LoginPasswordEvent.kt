package uz.saidburxon.newedu.presentation.feature.login_password

sealed class LoginPasswordEvent {
    data class OnLoginPassword(val number: String): LoginPasswordEvent()
    object OnConfirmClicked: LoginPasswordEvent()
}