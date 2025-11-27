package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.data.remote.model.PaymentStatusResponse
import uz.tikoncha_parent.data.remote.model.SubscriptionLimitResponse
import uz.tikoncha_parent.data.remote.model.SubscriptionPaymentRequest
import uz.tikoncha_parent.data.remote.model.SubscriptionPaymentResponse
import uz.tikoncha_parent.data.remote.model.SubscriptionPlansResponse

interface PaymentRepository {
    suspend fun subscriptionPayment(subscriptionPaymentRequest: SubscriptionPaymentRequest): SubscriptionPaymentResponse

    suspend fun getSubscriptionLimitsFromServer(): SubscriptionLimitResponse

    suspend fun subscriptionPlans(): SubscriptionPlansResponse

    suspend fun paymentStatus(merchantTransId: String): PaymentStatusResponse


}