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

        fun fromMillis(ms: Long): HourMinute {
            if (ms <= 0L) return HourMinute(0, 0)
            val totalMin = (ms / 60_000L).toInt()
            return HourMinute(hour = totalMin / 60, minute = totalMin % 60)
        }

        fun HourMinute.isEmpty(): Boolean {
            return hour == 0 && minute == 0
        }

    }

    fun toMinutes(): Int = hour * 60 + minute
}
