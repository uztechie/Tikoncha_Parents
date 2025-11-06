package uz.tikoncha_parent.presentation.policy

sealed class ScheduleEvent {
    data class OnGenderSelected(val genderIndex: Int): ScheduleEvent()

}