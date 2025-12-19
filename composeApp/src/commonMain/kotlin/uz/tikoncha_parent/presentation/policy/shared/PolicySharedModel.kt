package uz.tikoncha_parent.presentation.policy.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.domain.model.PolicyType
import uz.tikoncha_parent.domain.model.SubscriptionLimit
import uz.tikoncha_parent.domain.use_case.payment.SubscriptionLimitUseCase
import uz.tikoncha_parent.platform.Logger

class PolicySharedModel(
    private val subscriptionLimitUseCase: SubscriptionLimitUseCase
): ViewModel() {



    private val _state = MutableStateFlow(PolicySharedState())
    val state = _state.asStateFlow()

    init {
        _state.update {
            it.copy(
                selectedChild = AppSettings.selectedChild
            )
        }
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
                        locationRule = event.policyItemUi.locationRule,
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
            PolicySharedEvent.LoadSubscriptionLimit -> {
                getSubscriptionLimit()
            }

            is PolicySharedEvent.SetSelectedChild -> {
                _state.update {
                    it.copy(
                        selectedChild = event.child
                    )
                }
                refreshSubscriptionLimit()
            }

            is PolicySharedEvent.SetLocationRule -> {
                _state.update {
                    it.copy(
                        locationRule = event.locationRule
                    )
                }
            }
        }
    }

    private fun getSubscriptionLimit(){
        viewModelScope.launch {
            val result = subscriptionLimitUseCase.invoke()
            refreshSubscriptionLimit()
        }
    }


    private fun refreshSubscriptionLimit(){
        _state.update { innerState->
            val selectedChild = innerState.selectedChild
            innerState.copy(
                subscriptionLimit = AppSettings.subscriptionLimitList.find { it.childId == selectedChild?.userId }?: SubscriptionLimit()
            )
        }
    }
}