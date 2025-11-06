@file:OptIn(ExperimentalResourceApi::class)

package uz.tikoncha_parent.presentation.policy.time_rule

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import uz.tikoncha_parent.domain.model.MinuteRange
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.presentation.policy.rule_type_selection.RuleType


class TimeRuleViewModel: ViewModel() {

    private val _state = MutableStateFlow(TimeRuleState())
    val state = _state.asStateFlow()

    fun event(event: TimeRuleEvent) {
        when (event) {
            is TimeRuleEvent.RemoveTimeRule -> {
                _state.update {
                    it.copy(
                        timeList = it.timeList.filter { it != event.time }
                    )
                }
            }

            is TimeRuleEvent.SelectDay -> {
                _state.update { innerState ->

                    val set = innerState.selectedDays.toMutableSet()

                    val added = set.add(event.day)
                    if (!added) set.remove(event.day)

                    innerState.copy(
                        selectedDays = set.sortedBy { it.num }.toSet()
                    )
                }
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
                    val ranges = buildRanges(
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
                    val ranges = buildRanges(
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

            TimeRuleEvent.SaveTime -> {
                saveTime()
                clearTime()
                setItemEnabled(RuleType.TIME, true)
            }

            TimeRuleEvent.ClearTime -> {
                clearTime()

            }

            is TimeRuleEvent.SetTimeRuleData -> {
                val data = event.timeData
                _state.update {
                    it.copy(
                        currentId = data.id,
                        startTime = data.startTime,
                        endTime = data.endTime,
                        selectedDays = data.weekDays,
                        timeRanges = data.timeRange,
                        allDay = data.allDay,
                        selectOutside = data.outside,
                    )
                }
            }

            is TimeRuleEvent.SetList -> {
                _state.update {
                    it.copy(
                        timeList = event.list
                    )
                }
            }
        }
    }

    private fun clearTime() {
        _state.update {
            it.copy(
                selectedDays = emptySet(),
                allDay = false,
                selectOutside = false,
                startTime = LocalTime(8, 0),
                endTime = LocalTime(12, 0),
                timeRanges = emptyList(),
            )
        }
    }


    private fun saveTime() {
        _state.update { innerState ->
            val timeList = innerState.timeList.toMutableList()

            val isUpdate = innerState.currentId != null
            Logger.d("", "innerState.currentId = ${innerState.currentId}")
            val id = innerState.currentId
                ?: (innerState.timeList.maxOfOrNull { it.id }?.plus(1)
                    ?: 1) // Random o'rniga deterministik id

            val item = TimeRuleUi(
                id = id,
                time = "${innerState.startTime.toHourMinuteString()} - ${innerState.endTime.toHourMinuteString()}",
                weekDays = innerState.selectedDays,
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
            innerState.copy(
                currentId = null,
                timeList = timeList
            )
        }
    }

    private fun buildRanges(
        startTime: LocalTime,
        endTime: LocalTime,
        outside: Boolean
    ): List<MinuteRange> {
        val s = startTime.toMinutes().coerceIn(0, 1440)
        val e = endTime.toMinutes().coerceIn(0, 1440)

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