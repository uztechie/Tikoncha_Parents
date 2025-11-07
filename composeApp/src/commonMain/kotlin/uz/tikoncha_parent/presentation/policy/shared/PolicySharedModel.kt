package uz.tikoncha_parent.presentation.policy.shared

import androidx.lifecycle.ViewModel
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import uz.tikoncha_parent.presentation.policy.policy_setup.PolicySetupEvent
import uz.tikoncha_parent.presentation.policy.policy_setup.PolicySetupState

class PolicySharedModel: ViewModel() {

    private val _state = MutableStateFlow(PolicySharedState())
    val state = _state.asStateFlow()

    fun onEvent(event: PolicySharedEvent){
        when(event){
            is PolicySharedEvent.SetLimitRule -> {
                _state.value = _state.value.copy(
                    limitList = event.list
                )
            }
            is PolicySharedEvent.SetTimeRule -> {
                _state.value = _state.value.copy(
                    timeList = event.list
                )

            }

            PolicySharedEvent.ClearData -> {
                _state.update {
                    it.copy(
                        limitList = emptyList(),
                        timeList = emptyList(),
                    )
                }
            }

            is PolicySharedEvent.SetSelectedChild -> {

            }
        }
    }
}