package uz.tikoncha_parent.domain.model

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import kotlinx.serialization.Serializable

@Serializable
data class HourMinute(
    val hour:Int = 0,
    val minute:Int = 0
){
    companion object {
        fun fromMinutes(total: Int): HourMinute =
            HourMinute(total / 60, total % 60)
    }

    fun toMinutes(): Int = hour * 60 + minute
}
