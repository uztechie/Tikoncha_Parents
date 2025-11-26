package uz.tikoncha_parent.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TimeRule(
    val weekDay:Int,
    val startMin:Int,
    val endMin:Int,
    val inversion: Boolean
)