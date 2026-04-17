package uz.tikoncha_parent.domain.model.subscription

import kotlinx.serialization.Serializable

@Serializable
data class CoinPackageListResponse(
    val success: Boolean,
    val data: CoinPackageListWrapper? = null,
    val error: String? = null,
    val code: Int? = null,
)

@Serializable
data class CoinPackageListWrapper(
    val coin_price: Int,
    val coin_packages: List<CoinPackageListData>
)

@Serializable
data class CoinPackageListData(
    val coins: Int,
    val price: Int,
    val discount_percent: Int
)