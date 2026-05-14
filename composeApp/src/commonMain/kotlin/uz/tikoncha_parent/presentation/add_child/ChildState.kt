package uz.tikoncha_parent.presentation.add_child

import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class ChildState(
    val confirmCode: String = "",
    val number: String = "",
    val fullNumber: String = "",
    var accept: Boolean = false,
    var childJoined: Boolean = false,
    var showConnectChildTutorialCard: Boolean = false,



    val responseState: ResponseState<Unit> = ResponseState.Idle
)
