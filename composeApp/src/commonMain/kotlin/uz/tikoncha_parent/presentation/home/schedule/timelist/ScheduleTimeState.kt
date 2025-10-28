package uz.tikoncha_parent.presentation.home.schedule.timelist

import kotlinx.datetime.LocalTime
import uz.tikoncha_parent.domain.model.MinuteRange
import uz.tikoncha_parent.domain.model.WeekDay
import uz.tikoncha_parent.presentation.home.schedule.type.ScheduleType

data class ScheduleTimeState(
    val timeList: List<ScheduleTimeUi> = emptyList(),
    val selectedDays: Set<WeekDay> = setOf(),
    val currentDay: WeekDay? = null,
    val selectOutside: Boolean = false,
    val allDay: Boolean = false,
    val timeRanges: List<MinuteRange> = emptyList(),
    val startTime: LocalTime = LocalTime(8, 0),
    val endTime: LocalTime = LocalTime(12, 0),
    val currentId: Int? = null,

    val usageLimitDays: Set<WeekDay> = setOf(),
    val usageTimeList: List<ScheduleTimeUi> = emptyList(),
    val dayHour: LocalTime = LocalTime(0, 0),
    val dayMinute: LocalTime = LocalTime(0, 0),
    val hourly: LocalTime = LocalTime(0, 0),
    val enabledByType: Map<ScheduleType, Boolean> = emptyMap(),
)
