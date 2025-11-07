package uz.tikoncha_parent.presentation.policy

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PolicyViewModel : ViewModel() {

    private val _state = MutableStateFlow<PolicyState>(PolicyState())
    val state = _state.asStateFlow()

    fun onEvent(event: PolicyEvent){
        when(event){
            is PolicyEvent.SetSelectedChild -> {
                _state.value = _state.value.copy(
                    selectedChild = event.child
                )
            }
        }
    }


}