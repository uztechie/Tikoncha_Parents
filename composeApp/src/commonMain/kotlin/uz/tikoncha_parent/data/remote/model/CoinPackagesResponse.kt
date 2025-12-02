package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CoinPackagesResponse(
    val success: Boolean,
    val data: List<CoinPackageDto>,
    val error: String? = null,
    val code: Int? = null
)

@Serializable
data class CoinPackageDto(
    val coins: Int,
    val price: Long,
    val discount_percent: Int,
)
