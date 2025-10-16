@file:OptIn(ExperimentalTime::class)

package uz.tikoncha_parent.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.common.AppCode
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.mapper.mapToDailyUsagePeriods
import uz.tikoncha_parent.data.mapper.mapToWeeklyUsagePeriods
import uz.tikoncha_parent.data.mapper.toDailyAverage
import uz.tikoncha_parent.data.mapper.toDailyUsageMinutesForChart
import uz.tikoncha_parent.data.mapper.toUsageUi
import uz.tikoncha_parent.data.mapper.toUserInfo
import uz.tikoncha_parent.data.mapper.toWeeklyAverage
import uz.tikoncha_parent.data.mapper.toWeeklyUsageMinutesForChart
import uz.tikoncha_parent.data.remote.model.DeviceRegisterRequest
import uz.tikoncha_parent.data.remote.model.GetRuleItem
import uz.tikoncha_parent.data.remote.model.GetRulesData
import uz.tikoncha_parent.data.remote.model.UpsertRuleRequest
import uz.tikoncha_parent.domain.model.AppUsage
import uz.tikoncha_parent.domain.model.PolicyActionType
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.AppUsagesUseCase
import uz.tikoncha_parent.domain.use_case.ChildrenUseCase
import uz.tikoncha_parent.domain.use_case.RefreshRulesUseCase
import uz.tikoncha_parent.domain.use_case.RegisterDeviceUseCase
import uz.tikoncha_parent.domain.use_case.UpsertRuleUseCase
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.platform.getDeviceInfo
import uz.tikoncha_parent.presentation.domain.model.UsagePeriod
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import kotlin.time.ExperimentalTime


class HomeViewModel(
    private val appUsagesUseCase: AppUsagesUseCase,
    private val childrenUseCase: ChildrenUseCase,
    private val refreshRulesUseCase: RefreshRulesUseCase,
    private val upsertRuleUseCase: UpsertRuleUseCase,
    private val registerDeviceUseCase: RegisterDeviceUseCase
) : ViewModel()
{

    private val hasLoaded = MutableStateFlow(false)

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _usagePeriod = MutableStateFlow<UsagePeriod?>(null)


    private var childrenJob: Job? = null
    private var appUsageJob: Job? = null
    private var rulesJob: Job? = null
    private var computeAllJob: Job? = null
    private var computeAppListJob: Job? = null
    private var upsertRuleJob: Job? = null

    init {
        loadOnce()
    }

    fun loadOnce(){
        val setOk = hasLoaded.compareAndSet(expect = false, update = true)
        if (!setOk) return
        sendDeviceInfo()
    }

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

    private fun sendDeviceInfo(){
        viewModelScope.launch {
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




    private fun loadChildren() {
        childrenJob?.cancel()
        childrenJob = viewModelScope.launch {
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
                    appUsageResponseState = ResponseState.Loading,
                )
            }


            val response = appUsagesUseCase.invoke(state.value.selectedChildren?.userId ?: "")
            when (response) {
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            appUsageResponseState = ResponseState.Error(
                                res = response.resId,
                                message = response.message
                            )
                        )
                    }
                }

                is Resource.Success -> {
                    val usageList = response.data
                    Logger.d("TAG", "week=${usageList.mapToWeeklyUsagePeriods()}")
                    _state.update {
                        it.copy(
                            appUsageResponseState = ResponseState.Success(),
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
                            rulesError = response.message?:""
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
                    createRuleResponseState = ResponseState.Loading,
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
                            createRuleResponseState = ResponseState.Error(
                                res = response.resId,
                                message = response.message
                            )
                        )
                    }
                }

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            createRuleResponseState = ResponseState.Success(),
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

            println("AVERAGE week=$weeklyAverage  day=$dailyAverage  avg=$avg")


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