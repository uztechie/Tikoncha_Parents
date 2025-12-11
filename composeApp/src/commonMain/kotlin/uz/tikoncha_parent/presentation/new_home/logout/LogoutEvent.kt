package uz.tikoncha_parent.presentation.new_home.logout

sealed interface LogoutEvent {
    data class SetType(val type: LogoutType) : LogoutEvent
    data object CreateRequest : LogoutEvent
    data class DeleteRequest(val requestId: String) : LogoutEvent
    data object RefreshList : LogoutEvent
    data object ResetResponseState : LogoutEvent
}