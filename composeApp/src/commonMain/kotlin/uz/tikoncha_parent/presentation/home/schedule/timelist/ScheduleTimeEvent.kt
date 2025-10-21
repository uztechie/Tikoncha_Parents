package uz.tikoncha_parent.presentation.home.schedule.timelist

import kotlinx.datetime.LocalTime
import uz.tikoncha_parent.domain.model.WeekDay

sealed interface ScheduleTimeEvent {
    data class RemoveTime(val time: ScheduleTimeUi) : ScheduleTimeEvent
    data class SelectDay(val day: WeekDay): ScheduleTimeEvent
    data class SetAllDay(val allDay: Boolean): ScheduleTimeEvent
    data class SetOutsideInterval(val outside: Boolean): ScheduleTimeEvent
    data class SetTime(val startTime: LocalTime, val endTime: LocalTime): ScheduleTimeEvent
    data class SetTimeData(val timeData: ScheduleTimeUi): ScheduleTimeEvent
    data object SaveTime: ScheduleTimeEvent
    data object ClearTime: ScheduleTimeEvent


}