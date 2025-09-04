package uz.tikoncha_parent.presentation.child_confirm_cod

sealed class ChildConfirmEvent {
    data class SetConfirmCode(val code: String): ChildConfirmEvent()
}