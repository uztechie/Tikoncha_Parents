package uz.tikoncha_parent.presentation.profile.subscription.info

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.xatolik
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.model.subscription.PlanType
import uz.tikoncha_parent.domain.use_case.payment.GetSubscriptionStatusUseCase
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.presentation.profile.subscription.subscription_info.SubscriptionEffect
import uz.tikoncha_parent.presentation.profile.subscription.subscription_info.SubscriptionEvent
import uz.tikoncha_parent.presentation.profile.subscription.subscription_info.SubscriptionState
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class SubscriptionViewModel(
    private val getSubscriptionStatusUseCase: GetSubscriptionStatusUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(SubscriptionState())
    val state: StateFlow<SubscriptionState> = _state.asStateFlow()

    private val _effect = Channel<SubscriptionEffect>(Channel.BUFFERED)
    val effect: Flow<SubscriptionEffect> = _effect.receiveAsFlow()

    init {
        _state.update {
            it.copy(
                selectedChildId = AppSettings.selectedChild?.userId
            )
        }
        onEvent(SubscriptionEvent.Load)
    }

    fun onEvent(event: SubscriptionEvent) {
        when (event) {
            SubscriptionEvent.Load ->{
                if (_state.value.selectedChildId.isNullOrBlank()){
                    _effect.trySend(SubscriptionEffect.NavigateToPayment)
                }
                else{
                    loadStatus()
                }
            }

            SubscriptionEvent.ResetResponseState -> {
                _state.update { it.copy(subscriptionStatusState = ResponseState.Idle) }
            }

            SubscriptionEvent.OnCardClick -> {
                screenModelScope.launch {
                    _effect.trySend(SubscriptionEffect.NavigateToInfoMode)
                }
            }

            SubscriptionEvent.OnErrorDismissed -> {
                _state.update { it.copy(subscriptionStatusState = ResponseState.Idle) }
                screenModelScope.launch {
                    _effect.trySend(SubscriptionEffect.PopBack)
                }
            }

            SubscriptionEvent.OnBackClick -> {
                screenModelScope.launch { _effect.trySend(SubscriptionEffect.PopBack) }
            }
        }
    }

    private fun loadStatus() {
        screenModelScope.launch {
            _state.update { it.copy(subscriptionStatusState = ResponseState.Loading) }

            val result = getSubscriptionStatusUseCase.invoke(_state.value.selectedChildId)
            when(result){
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            subscriptionStatusState = ResponseState.Error(message = result.message, res = result.resId)
                        )
                    }
                }
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            subscription = result.data,
                            subscriptionStatusState = ResponseState.Success(Unit)
                        )
                    }

                    if (result.data.planType == PlanType.FREE || result.data.isExpired) {
                        _effect.trySend(SubscriptionEffect.NavigateToPayment)
                    }
                }
            }
        }
    }
}