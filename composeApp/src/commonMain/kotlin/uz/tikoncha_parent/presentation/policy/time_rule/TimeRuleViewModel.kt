@file:OptIn(ExperimentalResourceApi::class)

package uz.tikoncha_parent.presentation.policy.time_rule

import androidx.lifecycle.ViewModel
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import uz.tikoncha_parent.data.mapper.buildTimeRanges
import uz.tikoncha_parent.domain.model.MinuteRange
import uz.tikoncha_parent.domain.model.WeekDay
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.presentation.policy.WeekDayChipUi
import uz.tikoncha_parent.presentation.policy.asHasWeekDays
import uz.tikoncha_parent.presentation.policy.buildChipsForClear
import uz.tikoncha_parent.presentation.policy.buildChipsForCreate
import uz.tikoncha_parent.presentation.policy.buildChipsForEdit
import uz.tikoncha_parent.presentation.policy.rule_type_selection.RuleType
import kotlin.compareTo
import kotlin.text.set


class TimeRuleViewModel: ScreenModel {

    private val _state = MutableStateFlow(TimeRuleState())
    val state = _state.asStateFlow()

    init {
        _state.update { s ->
            val rules = s.timeList.map { it.asHasWeekDays() }
            s.copy(weekDays = buildChipsForCreate(rules))
        }
    }

    fun event(event: TimeRuleEvent) {
        when (event) {
            is TimeRuleEvent.RemoveTimeRule -> {
                _state.update {s->
                    val newList = s.timeList.filter { it != event.time }
                    val rules = newList.map { it.asHasWeekDays() }
                    s.copy(
                        timeList = newList,
                        weekDays = buildChipsForCreate(rules)
                    )
                }
            }

            is TimeRuleEvent.SelectDay -> {
                _state.update { s -> s.copy(weekDays = toggleDay(s.weekDays, event.day)) }
            }

            is TimeRuleEvent.SetAllDay -> {
                _state.update {
                    it.copy(
                        allDay = event.allDay
                    )
                }
            }

            is TimeRuleEvent.SetOutsideInterval -> {
                _state.update { old ->
                    val newOutside = event.outside
                    val ranges = buildTimeRanges(
                        startTime = old.startTime,
                        endTime = old.endTime,
                        outside = newOutside
                    )
                    old.copy(
                        selectOutside = newOutside,
                        timeRanges = ranges
                    )
                }
            }

            is TimeRuleEvent.SetTimeRule -> {
                _state.update {
                    val ranges = buildTimeRanges(
                        startTime = event.startTime,
                        endTime = event.endTime,
                        outside = it.selectOutside
                    )

                    it.copy(
                        startTime = event.startTime,
                        endTime = event.endTime,
                        timeRanges = ranges
                    )
                }

            }

            is TimeRuleEvent.SaveTime -> {
                _state.update {
                    val ranges = buildTimeRanges(
                        startTime = it.startTime,
                        endTime = it.endTime,
                        outside = it.selectOutside
                    )
                    it.copy(
                        startTime = event.startTime,
                        endTime = event.endTime,
                        timeRanges = ranges
                    )
                }

                saveTime()
                clearTime()
                setItemEnabled(RuleType.TIME, true)
            }

            TimeRuleEvent.ClearTime -> {
                clearTime()

            }

            is TimeRuleEvent.SetTimeRuleData -> {
                val data = event.timeData
                val weekdays = _state.value.timeList.map { it.asHasWeekDays() }
                _state.update {
                    it.copy(
                        currentId = data.id,
                        startTime = data.startTime,
                        endTime = data.endTime,
                        timeRanges = data.timeRange,
                        allDay = data.allDay,
                        selectOutside = data.outside,
                        weekDays = buildChipsForEdit(
                            currentSelected = data.weekDays,
                            rules = weekdays,
                            excludeId = data.id
                        )
                    )
                }
            }

            is TimeRuleEvent.SetList -> {
                _state.update {s->
                    val rules = event.list.map { it.asHasWeekDays() }
                    s.copy(
                        timeList = event.list,
                        showSetupDialog = event.list.isEmpty(),
                        weekDays = buildChipsForCreate(rules = rules)
                    )
                }
            }

            is TimeRuleEvent.ShowSetupDialog -> {
                _state.update {
                    it.copy(
                        showSetupDialog = event.show
                    )
                }
            }

            is TimeRuleEvent.BeginCreateRule -> {

            }

            TimeRuleEvent.OpenCreate -> {
                val rules = _state.value.timeList.map { it.asHasWeekDays() }
                _state.update {
                    it.copy(
                        currentId = null,
                        allDay = false,
                        selectOutside = false,
                        startTime = LocalTime(8, 0),
                        endTime = LocalTime(12, 0),
                        timeRanges = emptyList(),
                        weekDays = buildChipsForCreate(rules),
                        showSetupDialog = true
                    )
                }
            }
            is TimeRuleEvent.OpenEdite -> {
                val data = event.timeData
                val weekdays = _state.value.timeList.map { it.asHasWeekDays() }

                _state.update {
                    it.copy(
                        currentId = data.id,
                        allDay = data.allDay,
                        selectOutside = data.outside,
                        startTime = data.startTime,
                        endTime = data.endTime,
                        timeRanges = data.timeRange,
                        weekDays = buildChipsForEdit(
                            currentSelected = data.weekDays,
                            rules = weekdays,
                            excludeId = data.id
                        ),
                        showSetupDialog = true
                    )
                }
            }
        }
    }

