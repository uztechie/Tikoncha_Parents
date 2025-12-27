package uz.tikoncha_parent.presentation.new_home

import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class HomeState(

    val childrenList: List<UserInfo> = emptyList(),
    val selectedChild: UserInfo? = null,

    val parentRequestCount: Int = 0,
    val activeTaskCount: Int = 0,
    val blockedAppCount: Int = 0,

    val childrenResponseState: ResponseState<Nothing> = ResponseState.Idle,
    val appUsageResponseState: ResponseState<Nothing> = ResponseState.Idle,
    val createRuleResponseState: ResponseState<Nothing> = ResponseState.Idle,

    )
