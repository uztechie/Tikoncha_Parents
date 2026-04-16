package uz.tikoncha_parent.presentation.policy.policy_setup

import uz.tikoncha_parent.domain.model.LocationRule
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.policy.policy_list.PolicyItemUi
import uz.tikoncha_parent.presentation.policy.limit_rule.LimitRuleUi
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleUi
import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class PolicySetupState(
    val createState: ResponseState<Nothing> = ResponseState.Idle,
    val updateState: ResponseState<Nothing> = ResponseState.Idle,
    val deleteState: ResponseState<Nothing> = ResponseState.Idle,
) {
    val isLoading: Boolean
        get() = createState is ResponseState.Loading
                || updateState is ResponseState.Loading
                || deleteState is ResponseState.Loading
}
