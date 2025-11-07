package uz.tikoncha_parent.data.mapper

import uz.tikoncha_parent.data.remote.model.LimitRuleDto
import uz.tikoncha_parent.data.remote.model.TimeRuleDto
import uz.tikoncha_parent.presentation.policy.limit_rule.LimitRuleUi
import uz.tikoncha_parent.presentation.policy.limit_rule.toHourMinuteString
import uz.tikoncha_parent.presentation.policy.limit_rule.toMinutes
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleUi

fun List<TimeRuleUi>.toTimeRuleDtoList(): List<TimeRuleDto> {
    val list = mutableListOf<TimeRuleDto>()
    this.forEach {
        list.add(
            TimeRuleDto(
                start_time = it.startTime.toMinutes(),
                end_time = it.endTime.toMinutes(),
                days = it.weekDays.map { it.num },
                time_include = !it.outside
            )
        )
    }
    return list
}

fun List<LimitRuleUi>.toLimitRuleDtoList(): List<LimitRuleDto>{
    val list = mutableListOf<LimitRuleDto>()
    this.forEach {
        list.add(
            LimitRuleDto(
               limit_amount = it.time.toMinutes(),
               limit_type = it.limitType.name,
                days = it.weekDays.map { it.num }
            )
        )
    }
    return list
}
