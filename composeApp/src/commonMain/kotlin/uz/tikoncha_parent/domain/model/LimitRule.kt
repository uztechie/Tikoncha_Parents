package uz.tikoncha_parent.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LimitRule(
    val window: LimitWindow,
    val allowedMinutes:Int,
    val weekDay: Int
)
