package uz.tikoncha_parent.presentation.policy.time_rule

import kotlinx.datetime.LocalTime
import uz.tikoncha_parent.domain.model.MinuteRange
import uz.tikoncha_parent.domain.model.WeekDay

data class TimeRuleState(
    val timeList: List<TimeRuleUi> = emptyList(),
    val selectedDays: Set<WeekDay> = setOf(),
    val currentDay: WeekDay? = null,
    val selectOutside: Boolean = false,
    val allDay: Boolean = false,
    val timeRanges: List<MinuteRange> = emptyList(),
    val startTime: LocalTime = LocalTime(8, 0),
    val endTime: LocalTime = LocalTime(12, 0),
    val currentId: Int? = null,
)
