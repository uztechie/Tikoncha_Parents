package uz.tikoncha_parent.presentation.policy.policy_list

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlin.time.Clock
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.domain.model.SubscriptionLimit
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.model.permission_status.PermissionStatusType
import uz.tikoncha_parent.domain.repository.ChildRepository
import uz.tikoncha_parent.domain.repository.PermissionStatusRepository
import uz.tikoncha_parent.domain.use_case.policy.ObservePoliciesUseCase
import uz.tikoncha_parent.domain.use_case.policy.RefreshPoliciesUseCase
import uz.tikoncha_parent.presentation.policy.toItemUi
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class PolicyViewModel(
    private val observePolicies: ObservePoliciesUseCase,
    private val refreshPolicies: RefreshPoliciesUseCase,
    private val permissionStatusRepository: PermissionStatusRepository,
    private val childRepository: ChildRepository,
) : ScreenModel {

    private val _state = MutableStateFlow(PolicyState())
    val state = _state.asStateFlow()

    private var observeJob: Job? = null
    private var policyJob: Job? = null
    private var permissionJob: Job? = null
    private var childrenJob: Job? = null

    init {
        _state.update {
            it.copy(
                selectedChild = AppSettings.selectedChild,
                showPolicyTutorialCard = AppSettings.showPolicyTutorial,
                myUserId = AppSettings.userId,
            )
        }

        observeSelectedChild()
        loadPermissionStatus()
    }

    fun onEvent(event: PolicyEvent) {
        when (event) {
            PolicyEvent.RefreshPolicies -> {
                getSubscriptionLimit()
                loadPermissionStatus()
                getPolicies()
            }

            PolicyEvent.GetChildren -> loadChildren()

            is PolicyEvent.OnChildSelected -> {
                _state.update { it.copy(selectedChild = event.child) }
                AppSettings.selectedChildId = event.child.userId
                AppSettings.selectedChild = event.child
                observeSelectedChild()
                getSubscriptionLimit()
                getPolicies()
            }

            is PolicyEvent.OnTypeSelected ->
                _state.update { it.copy(selectedTypeIndex = event.index) }
        }
    }

    /** Keshni kuzatish — bola almashganda eski kuzatuv bekor qilinadi. */
    private fun observeSelectedChild() {
        observeJob?.cancel()
        val childId = _state.value.selectedChild?.userId
        if (childId.isNullOrBlank()) {
            _state.update { it.copy(policies = emptyList()) }
            return
        }

        observeJob = screenModelScope.launch {
            observePolicies(childId).collect { list ->
                val now = Clock.System.now()
                _state.update { current ->
                    current.copy(
                        policies = list
                            .map { it.toItemUi(current.myUserId, now) }
                            .sortedByDescending { it.policyType.order },
                        now = now,
                    )
                }
            }
        }
    }

    private fun getPolicies() {
        policyJob?.cancel()
        policyJob = screenModelScope.launch {
            val childId = _state.value.selectedChild?.userId
            if (childId.isNullOrBlank()) return@launch

            if (!_state.value.isInitialLoadDone) {
                _state.update { it.copy(policyResponseState = ResponseState.Loading) }
            }

            when (val res = refreshPolicies(childId)) {
                is Outcome.Failure -> _state.update {
                    it.copy(
                        policyResponseState = ResponseState.Error(failure = res),
                        isInitialLoadDone = true,
                    )
                }

                is Outcome.Success -> _state.update {
                    it.copy(
                        policyResponseState = ResponseState.Success(),
                        isInitialLoadDone = true,
                    )
                }
            }
        }
    }

    private fun loadChildren() {
        childrenJob?.cancel()
        childrenJob = screenModelScope.launch {
            _state.update { it.copy(childrenResponseState = ResponseState.Loading) }

            when (val res = childRepository.children()) {
                is Outcome.Failure -> _state.update {
                    it.copy(
                        childrenResponseState = ResponseState.Error(failure = res),
                        childrenList = it.childrenList.ifEmpty { AppSettings.children },
                    )
                }

                is Outcome.Success -> {
                    val children = res.data
                    AppSettings.syncSelectedChildWith(children)

                    if (children.isEmpty()) {
                        AppSettings.selectedChild = null
                        AppSettings.selectedChildId = ""
                    }

                    _state.update {
                        it.copy(
                            childrenResponseState = ResponseState.Success(),
                            childrenList = AppSettings.children,
                            selectedChild = AppSettings.selectedChild,
                        )
                    }
                }
            }
        }
    }

    private fun getSubscriptionLimit() {
        screenModelScope.launch { refreshSubscriptionLimit() }
    }

    private fun refreshSubscriptionLimit() {
        _state.update { current ->
            val childId = current.selectedChild?.userId
            val limit = AppSettings.subscriptionLimitList.find { it.childId == childId }
                ?: SubscriptionLimit()
            current.copy(subscriptionLimit = limit)
        }
    }

    /**
     * Ruxsat holati endi `isActive` ni O'ZGARTIRMAYDI — u serverdagi jadval holati.
     * Bu yerda faqat banner uchun ma'lumot yig'iladi.
     */
    private fun loadPermissionStatus() {
        permissionJob?.cancel()
        permissionJob = screenModelScope.launch {
            when (val res = permissionStatusRepository.permissionStatus(
                childId = _state.value.selectedChild?.userId ?: "",
                state = PermissionStatusType.POLICY,
            )) {
                is Outcome.Success -> _state.update { it.copy(permissionIssueList = res.data) }
                is Outcome.Failure -> _state.update { it.copy(permissionIssueList = emptyList()) }
            }
        }
    }
}