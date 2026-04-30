package uz.tikoncha_parent.presentation.policy.policy_list

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.mapper.toPolicyListUi
import uz.tikoncha_parent.data.remote.model.permission_status.PermissionStatusRequest
import uz.tikoncha_parent.domain.model.PolicyType
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.model.SubscriptionLimit
import uz.tikoncha_parent.domain.model.permission_status.PermissionStatusType
import uz.tikoncha_parent.domain.use_case.GetPoliciesFromServerUseCase
import uz.tikoncha_parent.domain.use_case.payment.SubscriptionLimitUseCase
import uz.tikoncha_parent.domain.use_case.permission_status.PermissionStatusUseCase
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class PolicyViewModel(
    private val getPoliciesFromServerUseCase: GetPoliciesFromServerUseCase,
    private val subscriptionLimitUseCase: SubscriptionLimitUseCase,
    private val permissionStatusUseCase: PermissionStatusUseCase
) : ScreenModel {

    private val TAG = "PolicyViewModel"
    private val _state = MutableStateFlow<PolicyState>(PolicyState())
    val state = _state.asStateFlow()

    init {
        _state.update {
            it.copy(
                selectedChild = AppSettings.selectedChild
            )
        }

        loadPermissionStatus()
    }


    fun onEvent(event: PolicyEvent) {
        when (event) {
            PolicyEvent.RefreshPolicies -> {
                getSubscriptionLimit()
                getPolicies()
            }
        }
    }


    private fun getSubscriptionLimit() {
        screenModelScope.launch {
            refreshSubscriptionLimit()
        }
    }

    private fun refreshSubscriptionLimit() {
        _state.update {
            it.copy(
                subscriptionLimit = AppSettings.subscriptionLimitList.find { limit -> limit.childId == state.value.selectedChild?.userId }
                    ?: SubscriptionLimit()
            )
        }
        Logger.d(TAG, " subscriptionLimit=${_state.value.subscriptionLimit}")

    }


    private var policyJob: Job? = null
    private fun getPolicies() {
        policyJob?.cancel()
        policyJob = screenModelScope.launch {

            if (!_state.value.isInitialLoadDone) {
                _state.update {
                    it.copy(
                        policyResponseState = ResponseState.Loading
                    )
                }
            }

            val result = getPoliciesFromServerUseCase.invoke(_state.value.selectedChild?.userId ?: "")

            when (result) {
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            policyResponseState = ResponseState.Error(message = result.message),
                            isInitialLoadDone = true
                        )
                    }
                }

                is Resource.Success -> {

                    _state.update { innerState ->
                        val policies = result.data
                            .map { it.toPolicyListUi() }
                            .sortedByDescending { it.policyType.order }

                        innerState.copy(
                            policyResponseState = ResponseState.Success(),
                            policies = policies,
                            isInitialLoadDone = true
                        )
                    }
                }
            }
        }
    }

    private fun loadPermissionStatus() {
        screenModelScope.launch {
            val res = permissionStatusUseCase.invoke(
                PermissionStatusRequest(
                    userId = _state.value.selectedChild?.userId?:"",
                    state = PermissionStatusType.POLICY.name
                )
            )
            when (res) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            permissionIssueList = res.data.issues
                        )
                    }
                }
                is Resource.Error -> {
                    // Xato — issue ko'rsatmaymiz, loading'ni yopamiz
                    _state.update {
                        it.copy(
                            permissionIssueList = emptyList()
                        )
                    }
                }
                else -> Unit
            }
        }
    }


}