package uz.tikoncha_parent.presentation.policy.policy_setup

import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.policy.limit_rule.LimitRuleUi
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleUi
import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class PolicySetupState(
    val limitList: List<LimitRuleUi> = emptyList(),
    val timeList: List<TimeRuleUi> = emptyList(),
    val responseState: ResponseState<Nothing> = ResponseState.Idle,
    val selectedChild: UserInfo? = null,
    val packagesString: String = ""
)
