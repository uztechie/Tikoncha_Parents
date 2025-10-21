package uz.tikoncha_parent.presentation.home.schedule.timelist

import uz.tikoncha_parent.domain.model.MinuteRange
import uz.tikoncha_parent.domain.model.WeekDay

data class ScheduleTimeUi(
    val time: String = "",
    val weekDays: Set<WeekDay> = emptySet(),
    val timeRange: List<MinuteRange> = emptyList()
)
