package uz.tikoncha_parent.presentation.policy

import uz.tikoncha_parent.domain.model.UserInfo

sealed class PolicyEvent {
    data class SetSelectedChild(val child: UserInfo): PolicyEvent()

}