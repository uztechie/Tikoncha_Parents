package uz.tikoncha_parent.presentation.home.schedule.timelist

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalTime
import uz.tikoncha_parent.domain.model.MinuteRange


class ScheduleTimeViewModel: ViewModel() {

    private val _state = MutableStateFlow(ScheduleTimeState())
    val state = _state.asStateFlow()

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
            is ScheduleTimeEvent.SetTime -> {
                _state.update {

                    val timeRange = MinuteRange(
                        start = event.startTime.hour * 60 + event.startTime.minute,
                        end = event.endTime.hour * 60 + event.endTime.minute
                    )


                    it.copy(
                        startTime = event.startTime,
                        endTime = event.endTime,
                        timeRanges = listOf(timeRange)
                    )
                }
            }

            ScheduleTimeEvent.SaveTime -> {
                saveTime()
                clearTime()
            }

            ScheduleTimeEvent.ClearTime -> {
                clearTime()

            }

        }
    }

    private fun clearTime(){
        _state.update {
            it.copy(
                selectedDays = emptySet(),
                allDay = false,
                startTime = LocalTime(8,0),
                endTime = LocalTime(12, 0),
                timeRanges = emptyList(),
            )
        }
    }


    private fun saveTime(){
        _state.update {innerState->
            val timeList = innerState.timeList.toMutableList()
            timeList.add(
                ScheduleTimeUi(
                    time = "${innerState.startTime.toHourMinuteString()} - ${innerState.endTime.toHourMinuteString()}",
                    weekDays = innerState.selectedDays,
                    timeRange = innerState.timeRanges
                )
            )
            innerState.copy(
                timeList = timeList
            )
        }
    }
}

fun LocalTime.toHourMinuteString(): String {
    val h = this.hour
    val m = this.minute
    val hh = if (h < 10) "0$h" else "$h"
    val mm = if (m < 10) "0$m" else "$m"
    return "$hh:$mm"
}