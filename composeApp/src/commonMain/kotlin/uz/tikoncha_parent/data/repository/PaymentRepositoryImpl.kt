package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.mapper.toDomain
import uz.tikoncha_parent.data.mapper.toSubscriptionLimit
import uz.tikoncha_parent.data.remote.PaymentApiService
import uz.tikoncha_parent.data.remote.app_error.ApiErrorMapper
import uz.tikoncha_parent.data.remote.model.PaymentStatusResponse
import uz.tikoncha_parent.data.remote.model.SubscriptionLimitResponse
import uz.tikoncha_parent.data.remote.model.SubscriptionPaymentRequest
import uz.tikoncha_parent.data.remote.model.SubscriptionPaymentResponse
import uz.tikoncha_parent.data.remote.model.SubscriptionPlansResponse
import uz.tikoncha_parent.data.remote.model.SubscriptionStatusResponse
import uz.tikoncha_parent.data.remote.model.subscription.PromoCodeValidationRequest
import uz.tikoncha_parent.data.remote.model.subscription.PromoCodeValidationResponse
import uz.tikoncha_parent.domain.model.SubscriptionLimit
import uz.tikoncha_parent.domain.model.app_error.ErrorCause
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.model.subscription.CoinPackageListResponse
import uz.tikoncha_parent.domain.model.subscription.CoinPackageListWrapper
import uz.tikoncha_parent.domain.model.subscription.PurchaseCoinRequest
import uz.tikoncha_parent.domain.model.subscription.PurchaseCoinResponse
import uz.tikoncha_parent.domain.model.subscription.SubscriptionStatus
import uz.tikoncha_parent.domain.model.transaction.TransactionPage
import uz.tikoncha_parent.domain.repository.PaymentRepository

class PaymentRepositoryImpl(
    private val api: PaymentApiService
) : PaymentRepository {

    override suspend fun subscriptionPayment(subscriptionPaymentRequest: SubscriptionPaymentRequest): SubscriptionPaymentResponse {
        return api.subscriptionPayment(subscriptionPaymentRequest)
    }

    override suspend fun syncSubscriptionLimits(): Outcome<List<SubscriptionLimit>> =
        apiCall(TAG) {
            val r = api.subscriptionLimits()
            val body = r.data
            when {
                r.success && body != null -> {
                    val limits = body.children.map { it.toSubscriptionLimit() }
                    AppSettings.subscriptionLimitList = limits      // kesh — endi shu yerda
                    Outcome.Success(limits)
                }

                r.success -> Outcome.Failure(ErrorCause.InvalidResponse)
                else -> Outcome.Failure(ApiErrorMapper.fromCode(r.code), r.error)
            }
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

    override suspend fun coinPackages(): Outcome<CoinPackageListWrapper> = apiCall(TAG) {
        val r = api.coinPackageList()
        val data = r.data
        if (r.success && data != null) Outcome.Success(data)
        else Outcome.Failure(ApiErrorMapper.fromCode(r.code), r.error)
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

    private companion object {
        const val TAG = "PaymentRepository"
    }
}