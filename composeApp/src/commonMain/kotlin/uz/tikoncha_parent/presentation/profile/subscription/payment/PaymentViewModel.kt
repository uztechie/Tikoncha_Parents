package uz.tikoncha_parent.presentation.profile.subscription.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.remote.model.SubscriptionPaymentRequest
import uz.tikoncha_parent.domain.model.PaymentStatus
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.service.PaymentService
import uz.tikoncha_parent.domain.use_case.payment.PaymentStatusUseCase
import uz.tikoncha_parent.domain.use_case.payment.SubscriptionLimitUseCase
import uz.tikoncha_parent.domain.use_case.payment.SubscriptionPaymentUseCase
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class PaymentViewModel(
    private val subscriptionLimitUseCase: SubscriptionLimitUseCase,
    private val paymentUseCase: SubscriptionPaymentUseCase,
    private val paymentStatusUseCase: PaymentStatusUseCase,
    private val paymentService: PaymentService
): ViewModel() {

    private val _state = MutableStateFlow(PaymentState())
    val state = _state.asStateFlow()

    fun onEvent(event: PaymentEvent){
        when(event){
            PaymentEvent.Pay -> {
                requestPayment()
            }

            PaymentEvent.ResetPaymentResponse -> {
                _state.update {
                    it.copy(
                        paymentResponseState = ResponseState.Idle
                    )
                }
            }
            is PaymentEvent.SetAmount -> {
                _state.update {
                    it.copy(
                        amount = event.amount
                    )
                }

            }

            is PaymentEvent.SetSubscriptionDuration -> {
                _state.update {
                    it.copy(
                        subscriptionDuration = event.duration
                    )
                }
            }

            is PaymentEvent.SetPlanId -> {
                _state.update {
                    it.copy(
                        planId = event.planId
                    )
                }
            }
        }
    }

    private fun requestPayment(){
        viewModelScope.launch {
            _state.update {
                it.copy(
                    paymentResponseState = ResponseState.Loading
                )
            }
            val request = SubscriptionPaymentRequest(
                plan_id = _state.value.planId,
                plan_duration = _state.value.subscriptionDuration.name,
                child_user_id = AppSettings.selectedChild?.userId?:""
            )
            val result = paymentUseCase.invoke(request)
            when(result){
                is Resource.Loading<*> -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            paymentResponseState = ResponseState.Error(
                                message = result.message,
                                res = result.resId
                            )
                        )
                    }
                }
                is Resource.Success-> {
                    _state.update {
                        it.copy(
                            paymentResponseState = ResponseState.Success(),
                            merchantTransId = result.data.merchant_trans_id,
                            serviceId = result.data.service_id,
                            amount = result.data.amount
                        )
                    }

                    openClickPayment(
                        serviceId = _state.value.serviceId.toString(),
                        merchantId = _state.value.merchantId.toString(),
                        amount = _state.value.amount.toString(),
                        transactionId = _state.value.merchantTransId
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

        statusJob = viewModelScope.launch {

            _state.update {
                it.copy(
                    paymentStatus = PaymentStatus.PENDING
                )
            }

            repeat(120) {
                val result = paymentStatusUseCase.invoke(_state.value.merchantTransId)
                when (result) {
                    is Resource.Loading<*> -> {}
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                paymentStatusError = result.message?:"Server connection error"
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
                            requestSubscriptionLimit()
                        }

                        if (status == PaymentStatus.COMPLETED || status == PaymentStatus.FAILED){
                            return@launch
                        }

                    }
                }
                delay(1000)
            }
        }
    }

    private fun requestSubscriptionLimit(){
        viewModelScope.launch {
            subscriptionLimitUseCase.invoke()
        }
    }
    private fun openClickPayment(
        serviceId: String,
        merchantId: String,
        amount: String,
        transactionId: String
    ) {


        paymentService.openClickPayment(
            serviceId = serviceId,
            merchantId = merchantId,
            amount = amount,
            transactionId = transactionId

        )
    }

}

