@file:OptIn(ExperimentalTime::class)

package org.example.project.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
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
import org.example.project.data.remote.model.GetRuleItem
import org.example.project.data.remote.model.GetRulesData
import org.example.project.data.remote.model.UpsertRuleRequest
import org.example.project.domain.model.AppUsage
import org.example.project.domain.model.PolicyActionType
import org.example.project.domain.model.Resource
import org.example.project.domain.use_case.AppUsagesUseCase
import org.example.project.domain.use_case.ChildrenUseCase
import org.example.project.domain.use_case.RefreshRulesUseCase
import org.example.project.domain.use_case.UpsertRuleUseCase
import org.example.project.platform.Logger
import org.example.project.presentation.domain.model.UsagePeriod
import kotlin.time.ExperimentalTime


class HomeViewModel(
    private val appUsagesUseCase: AppUsagesUseCase,
    private val childrenUseCase: ChildrenUseCase,
    private val refreshRulesUseCase: RefreshRulesUseCase,
    private val upsertRuleUseCase: UpsertRuleUseCase
) : ViewModel()
{

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _usagePeriod = MutableStateFlow<UsagePeriod?>(null)


    private var childrenJob: Job? = null
    private var appUsageJob: Job? = null
    private var rulesJob: Job? = null
    private var computeAllJob: Job? = null
    private var computeAppListJob: Job? = null
    private var upsertRuleJob: Job? = null

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.GetUsageList -> {
                _usagePeriod.value = event.usagePeriod
                _state.update {
                    it.copy(
                        dateSelectionType = event.dateSelectionType,
                    )
                }
                recomputeAll()

            }

            HomeEvent.OnChildSelectClicked -> {

            }

            is HomeEvent.OnChildSelected -> {
                _state.update {
                    it.copy(selectedChildren = event.child)
                }
                AppSettings.selectedChildId = event.child.userId
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
                    recomputeAll()
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
                    val usageApps: List<AppUsage> = state.value.appUsageList

                    val decisionsMap = buildDecisionsMap(rulesData.items)

                    val patched: List<AppUsage> = usageApps.map { item ->
                        val allowed = decisionsMap[item.packageName]
                        item.copy(allowed = allowed?:false)
                    }

                    _state.update {
                        it.copy(
                            rulesError = "",
                            appUsageList = patched,
                            rulesAppList = response.data.items
                        )
                    }
                    recomputeAppList()
                }
            }
        }
    }

    private fun createRule() {
        upsertRuleJob?.cancel()
        upsertRuleJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    createRuleError = "",
                    createRuleLoading = true,
                    createRuleSuccess = false
                )
            }

            val upsertRuleRequest = UpsertRuleRequest(
                target_user_id = state.value.selectedChildren?.userId?:"",
                `package` = state.value.selectedApp?.packageName?:"",
                action = if (state.value.selectedApp?.allowed == true) PolicyActionType.DENY.name
                else PolicyActionType.ALLOW.name
            )

            val response = upsertRuleUseCase.invoke(
                upsertRuleRequest
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
                    refreshRules()
//                    val appList = state.value.appUsageUiList
//                        .map {
//                            if (it.packageName == state.value.selectedApp?.packageName){
//                                it.copy(
//                                    allowed = !it.allowed
//                                )
//                            }
//                            else{
//                                it
//                            }
//                        }
//
//                    _state.update {
//                        it.copy(
//                            appUsageUiList = appList
//                        )
//                    }
                }
            }
        }
    }

    private fun recomputeAll(){
        val period = _usagePeriod.value ?: return
        computeAllJob?.cancel()
        computeAllJob = viewModelScope.launch(Dispatchers.Default) {
            val s = state.value
            val usageList = s.appUsageList
            val dateType = s.dateSelectionType

            val uiList = usageList.toUsageUi(
                startDate = period.startDate,
                endDate = period.endDate
            )

            val weeklyChartData = usageList.toWeeklyUsageMinutesForChart(period.startDate)
            val dailyChartData = usageList.toDailyUsageMinutesForChart(period.startDate)
            val weeklyAverage = usageList.toWeeklyAverage(period.startDate)
            val dailyAverage = usageList.toDailyAverage(period.startDate)

            val (chartData, avg) = if (dateType == DateSelectionType.WEEK) {
                weeklyChartData to weeklyAverage
            } else {
                dailyChartData to dailyAverage
            }

            _state.update {
                it.copy(
                    appUsageUiList = uiList,
                    dailyChartData = chartData,
                    averageUsageTime = avg
                )
            }

        }
    }

    private fun recomputeAppList(){
        val period = _usagePeriod.value ?: return
        computeAppListJob?.cancel()
        computeAppListJob = viewModelScope.launch(Dispatchers.Default) {

            val s = state.value
            val usageList = s.appUsageList

            val uiList = usageList.toUsageUi(
                startDate = period.startDate,
                endDate = period.endDate
            )



            _state.update {
                it.copy(
                    appUsageUiList = uiList,
                )
            }

        }
    }


    private fun buildDecisionsMap(items: List<GetRuleItem>): Map<String, Boolean> {
        val map = LinkedHashMap<String, Boolean>()
        items.forEach { item ->
            val pkg = item.`package`.trim()
            if (pkg.isNotEmpty()) {
                when (item.decision.uppercase()) {
                    "ALLOW" -> map[pkg] = true
                    "DENY"  -> map[pkg] = false
                    else    -> { map[pkg] = false }
                }
            }
        }
        return map
    }

}