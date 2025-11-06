package uz.tikoncha_parent.presentation.policy


data class ScheduleState(
    val appWebSelectionIndex: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)
