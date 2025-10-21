package uz.tikoncha_parent.presentation.home.schedule.timelist

import kotlinx.datetime.LocalTime
import uz.tikoncha_parent.domain.model.MinuteRange
import uz.tikoncha_parent.domain.model.WeekDay

data class ScheduleTimeUi(
    val id: Int = 0,
    val startTime: LocalTime = LocalTime(0,0),
    val endTime: LocalTime = LocalTime(0,0),
    val outside: Boolean = false,
    val allDay: Boolean = false,
    val time: String = "",
    val weekDays: Set<WeekDay> = emptySet(),
    val timeRange: List<MinuteRange> = emptyList()
)
