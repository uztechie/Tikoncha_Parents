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
import uz.tikoncha_parent.data.mapper.toUserInfo
import uz.tikoncha_parent.data.remote.model.DeviceRegisterRequest
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.ChildrenUseCase
import uz.tikoncha_parent.domain.use_case.ParentRequestsUseCase
import uz.tikoncha_parent.domain.use_case.RegisterDeviceUseCase
import uz.tikoncha_parent.domain.use_case.payment.SubscriptionLimitUseCase
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.platform.getDeviceInfo
import uz.tikoncha_parent.presentation.domain.model.UsagePeriod
import uz.tikoncha_parent.presentation.new_home.logout.ParentRequestEvent
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import kotlin.time.ExperimentalTime


class HomeViewModel(
    private val childrenUseCase: ChildrenUseCase,
    private val registerDeviceUseCase: RegisterDeviceUseCase,
    private val subscriptionLimitUseCase: SubscriptionLimitUseCase,
    private val parentRequestUseCase: ParentRequestsUseCase,
) : ScreenModel {

    private val hasLoaded = MutableStateFlow(false)

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _usagePeriod = MutableStateFlow<UsagePeriod?>(null)


    private var childrenJob: Job? = null
    private var appUsageJob: Job? = null
    private var computeAllJob: Job? = null

    init {
        loadOnce()
        _state.update {
            it.copy(
                selectedChild = AppSettings.selectedChild,
            )
        }
    }

    fun loadOnce(){
        val setOk = hasLoaded.compareAndSet(expect = false, update = true)
        if (!setOk) return
        sendDeviceInfo()
        getSubscriptionLimit()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {

            is HomeEvent.OnChildSelected -> {
                _state.update {
                    it.copy(selectedChild = event.child)
                }
                AppSettings.selectedChildId = event.child.userId
                AppSettings.selectedChild = event.child
            }


            HomeEvent.GetChildren -> {
                loadChildren()
            }

            HomeEvent.RefreshParentRequest -> {
                loadParentRequestsCount()
            }
        }
    }

    private fun sendDeviceInfo(){
        screenModelScope.launch {
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
    }

    private fun getSubscriptionLimit(){
        screenModelScope.launch {
            subscriptionLimitUseCase.invoke()
        }
    }




    private fun loadChildren() {
        Logger.d("loadChildren", " loadChildren current=${AppSettings.selectedChild}")
        childrenJob?.cancel()
        childrenJob = screenModelScope.launch {
            _state.update {
                it.copy(
                    childrenResponseState = ResponseState.Loading
                )
            }

            val response = childrenUseCase.invoke()
            when (response) {
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            childrenResponseState = ResponseState.Error(
                                res = response.resId,
                                message = response.message
                            )
                        )
                    }
                }

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            childrenResponseState = ResponseState.Success(),
                            childrenList = response.data.map { userInfoDto -> userInfoDto.toUserInfo() },
                            selectedChild = AppSettings.selectedChild
                        )
                    }
                    AppSettings.children = response.data.map { userInfoDto -> userInfoDto.toUserInfo() }
                    if (AppSettings.selectedChild == null){
                        AppSettings.selectedChild = AppSettings.children.firstOrNull()
                    }

                }
            }
        }
    }

    fun loadParentRequestsCount(){
        screenModelScope.launch {
            val result = parentRequestUseCase()
            when(result){
                is Resource.Loading -> {}
                is Resource.Error -> {}
                is Resource.Success -> {
                    val list = result.data
                    val count = list?.size

                    _state.update {
                        it.copy(
                            parentRequestCount = count?:0
                        )
                    }
                }
            }
        }
    }

}