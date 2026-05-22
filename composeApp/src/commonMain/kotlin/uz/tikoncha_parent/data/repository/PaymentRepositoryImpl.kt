package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.mapper.toDomain
import uz.tikoncha_parent.data.remote.PaymentApiService
import uz.tikoncha_parent.data.remote.model.PaymentStatusResponse
import uz.tikoncha_parent.data.remote.model.SubscriptionLimitResponse
import uz.tikoncha_parent.data.remote.model.SubscriptionPaymentRequest
import uz.tikoncha_parent.data.remote.model.SubscriptionPaymentResponse
import uz.tikoncha_parent.data.remote.model.SubscriptionPlansResponse
import uz.tikoncha_parent.data.remote.model.SubscriptionStatusResponse
import uz.tikoncha_parent.data.remote.model.subscription.PromoCodeValidationRequest
import uz.tikoncha_parent.data.remote.model.subscription.PromoCodeValidationResponse
import uz.tikoncha_parent.domain.model.subscription.CoinPackageListResponse
import uz.tikoncha_parent.domain.model.subscription.PurchaseCoinRequest
import uz.tikoncha_parent.domain.model.subscription.PurchaseCoinResponse
import uz.tikoncha_parent.domain.model.subscription.SubscriptionStatus
import uz.tikoncha_parent.domain.model.transaction.TransactionPage
import uz.tikoncha_parent.domain.repository.PaymentRepository

class PaymentRepositoryImpl(private val api: PaymentApiService) : PaymentRepository {

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

    override suspend fun promoCodeValidation(promoCodeValidationRequest: PromoCodeValidationRequest): PromoCodeValidationResponse {
        return api.promoCodeValidation(promoCodeValidationRequest)
    }

    override suspend fun coinPackages(): CoinPackageListResponse {
        return api.coinPackageList()
    }

    override suspend fun purchaseCoin(purchaseCoinRequest: PurchaseCoinRequest): PurchaseCoinResponse {
        return api.purchaseCoin(purchaseCoinRequest)
    }

    override suspend fun paymentTransactions(
        limit: Int,
        offset: Int
    ): TransactionPage {
        val response = api.paymentTransactions(limit = limit, offset = offset)

        if (!response.success || response.data == null) {
            throw IllegalStateException(
                response.error
                    ?: "Failed to load transactions (code=${response.code ?: "unknown"})"
            )
        }

        return response.data.toDomain()
    }


    override suspend fun getSubscriptionStatus(userId: String?): SubscriptionStatusResponse {
        return api.getSubscriptionStatus(userId)
    }
}