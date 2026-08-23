package uz.tikoncha_parent.presentation.login

sealed interface LoginEvent {
    data class OnNumberInsert(val number: String) : LoginEvent
    data object OnTelegramClicked : LoginEvent
    data object OnPhoneContinue : LoginEvent          // YANGI
    data object OnErrorDismissed : LoginEvent
    data object OnDialogErrorDismissed : LoginEvent    // YANGI
    data object OnTelegramReturned : LoginEvent
}