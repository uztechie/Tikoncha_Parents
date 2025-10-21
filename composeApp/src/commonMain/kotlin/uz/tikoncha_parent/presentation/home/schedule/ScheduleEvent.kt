package uz.tikoncha_parent.presentation.home.schedule

sealed class ScheduleEvent {
    data class OnGenderSelected(val genderIndex: Int): ScheduleEvent()

}