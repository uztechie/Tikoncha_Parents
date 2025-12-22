package uz.tikoncha_parent.presentation.policy.limit_rule

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import kotlinx.serialization.Serializable
import uz.tikoncha_parent.domain.model.DayHour
import uz.tikoncha_parent.domain.model.HourMinute
import uz.tikoncha_parent.domain.model.WeekDay

@Serializable

data class LimitRuleUi(
    val id: Int = 0,
    val time: HourMinute = HourMinute(),
    val weekDays: Set<WeekDay> = emptySet(),
    val limitType: DayHour = DayHour.DAY
): JavaSerializable
