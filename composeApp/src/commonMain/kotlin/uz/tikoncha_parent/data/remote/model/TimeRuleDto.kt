package uz.tikoncha_parent.data.remote.model

data class TimeRuleDto(
    val start_time: Int,
    val end_time: Int,
    val time_include: Boolean,
    val days: List<Int>
)
