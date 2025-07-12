package org.example.project.presentation.task

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TaskViewModel () : ViewModel() {

    private val _state = MutableStateFlow(TaskState())
    val state = _state.asStateFlow()


    fun onEvent(event: TaskEvent) {
        when (event) {
            is TaskEvent.OnDateChange -> {
                _state.update {
                    it.copy(
                        date = event.date
                    )
                }
            }

            is TaskEvent.OnTimeChange -> {
                _state.update {
                    it.copy(
                        time = event.time
                    )
                }
            }

            is TaskEvent.OnDescChange -> {
                _state.update {
                    it.copy(
                        desc = event.desc
                    )
                }
            }

            is TaskEvent.OnImportanceChange -> {
                _state.update {
                    it.copy(
                        importance = event.importance
                    )
                }
            }

            is TaskEvent.OnTitleChange -> {
                _state.update {
                    it.copy(
                        title = event.title
                    )
                }
            }

            is TaskEvent.OnChildSelected -> {
                _state.update {
                    it.copy(
                        child = event.child
                    )
                }
            }

            is TaskEvent.OnGenderSelected -> {
                _state.update {
                    it.copy(
                        genderIndex = event.genderIndex
                    )
                }
            }
        }
    }
}
