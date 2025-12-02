package uz.tikoncha_parent.presentation.profile.coins

import uz.tikoncha_parent.domain.model.CoinPackage

data class MyCoinsState(
    val isLoading: Boolean = false,
    val coins: Int? = null,
    val error: String? = null,
    val packages: List<CoinPackage> = emptyList()
)
