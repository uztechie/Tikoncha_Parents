package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.remote.PaymentApiService
import uz.tikoncha_parent.data.remote.model.PaymentStatusResponse
import uz.tikoncha_parent.data.remote.model.SubscriptionLimitResponse
import uz.tikoncha_parent.data.remote.model.SubscriptionPaymentRequest
import uz.tikoncha_parent.data.remote.model.SubscriptionPaymentResponse
import uz.tikoncha_parent.data.remote.model.SubscriptionPlansResponse
import uz.tikoncha_parent.domain.repository.PaymentRepository

class PaymentRepositoryImpl(private val api: PaymentApiService): PaymentRepository {
    override suspend fun subscriptionPayment(subscriptionPaymentRequest: SubscriptionPaymentRequest): SubscriptionPaymentResponse {
        return api.subscriptionPayment(subscriptionPaymentRequest)
    }

    override suspend fun getSubscriptionLimitsFromServer(): SubscriptionLimitResponse {
        return api.subscriptionLimits()
    }

    override suspend fun subscriptionPlans(): SubscriptionPlansResponse {
        return api.subscriptionPlans()
    }

    override suspend fun paymentStatus(merchantTransId: String): PaymentStatusResponse {
        return api.paymentStatus(merchantTransId)
    }


}