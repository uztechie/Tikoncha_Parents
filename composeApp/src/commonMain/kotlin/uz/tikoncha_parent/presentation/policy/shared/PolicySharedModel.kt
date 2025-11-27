package uz.tikoncha_parent.presentation.policy.shared

import androidx.lifecycle.ViewModel
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.domain.model.PolicyType
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.presentation.policy.policy_setup.PolicySetupEvent
import uz.tikoncha_parent.presentation.policy.policy_setup.PolicySetupState

class PolicySharedModel: ViewModel() {



    private val _state = MutableStateFlow(PolicySharedState())
    val state = _state.asStateFlow()

    init {
        refreshSubscriptionLimit()
        Logger.d("PolicySharedModel", "subscriptionLimit = ${_state.value.subscriptionLimit}")
    }

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
                        selectedPolicy = null,
                        policyTitle = "",
                        canUpdate = true,
//                        subscriptionLimitEntity = null
                    )
                }
            }

            is PolicySharedEvent.SetPolicy -> {
                _state.update {
                    it.copy(
                        limitList = event.policyItemUi.limitRule,
                        timeList = event.policyItemUi.timeRule,
                        selectedPolicy = event.policyItemUi,
                        policyTitle = event.policyItemUi.policyName,
                        canUpdate = event.policyItemUi.policyType == PolicyType.PARENT_CHILD
                    )
                }
            }

            is PolicySharedEvent.SetPolicyTitle -> {
                _state.update {
                    it.copy(
                        policyTitle = event.title
                    )
                }
                println(" policy = ${ _state.value.policyTitle }")
            }

            PolicySharedEvent.RefreshSubscriptionLimit -> {
                refreshSubscriptionLimit()
            }
        }
    }

    private fun refreshSubscriptionLimit(){
        _state.update {
            it.copy(
                subscriptionLimit = AppSettings.subscriptionLimit
            )
        }
    }
}