    private fun clearTime() {
        val rules = _state.value.timeList.map { it.asHasWeekDays() }

        _state.update {
            it.copy(
                allDay = false,
                selectOutside = false,
                startTime = LocalTime(8, 0),
                endTime = LocalTime(12, 0),
                timeRanges = emptyList(),
                weekDays = buildChipsForClear(rules)
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


    private fun TimeRuleState.selectedDaysFromChips(): Set<WeekDay> =
        weekDays.asSequence()
            .filter { it.selected }
            .map { it.day }
            .toSet()

    private fun saveTime() {
        _state.update { innerState ->
            val timeList = innerState.timeList.toMutableList()
            val selectedDays = innerState.selectedDaysFromChips()



            val isUpdate = innerState.currentId != null
            val id = innerState.currentId
                ?: (innerState.timeList.maxOfOrNull { it.id }?.plus(1)
                    ?: 1) // Random o'rniga deterministik id

            val item = TimeRuleUi(
                id = id,
                time = "${innerState.startTime.toHourMinuteString()} - ${innerState.endTime.toHourMinuteString()}",
                weekDays = selectedDays,
                timeRange = innerState.timeRanges,
                allDay = innerState.allDay,
                outside = innerState.selectOutside,
                startTime = innerState.startTime,
                endTime = innerState.endTime
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
                timeList = timeList,
                currentId = null,
                startTime = LocalTime(8, 0),
                endTime = LocalTime(12, 0),
                allDay = false,
                selectOutside = false,
                timeRanges = emptyList(),
                weekDays = rebuiltChips
            )
        }
    }




    private fun dailyViewingRanges(
        dayHour: LocalTime,
        hourly: LocalTime,
        outside: Boolean
    ): List<MinuteRange> {
        val s = dayHour.toMinutes().coerceIn(0, 1440)
        val e = hourly.toMinutes().coerceIn(0, 1440)

        return if (!outside) {
            when {
                s == e -> emptyList()
                s < e -> listOf(MinuteRange(s, e))
                else -> {
                    // Agar foydalanuvchi "ichki" oraliqni kesishib kechaga o'tadigan qilsa (mas: 22:00-03:00),
                    // uni ikkiga bo'lib qaytarish ham mumkin; lekin odatda ichki oraliqni s<e qilib cheklab qo'yish tavsiya.
                    listOf(MinuteRange(s, 1440), MinuteRange(0, e))
                }

            }
        } else {
            when {
                s == e -> listOf(MinuteRange(0, 1440))
                s < e -> {
                    val left = if (s > 0) MinuteRange(0, s) else null
                    val right = if (e < 1440) MinuteRange(e, 1440) else null
                    listOfNotNull(left, right)
                }

                else -> {
                    // s > e bo'lsa (mas: 22:00-03:00) ichki oraliq kechani kesib o'tgan bo'ladi,
                    // demak tashqarisi faqat (e, s) oralig'i. Uni bitta bo'lak qilib qaytaramiz.
                    listOf(MinuteRange(e, s))
                }
            }
        }

    }


    private fun setItemEnabled(type: RuleType, enabled: Boolean) {
//        _state.update {
//            it.copy(
//                enabledByType = it.enabledByType.toMutableMap().apply {
//                    this[type] = enabled
//                }
//            )
//        }
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

    private fun LocalTime.toMinutes(): Int = hour * 60 + minute
}