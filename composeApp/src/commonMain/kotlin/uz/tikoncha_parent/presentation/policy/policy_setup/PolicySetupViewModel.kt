package uz.tikoncha_parent.presentation.policy.policy_setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.mapper.toLimitRuleDtoList
import uz.tikoncha_parent.data.mapper.toTimeRuleDtoList
import uz.tikoncha_parent.data.remote.model.CreatePolicyRequest
import uz.tikoncha_parent.data.remote.model.UpdatePolicyRequest
import uz.tikoncha_parent.domain.model.PolicyType
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.policy.CreatePolicyUseCase
import uz.tikoncha_parent.domain.use_case.policy.DeletePolicyUseCase
import uz.tikoncha_parent.domain.use_case.policy.UpdatePolicyUseCase
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class PolicySetupViewModel(
    private val createPolicyUseCase: CreatePolicyUseCase,
    private val updatePolicyUseCase: UpdatePolicyUseCase,
    private val deletePolicyUseCase: DeletePolicyUseCase
): ViewModel() {


    private val TAG = "PolicySetupViewModel"
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
                        timeList = emptyList(),
                        responseState = ResponseState.Idle,
                        updateState = ResponseState.Idle,
                        deleteState = ResponseState.Idle
                    )
                }

            }
            is PolicySetupEvent.SavePolicy -> {
                if (state.value.selectedPolicyItemUi == null){
                    requestCreatePolicy()
                }
                else{
                    requestUpdatePolicy()
                }
            }

            is PolicySetupEvent.UpdatePackagesLint -> {
                _state.update {
                    it.copy(
                        packagesString = event.value,
                    )
                }
            }



            is PolicySetupEvent.SetSelectedApps -> {
                Logger.d(TAG, "onEvent: SetSelectedApps=${event.list}")
                _state.update {
                    it.copy(
                        selectedPackages = event.list
                    )
                }
            }
            is PolicySetupEvent.SetPolicy -> {
                _state.update {
                    it.copy(
                        selectedPolicyItemUi = event.policyItemUi
                    )
                }
            }

            is PolicySetupEvent.SetTitle -> {
                _state.update {
                    it.copy(
                        title = event.title
                    )
                }
            }

            PolicySetupEvent.DeletePolicy -> {
                requestDeletePolicy()
            }
            PolicySetupEvent.ResetResponseState -> {
                _state.update {
                    it.copy(
                        responseState = ResponseState.Idle,
                        updateState = ResponseState.Idle,
                        deleteState = ResponseState.Idle
                    )
                }
            }

            is PolicySetupEvent.SetSelectedChild -> {
                _state.update {
                    it.copy(
                        selectedChild = event.child
                    )
                }

            }
        }

    }





    private fun requestCreatePolicy(){
        viewModelScope.launch {
            _state.update {
                it.copy(
                    responseState = ResponseState.Loading
                )
            }


            val request = CreatePolicyRequest(
                policy_name = state.value.title,
                scope_id = state.value.selectedChild?.userId?:"",
                rule_name = state.value.title,
                scope_type = PolicyType.PARENT_CHILD.name,
                policy_is_active = true,
                resource_type = "APP",
                action = "DENY",
                priority = 101,
                packages = _state.value.selectedPackages,
                sites = emptyList(),
                time_rule = _state.value.timeList.toTimeRuleDtoList(),
                limit_rule = _state.value.limitList.toLimitRuleDtoList(),
                location_rule = null,
                wifi = null
            )

            val result = createPolicyUseCase.invoke(request)
            when(result){
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            responseState = ResponseState.Error(
                                message = result.message,
                                res = result.resId
                            )
                        )
                    }
                }
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            responseState = ResponseState.Success()
                        )
                    }
                }
            }

        }
    }

    private fun requestUpdatePolicy(){
        viewModelScope.launch {
            _state.update {
                it.copy(
                    updateState = ResponseState.Loading
                )
            }


            val request = UpdatePolicyRequest(
                name = state.value.title,
                resource_type = "APP",
                action = "DENY",
                priority = 101,
                packages = _state.value.selectedPackages,
                sites = emptyList(),
                time_rule = _state.value.timeList.toTimeRuleDtoList(),
                limit_rule = _state.value.limitList.toLimitRuleDtoList(),
                location_rule = null,
                wifi = null
            )

            val result = updatePolicyUseCase.invoke(request, state.value.selectedPolicyItemUi?.policyId?:"")
            when(result){
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            updateState = ResponseState.Error(
                                message = result.message,
                                res = result.resId
                            )
                        )
                    }
                }
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            updateState = ResponseState.Success()
                        )
                    }
                }
            }

        }
    }

    private fun requestDeletePolicy(){
        viewModelScope.launch {
            _state.update {
                it.copy(
                    deleteState = ResponseState.Loading
                )
            }


            val result = deletePolicyUseCase.invoke( state.value.selectedPolicyItemUi?.policyId?:"")
            when(result){
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            deleteState = ResponseState.Error(
                                message = result.message,
                                res = result.resId
                            )
                        )
                    }
                }
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            deleteState = ResponseState.Success()
                        )
                    }
                }
            }

        }
    }

}