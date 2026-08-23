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
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.model.SubscriptionLimit
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.model.permission_status.PermissionStatusType
import uz.tikoncha_parent.domain.repository.ChildRepository
import uz.tikoncha_parent.domain.repository.PermissionStatusRepository
import uz.tikoncha_parent.domain.use_case.GetPoliciesFromServerUseCase
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class PolicyViewModel(
    private val getPoliciesFromServerUseCase: GetPoliciesFromServerUseCase,
    private val permissionStatusRepository: PermissionStatusRepository,
    private val childRepository: ChildRepository
) : ScreenModel {

    private var childrenJob: Job? = null
    private val TAG = "PolicyViewModel"
    private val _state = MutableStateFlow<PolicyState>(PolicyState())
    val state = _state.asStateFlow()

    init {
        _state.update {
            it.copy(
                selectedChild = AppSettings.selectedChild,
                showPolicyTutorialCard = AppSettings.showPolicyTutorial
            )
        }

        loadPermissionStatus()
    }


    fun onEvent(event: PolicyEvent) {
        when (event) {
            PolicyEvent.RefreshPolicies -> {
                getSubscriptionLimit()
                loadPermissionStatus()
                getPolicies()

            }

            PolicyEvent.GetChildren -> {
                loadChildren()
            }

            is PolicyEvent.OnChildSelected -> {
                _state.update {
                    it.copy(selectedChild = event.child)
                }
                AppSettings.selectedChildId = event.child.userId
                AppSettings.selectedChild = event.child
                getSubscriptionLimit()
                getPolicies()
            }

            is PolicyEvent.OnTypeSelected -> {
                _state.update {
                    it.copy(
                        selectedTypeIndex = event.index
                    )
                }
            }
        }
    }

    private fun loadChildren() {
        childrenJob?.cancel()
        childrenJob = screenModelScope.launch {
            _state.update {
                it.copy(
                    childrenResponseState = ResponseState.Loading
                )
            }

            when (val res = childRepository.children()) {
                is Outcome.Failure -> _state.update {
                    it.copy(childrenResponseState = ResponseState.Error(failure = res))
                }

                is Outcome.Success -> {
                    val children = res.data

                    // ✅ AppSettings + selectedChild sync
                    AppSettings.syncSelectedChildWith(children)

                    if (children.isEmpty()) {
                        AppSettings.selectedChild = null
                        AppSettings.selectedChildId = ""
                    }

                    _state.update {
                        it.copy(
                            childrenResponseState = ResponseState.Success(),
                            childrenList = AppSettings.children,
                            selectedChild = AppSettings.selectedChild
                        )
                    }
                }
            }
        }
    }

    private fun getSubscriptionLimit() {
        screenModelScope.launch {
            refreshSubscriptionLimit()
        }
    }

    private fun refreshSubscriptionLimit() {
        _state.update { current ->
            val childId = current.selectedChild?.userId
            val limit = AppSettings.subscriptionLimitList
                .find { it.childId == childId }
                ?: SubscriptionLimit()
            current.copy(subscriptionLimit = limit)
        }
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
                            .map { it.toPolicyListUi().copy(isActive = innerState.permissionIssueList.isEmpty()) }
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

    private var permissionJob: Job? = null
    private fun loadPermissionStatus() {
        permissionJob?.cancel()
        permissionJob = screenModelScope.launch {
            when (val res = permissionStatusRepository.permissionStatus(
                childId = _state.value.selectedChild?.userId ?: "",
                state = PermissionStatusType.POLICY,
            )) {
                is Outcome.Success -> {
                    val issues = res.data
                    val hasIssues = issues.isNotEmpty()
                    _state.update { currentState ->
                        currentState.copy(
                            permissionIssueList = issues,
                            policies = currentState.policies.map { it.copy(isActive = !hasIssues) }
                        )
                    }
                }
                is Outcome.Failure -> _state.update {
                    it.copy(permissionIssueList = emptyList())
                }
            }
        }
    }
}