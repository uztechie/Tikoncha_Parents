@file:OptIn(ExperimentalTime::class)

package org.example.project.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.data.local.AppSettings
import org.example.project.data.mapper.mapToDailyUsagePeriods
import org.example.project.data.mapper.mapToWeeklyUsagePeriods
import org.example.project.data.mapper.toDailyAverage
import org.example.project.data.mapper.toDailyUsageMinutesForChart
import org.example.project.data.mapper.toUsageUi
import org.example.project.data.mapper.toUserInfo
import org.example.project.data.mapper.toWeeklyAverage
import org.example.project.data.mapper.toWeeklyUsageMinutesForChart
import org.example.project.data.remote.model.CreateRuleRequest
import org.example.project.data.remote.model.GetRulesData
import org.example.project.domain.model.PolicyActionType
import org.example.project.domain.model.PolicyMatcherType
import org.example.project.domain.model.PolicyResourceType
import org.example.project.domain.model.Resource
import org.example.project.domain.use_case.AppUsagesUseCase
import org.example.project.domain.use_case.ChildrenUseCase
import org.example.project.domain.use_case.CreateRuleUseCase
import org.example.project.domain.use_case.RefreshRulesUseCase
import org.example.project.platform.Logger
import org.example.project.presentation.domain.model.UsagePeriod
import kotlin.time.ExperimentalTime


