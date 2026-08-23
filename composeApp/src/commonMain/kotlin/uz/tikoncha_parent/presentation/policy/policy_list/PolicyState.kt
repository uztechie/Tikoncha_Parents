package uz.tikoncha_parent.presentation.policy.policy_list

import uz.tikoncha_parent.domain.model.PolicyType
import uz.tikoncha_parent.domain.model.SubscriptionLimit
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.domain.model.permission_status.PermissionIssue
import uz.tikoncha_parent.presentation.ui_state.ResponseState


data class PolicyState(
    val policyResponseState: ResponseState<Nothing> = ResponseState.Idle,
    val policies: List<PolicyItemUi> = emptyList(),
    val selectedChild: UserInfo? = null,
    val subscriptionLimit: SubscriptionLimit = SubscriptionLimit(),
    val isInitialLoadDone: Boolean = false,
    val permissionIssueList: List<PermissionIssue> = emptyList(),
    val childrenList: List<UserInfo> = emptyList(),
    val childrenResponseState: ResponseState<Nothing> = ResponseState.Idle,

    val selectedTypeIndex: Int = 0,
    val showPolicyTutorialCard: Boolean = false,
){
    val canCreatePolicy: Boolean
        get() = subscriptionLimit.policyCount.let {
            policies.count { it.policyType == PolicyType.PARENT_CHILD } < it
        } ?: true

    val filteredPolicies: List<PolicyItemUi> get() =
        policies.filter {
            it.policyType == when(selectedTypeIndex){
                0 ->  PolicyType.PARENT_CHILD
                1 -> PolicyType.STUDENT
                2 -> PolicyType.SCHOOL
                else ->PolicyType.PARENT_CHILD

            }
        }
}
