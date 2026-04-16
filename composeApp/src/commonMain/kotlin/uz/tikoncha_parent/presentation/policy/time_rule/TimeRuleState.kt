package uz.tikoncha_parent.presentation.policy.time_rule

import kotlinx.datetime.LocalTime
import uz.tikoncha_parent.domain.model.MinuteRange
import uz.tikoncha_parent.presentation.policy.common.WeekDayChipUi

data class TimeRuleState(
    val timeList: List<TimeRuleUi> = emptyList(),
    val weekDays: List<WeekDayChipUi> = emptyList(),
    val selectOutside: Boolean = false,
    val allDay: Boolean = false,
    val timeRanges: List<MinuteRange> = emptyList(),
    val startTime: LocalTime = LocalTime(8, 0),
    val endTime: LocalTime = LocalTime(12, 0),
    val currentId: Int? = null,
    val showSetupDialog: Boolean = false
)
