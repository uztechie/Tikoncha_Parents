@file:OptIn(ExperimentalResourceApi::class)

package uz.tikoncha_parent.presentation.policy.limit_rule

import org.jetbrains.compose.resources.ExperimentalResourceApi
import uz.tikoncha_parent.domain.model.DayHour
import uz.tikoncha_parent.domain.model.HourMinute
import uz.tikoncha_parent.domain.model.WeekDay

sealed interface LimitRuleEvent {


    data object ClearData: LimitRuleEvent

    data class SetList(val list: List<LimitRuleUi>): LimitRuleEvent


    data class SetTime(val time: HourMinute):LimitRuleEvent
    data class SelectWeekDay(val usageDay: WeekDay): LimitRuleEvent
    data object SaveLimit: LimitRuleEvent
    data class SetUsageLimitData(val usageLimitData: LimitRuleUi): LimitRuleEvent
    data class RemoveLimitRule(val usageLimit: LimitRuleUi) : LimitRuleEvent
    data class SetUsageType(val isDaily: Boolean) : LimitRuleEvent
    data class SelectLimitType(val dayHour: DayHour) : LimitRuleEvent
}
