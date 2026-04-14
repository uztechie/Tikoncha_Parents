package uz.tikoncha_parent.presentation.profile.coins

import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.new_home.HomeEvent

sealed interface CoinsEvent {
    data class OnChildSelected(val child: UserInfo): CoinsEvent
    data object GetChildren: CoinsEvent
    data class OnCoinsChanged(val value: Int): CoinsEvent
    data object LoadCoinList: CoinsEvent
    data class OnPackageSelected(val index: Int) : CoinsEvent
}