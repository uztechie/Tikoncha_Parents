package uz.tikoncha_parent.presentation.profile.subscription.subscription_info

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
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.model.subscription.PlanType
import uz.tikoncha_parent.domain.repository.PaymentRepository
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class SubscriptionViewModel(
    private val paymentRepository: PaymentRepository
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

            when (val res = paymentRepository.getSubscriptionStatus(_state.value.selectedChildId)) {
                is Outcome.Failure -> {
                    _state.update {
                        it.copy(subscriptionStatusState = ResponseState.Error(failure = res))
                    }
                }

                is Outcome.Success -> {
                    _state.update {
                        it.copy(
                            subscription = res.data,
                            subscriptionStatusState = ResponseState.Success(Unit)
                        )
                    }
                    if (res.data.planType == PlanType.FREE || res.data.isExpired) {
                        _effect.trySend(SubscriptionEffect.NavigateToPayment)
                    }
                }
            }
        }
    }
}