package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class TimeRuleDto(
    val start_time: Int,
    val end_time: Int,
    val time_include: Boolean,
    val days: List<Int>
)
