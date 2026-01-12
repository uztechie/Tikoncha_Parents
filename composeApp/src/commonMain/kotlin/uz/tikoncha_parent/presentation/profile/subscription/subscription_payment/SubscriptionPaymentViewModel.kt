package uz.tikoncha_parent.presentation.profile.subscription.subscription_payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.model.SubscriptionLimit
import uz.tikoncha_parent.domain.use_case.payment.SubscriptionLimitUseCase
import uz.tikoncha_parent.domain.use_case.payment.SubscriptionPlanUseCase
import uz.tikoncha_parent.presentation.ui_state.ResponseState


class SubscriptionPaymentViewModel(
    private val subscriptionPlanUseCase: SubscriptionPlanUseCase,
    private val subscriptionLimitUseCase: SubscriptionLimitUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(
        SubscriptionPaymentState()
    )
    val state = _state.asStateFlow()

    var limitJob: Job? = null

    init {
        requestSubscriptionPlans()
        _state.update {
            it.copy(
                selectedChild = AppSettings.selectedChild,
                children = AppSettings.children
            )
        }
        getCurrentLimit()
    }


    fun onEvent(event: SubscriptionPaymentEvent){
        when(event){
            SubscriptionPaymentEvent.ResetResponseState -> {
                _state.update {
                    it.copy(
                        subscriptionPlanState = ResponseState.Idle
                    )
                }
            }

            is SubscriptionPaymentEvent.SetSelectedChild -> {
                _state.update {
                    it.copy(
                        selectedChild = event.child
                    )
                }
                AppSettings.selectedChild = event.child
                getCurrentLimit()
            }
        }
    }


    private fun requestSubscriptionPlans() {
        screenModelScope.launch {
            _state.update {
                it.copy(
                    subscriptionPlanState = ResponseState.Loading
                )
            }

            val result = subscriptionPlanUseCase.invoke()
            when(result) {
                is Resource.Loading<*> -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            subscriptionPlanState = ResponseState.Error(
                                message = result.message
                            )
                        )
                    }
                }
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            subscriptionPlanState = ResponseState.Success(),
                            subscriptionUi = result.data.firstOrNull()
                        )
                    }
                }
            }
        }
    }

    private fun getCurrentLimit(){
        limitJob?.cancel()
        limitJob = screenModelScope.launch {
            subscriptionLimitUseCase.invoke()
            _state.update { innerState->
                val limit = AppSettings.subscriptionLimitList.find { it.childId == state.value.selectedChild?.userId }?: SubscriptionLimit()
                innerState.copy(
                    currentPlan = limit.subscriptionType
                )
            }
        }
    }
}