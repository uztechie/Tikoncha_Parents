package org.example.project.presentation.login

sealed class LoginEvent {
    data class OnNumberInsert(val number: String): LoginEvent()
    object OnConfirmClicked: LoginEvent()
}