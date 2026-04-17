package uz.tikoncha_parent.presentation.policy.limit_rule.setup

import uz.tikoncha_parent.domain.model.DayHour
import uz.tikoncha_parent.domain.model.WeekDay
import uz.tikoncha_parent.presentation.policy.limit_rule.LimitRuleUi

sealed interface LimitRuleSetupEvent {

    /**
     * Screen ochilganda bir marta chaqiriladi.
     * @param editingRule edit rejimda tahrirlanayotgan rule, create da null
     * @param allLimitRules shu policyning barcha limit rulelari (band kunlar hisobi uchun)
     */
    data class Init(
        val editingRule: LimitRuleUi?,
        val allLimitRules: List<LimitRuleUi>,
    ) : LimitRuleSetupEvent

    data class SelectDay(val day: WeekDay) : LimitRuleSetupEvent

    data class SelectLimitType(val type: DayHour) : LimitRuleSetupEvent

    data class SetHour(val hour: Int) : LimitRuleSetupEvent
    data class SetMinute(val minute: Int) : LimitRuleSetupEvent

    data object Save : LimitRuleSetupEvent
}