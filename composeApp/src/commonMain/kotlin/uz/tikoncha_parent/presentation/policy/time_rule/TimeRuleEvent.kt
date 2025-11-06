@file:OptIn(ExperimentalResourceApi::class)

package uz.tikoncha_parent.presentation.policy.time_rule

import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import uz.tikoncha_parent.domain.model.WeekDay

sealed interface TimeRuleEvent {

    data class SetList(val list: List<TimeRuleUi>): TimeRuleEvent
    data class RemoveTimeRule(val time: TimeRuleUi) : TimeRuleEvent
    data class SelectDay(val day: WeekDay): TimeRuleEvent
    data class SetAllDay(val allDay: Boolean): TimeRuleEvent
    data class SetOutsideInterval(val outside: Boolean): TimeRuleEvent
    data class SetTimeRule(val startTime: LocalTime, val endTime: LocalTime): TimeRuleEvent
    data class SetTimeRuleData(val timeData: TimeRuleUi): TimeRuleEvent
    data object SaveTime: TimeRuleEvent
    data object ClearTime: TimeRuleEvent
}
