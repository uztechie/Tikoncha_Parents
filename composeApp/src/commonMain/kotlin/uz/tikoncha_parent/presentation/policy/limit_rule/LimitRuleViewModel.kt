@file:OptIn(ExperimentalResourceApi::class)

package uz.tikoncha_parent.presentation.policy.limit_rule

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import uz.tikoncha_parent.domain.model.DayHour
import uz.tikoncha_parent.domain.model.HourMinute
import uz.tikoncha_parent.domain.model.WeekDay
import uz.tikoncha_parent.presentation.policy.common.WeekDayChipUi
import uz.tikoncha_parent.presentation.policy.common.asHasWeekDays
import uz.tikoncha_parent.presentation.policy.common.buildChipsForClear
import uz.tikoncha_parent.presentation.policy.common.buildChipsForCreate
import uz.tikoncha_parent.presentation.policy.common.buildChipsForEdit
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleUi


class LimitRuleViewModel : ScreenModel {

    private val _state = MutableStateFlow(LimitRuleState())
    val state = _state.asStateFlow()

    init {
        _state.update { s ->
            val rules = s.limitRuleList.map { it.asHasWeekDays() }
            s.copy(weekDays = buildChipsForCreate(rules))
        }
    }


    fun event(event: LimitRuleEvent) {
        when (event) {


            LimitRuleEvent.ClearData -> {
                clearTime()

            }


            is LimitRuleEvent.SelectWeekDay -> {
                _state.update { s -> s.copy(weekDays = toggleDay(s.weekDays, event.usageDay)) }
            }

            is LimitRuleEvent.SaveLimit -> {
                saveUsageTime()
                clearTime()
            }

            is LimitRuleEvent.RemoveLimitRule -> {
                _state.update {s->
                    val newList = s.limitRuleList.filter { it != event.usageLimit }
                    val rules = newList.map { it.asHasWeekDays() }
                    s.copy(
                        limitRuleList = newList,
                        weekDays = buildChipsForCreate(rules)
                    )
                }
            }

            is LimitRuleEvent.SetUsageLimitData -> {
                val data = event.usageLimitData
                val weekdays = _state.value.limitRuleList.map { it.asHasWeekDays() }
                _state.update {
                    it.copy(
                        currentId = data.id,
                        hourMinute = data.time,
                        selectedLimitType = data.limitType,
                        weekDays = buildChipsForEdit(
                            currentSelected = data.weekDays,
                            rules = weekdays,
                            excludeId = data.id
                        )
                    )
                }
            }

            is LimitRuleEvent.SetUsageType -> {

            }

            is LimitRuleEvent.SelectLimitType -> {
                _state.update {
                    it.copy(
                        selectedLimitType = event.dayHour
                    )
                }
            }

            is LimitRuleEvent.SetTime -> {
                _state.update {
                    it.copy(
                        hourMinute = event.time
                    )
                }
            }

            is LimitRuleEvent.SetList -> {
                _state.update {s->
                    val rules = event.list.map { it.asHasWeekDays() }
                    s.copy(
                        limitRuleList = event.list,
                        showSetupDialog = event.list.isEmpty(),
                        weekDays = buildChipsForCreate(rules)
                    )
                }
            }

            is LimitRuleEvent.ShowSetupDialog -> {
                _state.update {
                    it.copy(
                        showSetupDialog = event.show
                    )
                }
            }
        }
    }

    private fun clearTime() {
        val rules = _state.value.limitRuleList.map { it.asHasWeekDays() }
        _state.update {
            it.copy(
                weekDays = buildChipsForClear(rules),
                hourMinute = HourMinute(),
                selectedLimitType = DayHour.DAY,
                currentId = null
            )
        }

    }

    fun toggleDay(
        chips: List<WeekDayChipUi>,
        day: WeekDay
    ): List<WeekDayChipUi> =
        chips.map { c ->
            if (c.day == day && c.enabled) c.copy(selected = !c.selected) else c
        }








    private fun LimitRuleState.selectedDaysFromChips(): Set<WeekDay> =
        weekDays.filter { it.selected }.mapTo(mutableSetOf()) { it.day }
    private fun saveUsageTime() {
        _state.update { innerState ->
            val selectedDays = innerState.selectedDaysFromChips()

            val timeList = innerState.limitRuleList.toMutableList()
            val isUpdate = innerState.currentId != null
            val id = innerState.currentId
                ?: (innerState.limitRuleList.maxOfOrNull { it.id }?.plus(1) ?: 1)

            val item = LimitRuleUi(
                id = id,
                weekDays = selectedDays,
                time = innerState.hourMinute,
                limitType = innerState.selectedLimitType
            )

            if (isUpdate) {
                val idx = timeList.indexOfFirst { it.id == innerState.currentId }
                if (idx >= 0) {
                    timeList[idx] = item
                } else {
                    timeList.add(item)
                }
            } else {
                timeList.add(item)
            }

            val rules = timeList.map { it.asHasWeekDays() }          // HasWeekDays adapter (oldin berganman)
            val rebuiltChips = buildChipsForCreate(rules)
            innerState.copy(
                currentId = null,
                limitRuleList = timeList,
                weekDays = rebuiltChips
            )

        }
        clearTime()
    }
}


private fun createTempList(): List<TimeRuleUi> {
    val list = mutableListOf<TimeRuleUi>()
    for (i in 0..20) {
        list.add(
            TimeRuleUi(
                id = i
            )
        )
    }
    return list
}

fun LocalTime.toHourMinuteString(): String {
    val h = this.hour
    val m = this.minute
    val hh = if (h < 10) "0$h" else "$h"
    val mm = if (m < 10) "0$m" else "$m"
    return "$hh:$mm"
}


fun LocalTime.toMinutes(): Int = hour * 60 + minute
fun HourMinute.toMinutes(): Int = hour * 60 + minute