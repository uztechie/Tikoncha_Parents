package org.example.project.presentation.add_child

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.example.project.presentation.login.LoginEvent
import org.example.project.presentation.login.LoginState

class ChildViewmodel(): ViewModel() {

    private val _state = MutableStateFlow(ChildState())
    val state = _state.asStateFlow()

    fun onEvent(event: ChildEvent){
        when(event){
            is ChildEvent.OnNumberInsert -> {
                _state.update {
                    it.copy(
                        number = event.number,
                        fullNumber = "+998${event.number}"
                    )
                }
            }

            ChildEvent.OnConfirmClicked -> {

            }
        }
    }
}