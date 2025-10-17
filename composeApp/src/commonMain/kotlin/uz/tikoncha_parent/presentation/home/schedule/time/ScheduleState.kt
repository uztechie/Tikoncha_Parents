package uz.tikoncha_parent.presentation.home.schedule.time

import kotlinx.datetime.LocalTime

data class ActiveTimeState(
    val selectedDays: Set<WeekDay> = setOf(),
    val allDay: Boolean = false,
    val ranges: List<MinuteRange> = listOf(MinuteRange(12 * 60, 16 * 60)) // 12:00–16:00
)

data class MinuteRange(val start: Int, val end: Int) {
    init {
        require(start in 0..(24 * 60) && end in 0..(24 * 60) && start < end)
    }
}

data class ScheduleState(
    val time: LocalTime? = null
)