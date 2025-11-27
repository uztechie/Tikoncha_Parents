package uz.tikoncha_parent.presentation.policy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.mapper.toAppSelectionUi
import uz.tikoncha_parent.data.mapper.toPolicyListUi
import uz.tikoncha_parent.domain.model.PolicyType
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.GetPoliciesFromServerUseCase
import uz.tikoncha_parent.domain.use_case.payment.SubscriptionLimitUseCase
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class PolicyViewModel(
    private val getPoliciesFromServerUseCase: GetPoliciesFromServerUseCase,
    private val subscriptionLimitUseCase: SubscriptionLimitUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<PolicyState>(PolicyState())
    val state = _state.asStateFlow()

    init {
        _state.update {
            it.copy(
                subscriptionLimit = AppSettings.subscriptionLimit
            )
        }
        getSubscriptionLimit()
    }


    fun onEvent(event: PolicyEvent){
        when(event){
            is PolicyEvent.SetSelectedChild -> {
                _state.value = _state.value.copy(
                    selectedChild = event.child
                )
                getPolicies()
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
        _state.update {
            it.copy(
                subscriptionLimit = AppSettings.subscriptionLimit
            )
        }
    }


    private var policyJob: Job? = null
    private fun getPolicies(){
        policyJob?.cancel()
        policyJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    policyResponseState = ResponseState.Loading
                )
            }

            val result = getPoliciesFromServerUseCase.invoke(_state.value.selectedChild?.userId?:"")

            when(result){
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            policyResponseState = ResponseState.Error(message = result.message)
                        )
                    }
                }
                is Resource.Success -> {

                    _state.update {
                        it.copy(
                            policyResponseState = ResponseState.Success(),
                            policies = result.data
                                .map { it.toPolicyListUi() }
                                .sortedByDescending { it.policyType.order }
                        )
                    }
                }
            }
        }
    }


}