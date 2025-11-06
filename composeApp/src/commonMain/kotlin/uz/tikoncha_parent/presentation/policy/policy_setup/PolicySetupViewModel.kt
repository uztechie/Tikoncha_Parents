package uz.tikoncha_parent.presentation.policy.policy_setup

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PolicySetupViewModel(): ViewModel() {
    private val _state = MutableStateFlow(PolicySetupState())
    val state = _state.asStateFlow()

    fun onEvent(event: PolicySetupEvent){
        when(event){
            is PolicySetupEvent.SetLimitRule -> {
                _state.value = _state.value.copy(
                    limitList = event.list
                )
            }
            is PolicySetupEvent.SetTimeRule -> {
                _state.value = _state.value.copy(
                    timeList = event.list
                )

            }

            PolicySetupEvent.ClearData -> {
                _state.update {
                    it.copy(
                        limitList = emptyList(),
                        timeList = emptyList()
                    )
                }

            }
        }
    }
}