@file:OptIn(ExperimentalResourceApi::class)

package uz.tikoncha_parent.presentation.policy.limit_rule

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import uz.tikoncha_parent.domain.model.DayHour
import uz.tikoncha_parent.domain.model.HourMinute
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleUi


class LimitRuleViewModel : ViewModel() {

    private val _state = MutableStateFlow(LimitRuleState())
    val state = _state.asStateFlow()

    init {

    }

    fun event(event: LimitRuleEvent) {
        when (event) {


            LimitRuleEvent.ClearData -> {
                clearTime()

            }


            is LimitRuleEvent.SelectWeekDay -> {
                _state.update { innerState ->

                    val set = innerState.weekDays.toMutableSet()

                    val added = set.add(event.usageDay)
                    if (!added) set.remove(event.usageDay)

                    innerState.copy(
                        weekDays = set.sortedBy { it.num }.toSet()
                    )
                }
            }

            is LimitRuleEvent.SaveLimit -> {
                saveUsageTime()
                clearUsageTime()
            }

            is LimitRuleEvent.RemoveLimitRule -> {
                _state.update {
                    it.copy(
                        limitRuleList = it.limitRuleList.filter { it != event.usageLimit }
                    )
                }
            }

            is LimitRuleEvent.SetUsageLimitData -> {
                val data = event.usageLimitData
                _state.update {
                    it.copy(
                        currentId = data.id,
                        hourMinute = data.time,
                        weekDays = data.weekDays,
                        selectedLimitType = data.limitType
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
                _state.update {
                    it.copy(
                        limitRuleList = event.list
                    )
                }
            }
        }
    }

    private fun clearTime() {
        _state.update {
            it.copy(
                weekDays = emptySet(),
                hourMinute = HourMinute(),
                selectedLimitType = DayHour.DAY,
                currentId = null
            )
        }

    }




    private fun clearUsageTime() {
        _state.update {
            it.copy(
                weekDays = emptySet(),
                hourMinute = HourMinute(),
                selectedLimitType = DayHour.DAY
            )
        }
    }

    private fun saveUsageTime() {
        _state.update { innerState ->


            val timeList = innerState.limitRuleList.toMutableList()
            val isUpdate = innerState.currentId != null
            val id = innerState.currentId
                ?: (innerState.limitRuleList.maxOfOrNull { it.id }?.plus(1) ?: 1)

            val item = LimitRuleUi(
                id = id,
                weekDays = innerState.weekDays,
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
            innerState.copy(
                currentId = null,
                limitRuleList = timeList
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