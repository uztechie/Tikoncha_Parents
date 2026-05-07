package uz.tikoncha_parent.presentation.policy.policy_list

import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.new_home.HomeEvent

sealed class PolicyEvent {
    data object RefreshPolicies: PolicyEvent()
    data class OnChildSelected(val child: UserInfo): PolicyEvent()
    data object GetChildren: PolicyEvent()
}