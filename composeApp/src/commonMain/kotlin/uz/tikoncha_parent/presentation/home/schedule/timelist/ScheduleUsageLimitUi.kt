package uz.tikoncha_parent.presentation.home.schedule.timelist

import kotlinx.datetime.LocalTime
import uz.tikoncha_parent.domain.model.DayHour
import uz.tikoncha_parent.domain.model.WeekDay

data class ScheduleUsageLimitUi(
    val id: Int = 0,
    val time: LocalTime = LocalTime(0,0),
    val weekDays: Set<WeekDay> = emptySet(),
    val limitType: DayHour = DayHour.DAY
)
