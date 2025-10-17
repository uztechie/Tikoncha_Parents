package uz.tikoncha_parent.presentation.home.schedule.time

import kotlinx.datetime.LocalTime
import uz.tikoncha_parent.presentation.task.TaskEvent

sealed interface ScheduleEvent {

    data class OnTimeChange(val time: LocalTime) : ScheduleEvent
}