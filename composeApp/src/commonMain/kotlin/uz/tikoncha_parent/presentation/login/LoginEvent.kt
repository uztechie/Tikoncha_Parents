package uz.tikoncha_parent.presentation.login

sealed interface LoginEvent {
    data class OnNumberInsert(val number: String): LoginEvent
}