package uz.tikoncha_parent.presentation.policy.limit_rule

import uz.tikoncha_parent.domain.model.DayHour
import uz.tikoncha_parent.domain.model.HourMinute
import uz.tikoncha_parent.presentation.policy.common.WeekDayChipUi
import uz.tikoncha_parent.presentation.policy.rule_type_selection.RuleType

data class LimitRuleState(

    val currentId: Int? = null,
    val weekDays: List<WeekDayChipUi> = emptyList(),
    val limitRuleList: List<LimitRuleUi> = emptyList(),
    val hourMinute: HourMinute = HourMinute(),
    val enabledByType: Map<RuleType, Boolean> = emptyMap(),
    val selectedLimitType: DayHour = DayHour.DAY,
    val showSetupDialog: Boolean = false
)
