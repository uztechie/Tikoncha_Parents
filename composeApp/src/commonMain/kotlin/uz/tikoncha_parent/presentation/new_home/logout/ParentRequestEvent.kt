package uz.tikoncha_parent.presentation.new_home.logout

sealed interface ParentRequestEvent {
    data class SetType(val type: ParentRequestType) : ParentRequestEvent
    data class SelectedRequest(val request: ParentRequestUi) : ParentRequestEvent
    data object AccessSelectedRequest: ParentRequestEvent
    data object DenySelectedRequest: ParentRequestEvent
    data object ResetResponseState : ParentRequestEvent
}