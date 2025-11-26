package uz.tikoncha_parent.presentation.policy

import uz.tikoncha_parent.domain.model.WeekDay
import uz.tikoncha_parent.presentation.policy.limit_rule.LimitRuleUi
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleUi


interface HasWeekDays {
    val id: Int
    val weekDays: Set<WeekDay>
}

fun TimeRuleUi.asHasWeekDays(): HasWeekDays = object : HasWeekDays {
    override val id: Int get() = this@asHasWeekDays.id
    override val weekDays: Set<WeekDay> get() = this@asHasWeekDays.weekDays
}

fun LimitRuleUi.asHasWeekDays(): HasWeekDays = object : HasWeekDays {
    override val id: Int get() = this@asHasWeekDays.id
    override val weekDays: Set<WeekDay> get() = this@asHasWeekDays.weekDays
}


fun <T : HasWeekDays> calcDisabledDays(
    rules: List<T>,
    excludeId: Int? = null
): Set<WeekDay> = rules.asSequence()
    .filter { excludeId == null || it.id != excludeId }
    .flatMap { it.weekDays.asSequence() }
    .toSet()

fun buildWeekdayChips(
    selected: Set<WeekDay>,
    disabled: Set<WeekDay>
): List<WeekDayChipUi> = WeekDay.ordered.map { day ->
    WeekDayChipUi(
        day = day,
        labelResId = 1, //day.labelResId(),
        selected = day in selected,
        enabled = day !in disabled
    )
}

fun <T : HasWeekDays> buildChipsForCreate(
    rules: List<T>
): List<WeekDayChipUi> {
    val disabled = calcDisabledDays(rules)
    return buildWeekdayChips(selected = emptySet(), disabled = disabled)
}

fun <T : HasWeekDays> buildChipsForEdit(
    currentSelected: Set<WeekDay>,
    rules: List<T>,
    excludeId: Int
): List<WeekDayChipUi> {
    val disabled = calcDisabledDays(rules, excludeId = excludeId)
    return buildWeekdayChips(selected = currentSelected, disabled = disabled)
}

fun <T : HasWeekDays> buildChipsForClear(
    rules: List<T> = emptyList()
): List<WeekDayChipUi> {
    // clear paytida agar mavjud qoida bo‘lsa, ularning kunlari disable bo‘lib qoladi,
    // aks holda hammasi enabled va tanlanmagan bo‘ladi
    val disabled = calcDisabledDays(rules)
    return buildWeekdayChips(selected = emptySet(), disabled = disabled)
}