package uz.tikoncha_parent.presentation.new_home.logout

sealed interface ParentRequestEvent {
    data class SetType(val type: ParentRequestType) : ParentRequestEvent
    data object CreateRequest : ParentRequestEvent
    data class DeleteRequest(val requestId: String) : ParentRequestEvent
    data object RefreshList : ParentRequestEvent
    data object ResetResponseState : ParentRequestEvent
}