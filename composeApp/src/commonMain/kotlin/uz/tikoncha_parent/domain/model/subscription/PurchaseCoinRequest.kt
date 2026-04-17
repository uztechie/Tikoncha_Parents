package uz.tikoncha_parent.domain.model.subscription

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseCoinRequest(
    val coins: Int,
)
