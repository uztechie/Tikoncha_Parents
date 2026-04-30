@file:OptIn(ExperimentalTime::class)

package uz.tikoncha_parent.presentation.statistic

import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import uz.tikoncha_parent.common.DateTimeUtil
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.mapper.mapToDailyUsagePeriods
import uz.tikoncha_parent.data.mapper.mapToWeeklyUsagePeriods
import uz.tikoncha_parent.data.mapper.toDailyAverage
import uz.tikoncha_parent.data.mapper.toDailyUsageMinutesForChart
import uz.tikoncha_parent.data.mapper.toTodayAverage
import uz.tikoncha_parent.data.mapper.toUsageUi
import uz.tikoncha_parent.data.mapper.toUserInfo
import uz.tikoncha_parent.data.mapper.toWeeklyAverage
import uz.tikoncha_parent.data.mapper.toWeeklyUsageMinutesForChart
import uz.tikoncha_parent.data.remote.model.permission_status.PermissionStatusRequest
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.model.SubscriptionLimit
import uz.tikoncha_parent.domain.model.permission_status.PermissionStatusType
import uz.tikoncha_parent.domain.use_case.AppUsagesUseCase
import uz.tikoncha_parent.domain.use_case.ChildrenUseCase
import uz.tikoncha_parent.domain.use_case.payment.SubscriptionLimitUseCase
import uz.tikoncha_parent.domain.use_case.permission_status.PermissionStatusUseCase
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.presentation.domain.model.UsagePeriod
import uz.tikoncha_parent.presentation.new_home.HomeEvent
import uz.tikoncha_parent.presentation.profile.coins.CoinsEvent
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


class StatisticViewModel(
    private val appUsagesUseCase: AppUsagesUseCase,
    private val subscriptionLimitUseCase: SubscriptionLimitUseCase,
    private val childrenUseCase: ChildrenUseCase,
    private val permissionStatusUseCase: PermissionStatusUseCase
) : ScreenModel
{

    private val TAG = "StatisticViewModel"

    private val _state = MutableStateFlow(StatisticState())
    val state = _state.asStateFlow()

    private val _usagePeriod = MutableStateFlow<UsagePeriod?>(null)


    private var childrenJob: Job? = null
    private var appUsageJob: Job? = null
    private var computeAllJob: Job? = null

    init {
        Logger.d(TAG, "INIT")
    }




    fun onEvent(event: StatisticEvent) {
        when (event) {

            is StatisticEvent.OnChildSelected -> {

                Logger.d(TAG, "Statistics-OnChildSelected AppSettings.selectedChild=${AppSettings.selectedChild}")
                Logger.d(TAG, "Statistics-OnChildSelected event.child=${event.child}")

                _state.update {
                    it.copy(selectedChild = event.child)
                }
                AppSettings.selectedChildId = event.child.userId
                AppSettings.selectedChild = event.child
                loadAppUsages()

                Logger.d(TAG, "Statistics-OnChildSelected after AppSettings.selectedChild=${AppSettings.selectedChild}")
                Logger.d(TAG, "Statistics-OnChildSelected after event.child=${event.child}")
            }

            is StatisticEvent.GetUsageList -> {
                _usagePeriod.value = event.usagePeriod
                val isToday = event.dateSelectionType == DateSelectionType.DAY &&
                        event.usagePeriod.startDate.isToday()
                _state.update {
                    it.copy(
                        dateSelectionType = event.dateSelectionType,
                        selectedPeriod = event.usagePeriod,
                        isTodaySelected = isToday
                    )
                }
                recomputeAll()
            }

            StatisticEvent.GetAppUsage -> {
                loadAppUsages()
            }



            StatisticEvent.RefreshSubscriptionLimit -> {
                getSubscriptionLimit()
            }

            StatisticEvent.RefreshChild -> {
                _state.update { innerState->
                    val limit = AppSettings.subscriptionLimitList.find { it.childId == AppSettings.selectedChild?.userId }?: SubscriptionLimit()
                    innerState.copy(
                        selectedChild = AppSettings.selectedChild,
                        subscriptionLimit = limit
                    )
                }
            }

            StatisticEvent.GetChildren -> {
                loadChildren()
            }

            StatisticEvent.ClearAll -> {

            }
        }
    }


    private fun LocalDate.isToday(): Boolean {
        val today = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
        return this == today
    }

    private fun getSubscriptionLimit(){
        screenModelScope.launch {
            subscriptionLimitUseCase.invoke()
        }
    }


    private fun loadAppUsages() {
        val childId = state.value.selectedChild?.userId
        if (childId.isNullOrEmpty()) {
            return
        }
        appUsageJob?.cancel()
        appUsageJob = screenModelScope.launch {
            _state.update {
                it.copy(
                    appUsageResponseState = ResponseState.Loading,
                )
            }


            val response = appUsagesUseCase.invoke(state.value.selectedChild?.userId ?: "")
            when (response) {
                is Resource.Loading -> {}
                is Resource.Error -> {
                    println("loadAppUsages ERROR: msg=${response.message}")
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
                            todayUsage = usageList.toTodayAverage()

                            )
                    }
                    recomputeAll()
                }
            }
        }
    }




    private fun recomputeAll(){
        val period = _usagePeriod.value ?: return
        computeAllJob?.cancel()
        computeAllJob = screenModelScope.launch(Dispatchers.IO) {
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
                    weeklyChartData = weeklyChartData,
                    averageUsageTime = avg
                )
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
                    if (AppSettings.selectedChild == null) {
                        AppSettings.selectedChild = AppSettings.children.firstOrNull()
                        _state.update { it.copy(selectedChild = AppSettings.selectedChild) }
                    }
                    loadAppUsages()
                    loadPermissionStatus()

//                    AppSettings.children = response.data.map { userInfoDto -> userInfoDto.toUserInfo() }
//                    if (AppSettings.selectedChild == null){
//                        AppSettings.selectedChild = AppSettings.children.firstOrNull()
//                    }
                }
            }
        }
    }

    private fun loadPermissionStatus() {
        screenModelScope.launch {
            val res = permissionStatusUseCase.invoke(
                PermissionStatusRequest(
                    userId = _state.value.selectedChild?.userId?:"",
                    state = PermissionStatusType.STATISTICS.name
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