class HomeViewModel(
    private val appUsagesUseCase: AppUsagesUseCase,
    private val childrenUseCase: ChildrenUseCase,
    private val refreshRulesUseCase: RefreshRulesUseCase,
    private val createRuleUseCase: CreateRuleUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _usagePeriod = MutableStateFlow<UsagePeriod?>(null)


    private var childrenJob: Job? = null
    private var appUsageJob: Job? = null
    private var rulesJob: Job? = null
    private var createRuleJob: Job? = null

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.GetUsageList -> {
                _usagePeriod.value = event.usagePeriod
                _state.update {
                    it.copy(
                        dateSelectionType = event.dateSelectionType,
                    )
                }
                getChartData()
                getAppUsageList()

            }

            HomeEvent.OnChildSelectClicked -> {

            }

            is HomeEvent.OnChildSelected -> {
                _state.update {
                    it.copy(selectedChildren = event.child)
                }
                loadAppUsages()

            }

            HomeEvent.GetAppUsage -> {
                loadAppUsages()
            }

            HomeEvent.GetChildren -> {
                loadChildren()
            }

            is HomeEvent.TodaySelected -> {
                _state.update {
                    it.copy(
                        isTodaySelected = event.today
                    )
                }
            }

            is HomeEvent.OnLockClicked -> {
                _state.update {
                    it.copy(
                        selectedApp = event.appUsageUi
                    )
                }
                createRule()
            }
        }
    }


    private fun getAppUsageList() {
        _state.update {
            it.copy(
                appUsageUiList = state.value.appUsageList.toUsageUi(
                    startDate = _usagePeriod.value?.startDate,
                    endDate = _usagePeriod.value?.endDate
                )
            )
        }
    }

    private fun getChartData() {
        val weeklyChartData = state.value.appUsageList.toWeeklyUsageMinutesForChart(
            startDate = _usagePeriod.value?.startDate
        )

        val dailyChartData = state.value.appUsageList.toDailyUsageMinutesForChart(
            startDate = _usagePeriod.value?.startDate
        )
        val weeklyAverage = state.value.appUsageList.toWeeklyAverage(_usagePeriod.value?.startDate)
        val dailyAverage = state.value.appUsageList.toDailyAverage(_usagePeriod.value?.startDate)


        println("AVER=$weeklyAverage  daily=$dailyAverage")

        if (state.value.dateSelectionType == DateSelectionType.WEEK) {
            _state.update {
                it.copy(
                    dailyChartData = weeklyChartData,
                    averageUsageTime = weeklyAverage
                )
            }
        } else {
            _state.update {
                it.copy(
                    dailyChartData = dailyChartData,
                    averageUsageTime = dailyAverage
                )
            }
        }

    }


    private fun loadChildren() {
        childrenJob?.cancel()
        childrenJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    childrenLoading = true,
                    childrenError = ""
                )
            }

            val response = childrenUseCase.invoke()
            when (response) {
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            childrenLoading = false,
                            childrenError = response.message
                        )
                    }
                }

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            childrenLoading = false,
                            childrenError = "",
                            childrenList = response.data.map { userInfoDto -> userInfoDto.toUserInfo() }
                        )
                    }
                }
            }
        }
    }

    private fun loadAppUsages() {
        appUsageJob?.cancel()
        appUsageJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    appUsageLoading = true,
                    appUsageError = ""
                )
            }

            val response = appUsagesUseCase.invoke(state.value.selectedChildren?.userId ?: "")
            when (response) {
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            appUsageLoading = false,
                            appUsageError = response.message
                        )
                    }
                }

                is Resource.Success -> {
                    val usageList = response.data
                    Logger.d("TAG", "week=${usageList.mapToWeeklyUsagePeriods()}")
                    _state.update {
                        it.copy(
                            appUsageLoading = false,
                            appUsageError = "",
                            appUsageList = usageList,
                            dailyPeriods = usageList.mapToDailyUsagePeriods(),
                            weeklyPeriods = usageList.mapToWeeklyUsagePeriods(),

                            )
                    }
                    getAppUsageList()
                    getChartData()
                    refreshRules()
                }
            }
        }
    }


    private fun refreshRules() {
        rulesJob?.cancel()
        rulesJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    rulesError = ""
                )
            }

            val response: Resource<GetRulesData> =
                refreshRulesUseCase.invoke(state.value.selectedChildren?.userId ?: "")
            when (response) {
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            rulesError = response.message
                        )
                    }
                }

                is Resource.Success -> {
                    val rulesData = response.data
                    val usageApps: List<AppUsageUi> = state.value.appUsageUiList

                    val allowSet: Set<String> = rulesData.apps.allow.map { it.value }.toSet()
                    val denySet: Set<String> = rulesData.apps.deny.map { it.value }.toSet()

                    val patched: List<AppUsageUi> = usageApps.map { item ->
                        val allowed = when {
                            item.packageName in allowSet -> true
                            item.packageName in denySet -> false
                            else -> false
                        }
                        item.copy(allowed = allowed)
                    }

                    _state.update {
                        it.copy(
                            rulesError = "",
                            appUsageUiList = patched
                        )
                    }
                }
            }
        }
    }

    private fun createRule() {
        createRuleJob?.cancel()
        createRuleJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    createRuleError = "",
                    createRuleLoading = true,
                    createRuleSuccess = false
                )
            }

            val createRuleRequest = CreateRuleRequest(
                policyId = AppSettings.policyId,
                resource_type = PolicyResourceType.APP.name,
                matcher = PolicyMatcherType.PACKAGE.name,
                value = state.value.selectedApp?.packageName?:"",
                action = if (state.value.selectedApp?.allowed == true) PolicyActionType.DENY.name
                else PolicyActionType.ALLOW.name
            )

            val response = createRuleUseCase.invoke(
                createRuleRequest = createRuleRequest,
                userId = state.value.selectedChildren?.userId?:""
            )
            when (response) {
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            createRuleError = response.message,
                            createRuleLoading = false,
                            createRuleSuccess = false
                        )
                    }
                }

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            createRuleError = "",
                            createRuleLoading = false,
                            createRuleSuccess = true
                        )
                    }

                    val appList = state.value.appUsageUiList
                        .map {
                            if (it.packageName == state.value.selectedApp?.packageName){
                                it.copy(
                                    allowed = !it.allowed
                                )
                            }
                            else{
                                it
                            }
                        }

                    _state.update {
                        it.copy(
                            appUsageUiList = appList
                        )
                    }
                }
            }
        }
    }

}