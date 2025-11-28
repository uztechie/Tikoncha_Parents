package uz.tikoncha_parent.presentation.new_home

import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class HomeState(

    val childrenList: List<UserInfo> = emptyList(),
    val selectedChild: UserInfo? = null,

    val childrenResponseState: ResponseState<Nothing> = ResponseState.Idle,
    val appUsageResponseState: ResponseState<Nothing> = ResponseState.Idle,
    val createRuleResponseState: ResponseState<Nothing> = ResponseState.Idle,

    )
