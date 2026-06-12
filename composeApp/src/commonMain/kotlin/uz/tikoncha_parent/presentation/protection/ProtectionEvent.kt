package uz.tikoncha_parent.presentation.protection

import uz.tikoncha_parent.domain.model.protection.AccountRequestAction

sealed interface ProtectionEvent {
    data class LoadStatus(val childId: String) : ProtectionEvent
    data class Refresh(val childId: String) : ProtectionEvent
    data object ToggleCodeVisibility : ProtectionEvent

    data class ApproveStrictRequest(val requestId: String) : ProtectionEvent
    data class RejectStrictRequest(val requestId: String) : ProtectionEvent

    data class AllowAccountRequest(val action: AccountRequestAction) : ProtectionEvent
    data class DenyAccountRequest(val action: AccountRequestAction) : ProtectionEvent

    data object ActionErrorDismissed : ProtectionEvent
}