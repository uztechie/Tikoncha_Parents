package uz.tikoncha_parent.presentation.profile.subscription.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.remote.model.SubscriptionPaymentRequest
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.service.PaymentService
import uz.tikoncha_parent.domain.use_case.payment.SubscriptionPaymentUseCase
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class PaymentViewModel(
    private val subscriptionPaymentUseCase: SubscriptionPaymentUseCase,
    private val paymentService: PaymentService
): ViewModel() {

    private val _state = MutableStateFlow(PaymentState())
    val state = _state.asStateFlow()

    fun onEvent(event: PaymentEvent){
        when(event){
            PaymentEvent.Purchase -> {
                purchaseSubscription()
            }

            PaymentEvent.ResetResponseState -> {
                _state.update {
                    it.copy(
                        responseState = ResponseState.Idle
                    )
                }
            }
        }
    }

    private fun purchaseSubscription(){
        viewModelScope.launch {
            _state.update {
                it.copy(
                    responseState = ResponseState.Loading
                )
            }

            val request = SubscriptionPaymentRequest(
                child_user_id = "be13765f-3121-4db2-9308-4ebb28627da5",
                tier = "MONTHLY_1"
            )

            val result = subscriptionPaymentUseCase(request)
            when(result){
                is Resource.Loading<*> -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            responseState = ResponseState.Error(
                                res = result.resId,
                                message = result.message
                            )
                        )
                    }
                }
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            responseState = ResponseState.Success(),
                            serviceId = result.data.service_id,
                            merchantTransId = result.data.merchant_trans_id,
                            amount = result.data.amount
                        )
                    }

                    paymentService.openClickPayment(
                        serviceId = _state.value.serviceId.toString(),
                        merchantTransId = _state.value.merchantTransId,
                        amount = _state.value.amount.toString(),
                        transactionId = ""

                    )
                }
            }
        }
    }


}