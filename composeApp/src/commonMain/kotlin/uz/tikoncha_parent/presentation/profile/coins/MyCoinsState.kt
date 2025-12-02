package uz.tikoncha_parent.presentation.profile.coins

import uz.tikoncha_parent.domain.model.CoinPackage
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class MyCoinsState(
    val isLoading: Boolean = false,
    val coins: Int? = null,
    val error: String? = null,
    val packages: List<CoinPackage> = emptyList(),

    val selectedChild: UserInfo? = null,
    val childrenList: List<UserInfo> = emptyList(),

    val childrenResponseState: ResponseState<Nothing> = ResponseState.Idle,
)
