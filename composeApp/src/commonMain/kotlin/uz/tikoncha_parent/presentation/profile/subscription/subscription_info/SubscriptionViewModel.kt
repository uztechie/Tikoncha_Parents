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
        onEvent(SubscriptionEvent.Load)
    }

    fun onEvent(event: SubscriptionEvent) {
        when (event) {
            SubscriptionEvent.Load,

            SubscriptionEvent.Retry -> {
                loadStatus()
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
            try {
                val childId = AppSettings.selectedChildId
                val status = getSubscriptionStatusUseCase(childId)
                Logger.d("SubscriptionVM", "status=$status")

                _state.update {
                    it.copy(
                        subscription = status,
                        subscriptionStatusState = ResponseState.Success(Unit)
                    )
                }

                Logger.d("SubscriptionVM", "status=$status")
                // FREE yoki expired -> to'lov ekraniga effect orqali
                if (status.planType == PlanType.FREE || status.isExpired) {
                    _effect.trySend(SubscriptionEffect.NavigateToPayment)
                }
            } catch (e: Exception) {
                Logger.d("SubscriptionVM", "error=${e.message}")
                _state.update {
                    it.copy(
                        subscriptionStatusState = ResponseState.Error(res = Res.string.xatolik)
                    )
                }
            }
        }
    }
}