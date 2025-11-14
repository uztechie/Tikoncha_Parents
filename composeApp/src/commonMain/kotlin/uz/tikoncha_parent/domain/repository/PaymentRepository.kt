package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.data.remote.model.SubscriptionPurchaseRequest
import uz.tikoncha_parent.data.remote.model.SubscriptionPurchaseResponse

interface PaymentRepository {
    suspend fun subscriptionPurchase(subscriptionPurchaseRequest: SubscriptionPurchaseRequest): SubscriptionPurchaseResponse
}