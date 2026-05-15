package uz.tikoncha_parent.presentation.add_child

sealed interface AddChildEvent {
    data class PhoneChanged(val value: String) : AddChildEvent
    data object RequestCode : AddChildEvent
    data object RefreshCode : AddChildEvent
    data object CodeCopied : AddChildEvent
    data object OpenAppLink : AddChildEvent
    data object ShareLink : AddChildEvent
    data object PlayTutorial : AddChildEvent
    data object DismissError : AddChildEvent
    data object DismissSnackbar : AddChildEvent
    data object NavigateBack : AddChildEvent
}