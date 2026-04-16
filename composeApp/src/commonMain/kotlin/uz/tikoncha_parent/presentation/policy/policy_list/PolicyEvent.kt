package uz.tikoncha_parent.presentation.policy.policy_list

sealed class PolicyEvent {
    data object RefreshPolicies: PolicyEvent()

}