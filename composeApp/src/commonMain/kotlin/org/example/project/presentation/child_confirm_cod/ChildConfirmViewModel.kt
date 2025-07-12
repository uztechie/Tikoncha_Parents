package org.example.project.presentation.child_confirm_cod

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChildConfirmViewModel(): ViewModel() {

    private val _state = MutableStateFlow(ChildConfirmState())
    val state = _state.asStateFlow()

    fun onEvent(event: ChildConfirmEvent){
        when(event){
            is ChildConfirmEvent.OnNumberCod -> {
                _state.update {
                    it.copy(
                        codNumber = event.codNumber.toString()
                    )
                }
            }
        }
    }
}