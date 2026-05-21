package uz.tikoncha_parent.presentation.profile.coin_purchase

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.remote.model.subscription.PromoCodeValidationRequest
import uz.tikoncha_parent.domain.model.PaymentStatus
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.model.subscription.PurchaseCoinRequest
import uz.tikoncha_parent.domain.use_case.payment.PaymentStatusUseCase
import uz.tikoncha_parent.domain.use_case.payment.PromoCodeValidationUseCase
import uz.tikoncha_parent.domain.use_case.payment.PurchaseCoinUseCase

class CoinPurchaseViewModel (
    private val promoCodeValidationUseCase: PromoCodeValidationUseCase,
    private val purchaseCoinUseCase: PurchaseCoinUseCase,
    private val paymentStatusUseCase: PaymentStatusUseCase
): ScreenModel {

    private val _state = MutableStateFlow(CoinPurchaseState())
    val state = _state.asStateFlow()


    private val _effect = MutableSharedFlow<CoinPurchaseEffect>(extraBufferCapacity = 1)
    val effect = _effect.asSharedFlow()

    fun onEvent(event: CoinPurchaseEvent){
        when(event){
            is CoinPurchaseEvent.ApplyPromoCode -> {
                _state.update {
                    it.copy(
                        promoCode = event.promoCode
                    )
                }
                validatePromoCode()
            }
            is CoinPurchaseEvent.SetupData -> {
                _state.update {
                    it.copy(
                        coins = event.coins,
                        totalPrice = event.totalPrice,
                        discountPrice = event.discountPrice
                    )
                }
            }

            CoinPurchaseEvent.StartPayment -> {
                requestPayment()
            }
        }
    }

    private var promoCodeJob: Job? = null

    private fun validatePromoCode(){
        promoCodeJob?.cancel()
        promoCodeJob = screenModelScope.launch {
            _state.update {
                it.copy(
                    promoCodeLoading = true,
                )
            }
            val request = PromoCodeValidationRequest(
                code = _state.value.promoCode,
                amount = _state.value.totalPrice - _state.value.discountPrice
            )
            when(val result = promoCodeValidationUseCase.invoke(request)){
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            promoCodeLoading = false,
                            promoActivated = false,
                        )
                    }
                    _effect.tryEmit(CoinPurchaseEffect.ShowPromoCodeErrorToast(result.message ?: "Server connection error"))
                }
                is Resource.Success -> {
                    val discountedPromoCodePrice = result.data.savings
                    _state.update {
                        it.copy(
                            promoActivated = true,
                            promoCodeLoading = false,
                            promoCodeDiscountPrice = discountedPromoCodePrice,
                        )
                    }
                    _effect.tryEmit(CoinPurchaseEffect.ShowPromoCodeSuccessToast)
                }
                is Resource.Loading -> {}
            }
        }
    }

    private fun requestPayment(){
        screenModelScope.launch {
            _state.update {
                it.copy(
                    paymentLoading = true,
                    paymentActive = true
                )
            }
            val request = PurchaseCoinRequest(
                coins = _state.value.coins
            )
            when(val result = purchaseCoinUseCase.invoke(request)){
                is Resource.Loading<*> -> {}
                is Resource.Error -> {
                    _effect.tryEmit(CoinPurchaseEffect.PaymentFailed(result.message?:"Server connection error"))
                    _state.update {
                        it.copy(
                            paymentLoading = false,
                            paymentActive = false
                        )
                    }
                }
                is Resource.Success-> {
                    _state.update {
                        it.copy(
                            paymentLoading = false,
                            merchantTransId = result.data.merchant_trans_id,
                            serviceId = result.data.service_id
                        )
                    }

                    _effect.tryEmit(
                        CoinPurchaseEffect.OpenClickPayment(
                            serviceId = result.data.service_id,
                            merchantId = _state.value.merchantId,
                            amount = _state.value.finalPrice,
                            transactionId = result.data.merchant_trans_id
                        )
                    )
                    requestPaymentStatus()
                }
            }
        }
    }

    var statusJob: Job? = null
    private fun requestPaymentStatus(){
        statusJob?.cancel()

        val merchantTransId = _state.value.merchantTransId
        if (merchantTransId.isBlank()) return

        statusJob = screenModelScope.launch {

            _state.update {
                it.copy(
                    paymentStatus = PaymentStatus.PENDING
                )
            }

            repeat(120) {
                when (val result = paymentStatusUseCase.invoke(_state.value.merchantTransId)) {
                    is Resource.Loading<*> -> {}
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                paymentStatusError = result.message ?: "Server connection error"
                            )
                        }
                    }

                    is Resource.Success -> {
                        val status = PaymentStatus.fromString(result.data.status)

                        _state.update {
                            it.copy(
                                paymentStatus = PaymentStatus.fromString(result.data.status)
                            )
                        }

                        if (status == PaymentStatus.COMPLETED){
                            _effect.tryEmit(CoinPurchaseEffect.PaymentSuccess)
                        }

                        if (status == PaymentStatus.COMPLETED || status == PaymentStatus.FAILED){
                            _state.update {
                                it.copy(
                                    paymentActive = false
                                )
                            }
                            return@launch
                        }
                    }
                }
                delay(1000)
            }
        }
    }
}