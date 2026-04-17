package uz.tikoncha_parent.presentation.policy.time_rule.setup

import kotlinx.datetime.LocalTime
import uz.tikoncha_parent.domain.model.WeekDay
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleUi

sealed interface TimeRuleSetupEvent {

    /**
     * Screen ochilganda bir marta chaqiriladi.
     * @param editingRule edit rejimda tahrirlanayotgan rule, create da null
     * @param allTimeRules shu policyning barcha time rulelari (band kunlar hisobi uchun)
     */
    data class Init(
        val editingRule: TimeRuleUi?,
        val allTimeRules: List<TimeRuleUi>,
    ) : TimeRuleSetupEvent

    data class SelectDay(val day: WeekDay) : TimeRuleSetupEvent

    data class SetStartTime(val time: LocalTime) : TimeRuleSetupEvent
    data class SetEndTime(val time: LocalTime) : TimeRuleSetupEvent
    data class SetTimeRange(val start: LocalTime, val end: LocalTime) : TimeRuleSetupEvent


    data class SetAllDay(val allDay: Boolean) : TimeRuleSetupEvent
    data class SetReverse(val outside: Boolean) : TimeRuleSetupEvent

    data object Save : TimeRuleSetupEvent
}