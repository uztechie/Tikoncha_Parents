package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class MyCoinsResponse(
    val success: Boolean = false,
    val data: CoinsData? = null,
    val error: String? = null,
    val code: Int? = null
)

@Serializable
data class CoinsData(
    val coins: Int = 0
)