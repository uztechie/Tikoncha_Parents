@file:OptIn(ExperimentalResourceApi::class)

package uz.tikoncha_parent.presentation.home.schedule.timelist

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.ResourceReader
import uz.tikoncha_parent.domain.model.MinuteRange
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.presentation.home.schedule.type.ScheduleType


class ScheduleTimeViewModel: ViewModel() {

    private val _state = MutableStateFlow(ScheduleTimeState())
    val state = _state.asStateFlow()

    init {
        _state.update {
            it.copy(
                timeList = createTempList()
            )
        }
    }

    fun event(event: ScheduleTimeEvent){
        when(event){
            is ScheduleTimeEvent.RemoveTime -> {
                _state.update {
                    it.copy(
                        timeList = it.timeList.filter { it != event.time }
                    )
                }
            }

            is ScheduleTimeEvent.SelectDay -> {
                _state.update { innerState ->

                    val set = innerState.selectedDays.toMutableSet()

                    val added = set.add(event.day)
                    if (!added) set.remove(event.day)

                    innerState.copy(
                        selectedDays = set.sortedBy { it.num }.toSet()
                    )
                }
            }
            is ScheduleTimeEvent.SetAllDay -> {
                _state.update {
                    it.copy(
                        allDay = event.allDay
                    )
                }
            }
            is ScheduleTimeEvent.SetOutsideInterval -> {
                _state.update {old->
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
            is ScheduleTimeEvent.SetTime -> {
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

            ScheduleTimeEvent.SaveTime -> {
                saveTime()
                clearTime()
                setItemEnabled(ScheduleType.TIME, true)
            }

            ScheduleTimeEvent.ClearTime -> {
                clearTime()

            }

            is ScheduleTimeEvent.SetTimeData -> {
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

            is ScheduleTimeEvent.SetUsageLimitTime -> {
                _state.update {
                    it.copy(
                        startTime = event.time,
                    )
                }
            }

            is ScheduleTimeEvent.SelectUsageDay -> {
                _state.update { innerState ->

                    val set = innerState.usageLimitDays.toMutableSet()

                    val added = set.add(event.usageDay)
                    if (!added) set.remove(event.usageDay)

                    innerState.copy(
                        usageLimitDays = set.sortedBy { it.num }.toSet()
                    )
                }
            }

            is ScheduleTimeEvent.SaveLimit -> {
                saveUsageTime()
                clearUsageTime()
                setItemEnabled(ScheduleType.USAGE_LIMIT, true)
            }

            is ScheduleTimeEvent.RemoveUsageLimit -> {
                _state.update {
                    it.copy(
                        usageTimeList = it.usageTimeList.filter { it != event.usageLimit }
                    )
                }
            }
            is ScheduleTimeEvent.SetUsageLimitData -> {
                val data = event.usageLimitData
                _state.update {
                    it.copy(
                        currentId = data.id,
                        dayHour = data.time,
                        usageLimitDays = data.weekDays,
                        isDailyUsage = data.isDailyUsage
                    )
                }
            }
            is ScheduleTimeEvent.SetUsageType ->  {
                _state.update {
                    it.copy(
                        isDailyUsage = event.isDaily
                    )
                }
            }

            is ScheduleTimeEvent.SelectLimitType -> {
                _state.update {
                    it.copy(
                        selectedLimitType = event.dayHour
                    )
                }
            }
        }
    }

    private fun clearTime(){
        _state.update {
            it.copy(
                selectedDays = emptySet(),
                allDay = false,
                selectOutside = false,
                startTime = LocalTime(8,0),
                endTime = LocalTime(12, 0),
                timeRanges = emptyList(),
            )
        }
    }


    private fun saveTime(){
        _state.update {innerState->
            val timeList = innerState.timeList.toMutableList()

            val isUpdate = innerState.currentId != null
            Logger.d("", "innerState.currentId = ${innerState.currentId}")
            val id = innerState.currentId
                ?: (innerState.timeList.maxOfOrNull { it.id }?.plus(1) ?: 1) // Random o'rniga deterministik id

            val item = ScheduleTimeUi(
                id = id,
                time = "${innerState.startTime.toHourMinuteString()} - ${innerState.endTime.toHourMinuteString()}",
                weekDays = innerState.selectedDays,
                timeRange = innerState.timeRanges,
                allDay = innerState.allDay,
                outside = innerState.selectOutside,
                startTime = innerState.startTime,
                endTime = innerState.endTime
            )

            if (isUpdate){
                val idx = timeList.indexOfFirst { it.id == innerState.currentId }
                if (idx >= 0){
                    timeList[idx] = item
                }
                else{
                    timeList.add(item)
                }
            }
            else{
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
                else   -> {
                    // Agar foydalanuvchi "ichki" oraliqni kesishib kechaga o'tadigan qilsa (mas: 22:00-03:00),
                    // uni ikkiga bo'lib qaytarish ham mumkin; lekin odatda ichki oraliqni s<e qilib cheklab qo'yish tavsiya.
                    listOf(MinuteRange(s, 1440), MinuteRange(0, e))
                }

            }
        }
        else{
            when {
                s == e -> listOf(MinuteRange(0, 1440))
                s < e -> {
                    val left = if (s>0) MinuteRange(0, s) else null
                    val right = if (e<1440) MinuteRange(e, 1440) else null
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
                else   -> {
                    // Agar foydalanuvchi "ichki" oraliqni kesishib kechaga o'tadigan qilsa (mas: 22:00-03:00),
                    // uni ikkiga bo'lib qaytarish ham mumkin; lekin odatda ichki oraliqni s<e qilib cheklab qo'yish tavsiya.
                    listOf(MinuteRange(s, 1440), MinuteRange(0, e))
                }

            }
        }
        else{
            when {
                s == e -> listOf(MinuteRange(0, 1440))
                s < e -> {
                    val left = if (s>0) MinuteRange(0, s) else null
                    val right = if (e<1440) MinuteRange(e, 1440) else null
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


    private fun setItemEnabled(type: ScheduleType, enabled: Boolean){
        _state.update {
            it.copy(
                enabledByType = it.enabledByType.toMutableMap().apply {
                    this[type] = enabled
                }
            )
        }
    }
    private fun clearUsageTime(){
        _state.update {
            it.copy(
                usageLimitDays = emptySet(),
                dayHour = LocalTime(8,0),
            )
        }
    }
    private fun saveUsageTime( res: ResourceReader){
        _state.update {innerState->


            val timeList = innerState.usageTimeList.toMutableList()
            val isUpdate = innerState.currentId != null
            val id = innerState.currentId
                ?: (innerState.usageTimeList.maxOfOrNull { it.id }?.plus(1) ?: 1)

            val item = ScheduleUsageLimitUi(
                id = id,
                weekDays = innerState.usageLimitDays,
                time = innerState.dayHour,
                isDailyUsage = innerState.isDailyUsage,
            )

            if (isUpdate){
                val idx = timeList.indexOfFirst { it.id == innerState.currentId }
                if (idx >= 0){
                    timeList[idx] = item
                }
                else{
                    timeList.add(item)
                }
            }
            else{
                timeList.add(item)
            }
            innerState.copy(
                currentId = null,
                usageTimeList = timeList
            )
        }
    }
}



private fun createTempList(): List<ScheduleTimeUi>{
    val list = mutableListOf<ScheduleTimeUi>()
    for (i in 0..20){
        list.add(
            ScheduleTimeUi(
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