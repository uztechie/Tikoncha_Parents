package uz.tikoncha_parent.presentation.home.schedule.time

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ScheduleViewModel() : ViewModel() {

    private val _state = MutableStateFlow(ScheduleState())
    val state = _state.asStateFlow()

    fun onEvent(event: ScheduleEvent) {
        when (event) {
            is ScheduleEvent.OnTimeChange -> {
                _state.update {
                    it.copy(
                        time = event.time
                    )
                }
            }
        }
    }
}