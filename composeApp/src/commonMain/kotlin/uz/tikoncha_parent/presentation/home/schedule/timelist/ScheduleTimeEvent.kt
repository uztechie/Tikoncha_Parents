@file:OptIn(ExperimentalResourceApi::class)

package uz.tikoncha_parent.presentation.home.schedule.timelist

import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import uz.tikoncha_parent.domain.model.DayHour
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


    data class SetUsageLimitTime(val time: LocalTime):ScheduleTimeEvent
    data class SelectUsageDay(val usageDay: WeekDay): ScheduleTimeEvent
    data object SaveLimit: ScheduleTimeEvent
    data class SetUsageLimitData(val usageLimitData: ScheduleUsageLimitUi): ScheduleTimeEvent
    data class RemoveUsageLimit(val usageLimit: ScheduleUsageLimitUi) : ScheduleTimeEvent
    data class SetUsageType(val isDaily: Boolean) : ScheduleTimeEvent
    data class SelectLimitType(val dayHour: DayHour) : ScheduleTimeEvent
}
