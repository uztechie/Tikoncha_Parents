@file:OptIn(ExperimentalTime::class)

package uz.tikoncha_parent.presentation.statistic

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
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
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.AppUsagesUseCase
import uz.tikoncha_parent.domain.use_case.ChildrenUseCase
import uz.tikoncha_parent.domain.use_case.RegisterDeviceUseCase
import uz.tikoncha_parent.domain.use_case.payment.SubscriptionLimitUseCase
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.platform.getDeviceInfo
import uz.tikoncha_parent.presentation.domain.model.UsagePeriod
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import kotlin.time.ExperimentalTime


class StatisticViewModel(
    private val appUsagesUseCase: AppUsagesUseCase,
    private val subscriptionLimitUseCase: SubscriptionLimitUseCase,

) : ScreenModel
{


    private val _state = MutableStateFlow(StatisticState())
    val state = _state.asStateFlow()

    private val _usagePeriod = MutableStateFlow<UsagePeriod?>(null)


    private var childrenJob: Job? = null
    private var appUsageJob: Job? = null
    private var computeAllJob: Job? = null

    init {

    }




    fun onEvent(event: StatisticEvent) {
        when (event) {
            is StatisticEvent.GetUsageList -> {
                _usagePeriod.value = event.usagePeriod
                _state.update {
                    it.copy(
                        dateSelectionType = event.dateSelectionType,
                    )
                }
                recomputeAll()

            }


            StatisticEvent.GetAppUsage -> {
                loadAppUsages()
            }


            is StatisticEvent.TodaySelected -> {
                _state.update {
                    it.copy(
                        isTodaySelected = event.today
                    )
                }
            }


            StatisticEvent.RefreshSubscriptionLimit -> {
                getSubscriptionLimit()
            }

            StatisticEvent.RefreshChild -> {
                _state.update {
                    it.copy(
                        selectedChild = AppSettings.selectedChild
                    )
                }
            }
        }
    }



    private fun getSubscriptionLimit(){
        screenModelScope.launch {
            subscriptionLimitUseCase.invoke()
        }
        _state.update {
            it.copy(
                subscriptionLimit = AppSettings.subscriptionLimit
            )
        }
    }


    private fun loadAppUsages() {
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
                    averageUsageTime = avg
                )
            }

        }
    }

}