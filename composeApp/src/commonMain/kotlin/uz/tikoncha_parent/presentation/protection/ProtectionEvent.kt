package uz.tikoncha_parent.presentation.protection

import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.domain.model.protection.AccountRequestAction
import uz.tikoncha_parent.presentation.new_home.HomeEvent

sealed interface ProtectionEvent {
    data class LoadStatus(val childId: String) : ProtectionEvent
    data class Refresh(val childId: String) : ProtectionEvent
    data object ToggleCodeVisibility : ProtectionEvent

    data class ApproveStrictRequest(val requestId: String) : ProtectionEvent
    data class RejectStrictRequest(val requestId: String) : ProtectionEvent

    data class AllowAccountRequest(val action: AccountRequestAction) : ProtectionEvent
    data class DenyAccountRequest(val action: AccountRequestAction) : ProtectionEvent

    data object ActionErrorDismissed : ProtectionEvent

    data class OnChildSelected(val child: UserInfo) : ProtectionEvent
    data object GetChildren : ProtectionEvent
}