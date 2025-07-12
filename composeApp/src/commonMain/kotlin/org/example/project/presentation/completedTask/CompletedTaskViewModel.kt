package org.example.project.presentation.completedTask

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.example.project.presentation.register.RegisterEvent
import org.example.project.presentation.register.RegisterState

class CompletedTaskViewModel() : ViewModel() {
    private val _state = MutableStateFlow(CompletedTaskState())
    val state = _state.asStateFlow()

    fun onEvent(event: CompletedTaskEvent) {
        when (event) {
            is CompletedTaskEvent.OnGenderSelected -> {
                _state.update {
                    it.copy(
                        genderIndex = event.genderIndex
                    )
                }
            }
        }
    }
}