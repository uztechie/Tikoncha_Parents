package uz.tikoncha_parent.domain.model

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import kotlinx.serialization.Serializable

@Serializable
data class HourMinute(
    val hour:Int = 0,
    val minute:Int = 0
):JavaSerializable
