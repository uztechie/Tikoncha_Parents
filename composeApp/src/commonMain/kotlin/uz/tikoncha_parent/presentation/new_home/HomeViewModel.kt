@file:OptIn(ExperimentalTime::class)

package uz.tikoncha_parent.presentation.new_home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.common.AppCode
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.mapper.toPolicyListUi
import uz.tikoncha_parent.data.remote.model.DeviceRegisterRequest
import uz.tikoncha_parent.domain.model.PolicyType
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.model.protection.missingRequiredPermissionCount
import uz.tikoncha_parent.domain.model.protection.pendingRequestCount
import uz.tikoncha_parent.domain.repository.ChildRepository
import uz.tikoncha_parent.domain.repository.PaymentRepository
import uz.tikoncha_parent.domain.repository.ProtectionRepository
import uz.tikoncha_parent.domain.use_case.GetPoliciesFromServerUseCase
import uz.tikoncha_parent.domain.use_case.app_usage.TodayUsageUseCase
import uz.tikoncha_parent.domain.use_case.device.RegisterDeviceUseCase
import uz.tikoncha_parent.domain.use_case.todo.TodoListUseCase
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.platform.getDeviceInfo
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import kotlin.time.ExperimentalTime

class HomeViewModel(
    private val childRepository: ChildRepository,
    private val registerDeviceUseCase: RegisterDeviceUseCase,
    private val paymentRepository: PaymentRepository,
    private val todoListUseCase: TodoListUseCase,
    private val getPoliciesFromServerUseCase: GetPoliciesFromServerUseCase,
    private val todayUsageUseCase: TodayUsageUseCase,
    private val protectionRepository: ProtectionRepository,
) : ScreenModel {

    private val TAG = "HomeViewModel"
    private val hasLoaded = MutableStateFlow(false)

    private val hasAppUsageLoaded = MutableStateFlow<String?>("")
    private val hasPolicyLoaded = MutableStateFlow<String?>("")
    private val hasTaskLoaded = MutableStateFlow<String?>("")

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private var childrenJob: Job? = null
    private var todayUsageJob: Job? = null

    private var protectionJob: Job? = null

    init {
        loadOnce()
    }

    fun loadOnce() {
        val setOk = hasLoaded.compareAndSet(expect = false, update = true)
        if (!setOk) return
        sendDeviceInfo()
        getSubscriptionLimit()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnChildSelected -> {
                _state.update { it.copy(selectedChild = event.child) }
                AppSettings.selectedChildId = event.child.userId
                AppSettings.selectedChild = event.child

                loadAll()

            }

            HomeEvent.GetChildren -> loadChildren()
            HomeEvent.ReloadUserInfo -> reloadUserInfo()
            HomeEvent.RefreshParentRequest -> {
                loadProtectionStatus()
            }

            HomeEvent.SyncSelectedChildFromSettings -> {
                Logger.d(TAG, "SyncSelectedChildFromSettings = ${AppSettings.selectedChild}")
                _state.update { it.copy(selectedChild = AppSettings.selectedChild) }

                loadAll()
            }
        }
    }

    private fun loadAll(){
        val selectedChildId = _state.value.selectedChild?.userId
        if (hasTaskLoaded.value != selectedChildId){
            loadTasks()
        }
        if (hasPolicyLoaded.value != selectedChildId){
            loadPolicies()
        }
        if (hasAppUsageLoaded.value != selectedChildId){
            loadTodayUsage()
        }
        loadProtectionStatus()
    }

    private fun loadProtectionStatus() {
        val childId = _state.value.selectedChild?.userId
        if (childId.isNullOrEmpty()) {
            _state.update {
                it.copy(
                    protectionPendingRequestCount = 0,
                    protectionPermissionOffCount = 0,
                )
            }
            return
        }
        protectionJob?.cancel()
        protectionJob = screenModelScope.launch {
            when (val res = protectionRepository.protectionStatus(childId)) {
                is Outcome.Success -> _state.update {
                    it.copy(
                        protectionPendingRequestCount = res.data.pendingRequestCount(),
                        protectionPermissionOffCount = res.data.missingRequiredPermissionCount(),
                    )
                }
                is Outcome.Failure -> Unit
            }
        }
    }

    private fun reloadUserInfo() {
        _state.update {
            it.copy(
                showTikonchaTutorialCard = AppSettings.showTikonchaTutorial,
                userName = AppSettings.userInfo?.name ?: "",
                userImageUrl = AppSettings.userInfo?.avatarUrl ?: AppSettings.profileImageUrl
            )
        }
    }

    private fun sendDeviceInfo() = screenModelScope.launch {
        val token = AppSettings.fcmToken
        val info = getDeviceInfo()
        val request = DeviceRegisterRequest(
            fcm_token = token,
            manufacturer = info.manufacturer,
            model_name = info.modelName,
            os_version = info.osVersion,
            os = info.os,
            app_code = AppCode.currentAppCode
        )
        registerDeviceUseCase.invoke(request)
    }

    private fun getSubscriptionLimit() = screenModelScope.launch {
        paymentRepository.syncSubscriptionLimits()
    }

    private fun loadChildren() {
        childrenJob?.cancel()
        childrenJob = screenModelScope.launch {
            _state.update { it.copy(childrenResponseState = ResponseState.Loading) }

            when (val res = childRepository.children()) {
                is Outcome.Failure -> _state.update {
                    it.copy(childrenResponseState = ResponseState.Error(failure = res))
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

                    loadAll()
                }
            }
        }
    }


    private fun loadTasks() = screenModelScope.launch {
        val selectedId = state.value.selectedChild?.userId ?: return@launch
        when (val result = todoListUseCase.invoke(selectedId)) {
            is Resource.Success -> {
                _state.update {
                    it.copy(activeTaskCount = result.data.count { t -> !t.is_completed })
                }
                hasTaskLoaded.value = _state.value.selectedChild?.userId
            }

            else -> Unit
        }
    }

    private fun loadPolicies() = screenModelScope.launch {
        val selectedChildId = _state.value.selectedChild?.userId ?: return@launch
        when (val result = getPoliciesFromServerUseCase.invoke(selectedChildId)) {
            is Resource.Success ->{
                _state.update { innerState ->
                    val policies = result.data
                        .map { it.toPolicyListUi() }
                        .sortedByDescending { it.policyType.order }
                    val parentPolicyCount = policies
                        .filter { it.policyType == PolicyType.PARENT_CHILD }
                        .flatMap { it.packages }.toSet().size
                    innerState.copy(parentPolicyCount = parentPolicyCount)
                }

                hasPolicyLoaded.value = _state.value.selectedChild?.userId
            }

            else -> Unit
        }
    }

    private fun loadTodayUsage() {
        val childId = _state.value.selectedChild?.userId
        if (childId.isNullOrEmpty()) return
        todayUsageJob?.cancel()
        todayUsageJob = screenModelScope.launch {
            when (val res = todayUsageUseCase.invoke(childId)) {
                is Outcome.Success -> {
                    _state.update { it.copy(todayUsage = res.data) }
                    hasAppUsageLoaded.value = _state.value.selectedChild?.userId
                }
                is Outcome.Failure -> Unit
            }
        }
    }
}