package uz.tikoncha_parent.presentation.policy.policy_setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.hammasi
import uz.tikoncha_parent.data.mapper.toLimitRuleDtoList
import uz.tikoncha_parent.data.mapper.toTimeRuleDtoList
import uz.tikoncha_parent.data.remote.model.CreatePolicyRequest
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.CreatePolicyUseCase
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.presentation.policy.limit_rule.LimitRuleUi
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleUi
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class PolicySetupViewModel(
    private val policyUseCase: CreatePolicyUseCase
): ViewModel() {
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
                    )
                }
            }
            is PolicySetupEvent.SavePolicy -> {
                requestCreatePolicy()
            }

            is PolicySetupEvent.SetSelectedChild -> {
                _state.update {
                    it.copy(
                        selectedChild = event.child
                    )
                }
            }

            is PolicySetupEvent.UpdatePackagesLint -> {
                _state.update {
                    it.copy(
                        packagesString = event.value
                    )
                }
            }
        }

    }





    private fun requestCreatePolicy(){
        Logger.d("requestCreatePolicy", "requestCreatePolicy")
        viewModelScope.launch {
            _state.update {
                it.copy(
                    responseState = ResponseState.Loading
                )
            }

            val packages = _state.value.packagesString
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }


            if (packages.isEmpty()){
                _state.update {
                    it.copy(
                        responseState = ResponseState.Error(
                            res = Res.string.hammasi,
                            message = "Insert packages"
                        )
                    )
                }
                return@launch
            }

            val request = CreatePolicyRequest(
                scope_id = _state.value.selectedChild?.userId?:"",
                policy_name = "Parent to child policy",
                scope_type = "PARENT_CHILD",
                policy_is_active = true,
                resource_type = "APP",
                action = "DENY",
                priority = 100,
                packages = packages,
                sites = emptyList(),
                time_rule = _state.value.timeList.toTimeRuleDtoList(),
                limit_rule = _state.value.limitList.toLimitRuleDtoList(),
                location_rule = null,
                wifi = null
            )

            val result = policyUseCase.invoke(request)
            when(result){
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            responseState = ResponseState.Error(
                                res = result.resId,
                                message = result.message
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
}