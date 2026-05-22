    package uz.tikoncha_parent.domain.repository

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

    interface PaymentRepository {
        suspend fun subscriptionPayment(subscriptionPaymentRequest: SubscriptionPaymentRequest): SubscriptionPaymentResponse

        suspend fun getSubscriptionLimitsFromServer(): SubscriptionLimitResponse

        suspend fun subscriptionPlans(): SubscriptionPlansResponse

        suspend fun paymentStatus(merchantTransId: String): PaymentStatusResponse

        suspend fun promoCodeValidation(promoCodeValidationRequest: PromoCodeValidationRequest): PromoCodeValidationResponse

        suspend fun coinPackages(): CoinPackageListResponse

        suspend fun purchaseCoin(purchaseCoinRequest: PurchaseCoinRequest): PurchaseCoinResponse

        suspend fun paymentTransactions(limit: Int, offset: Int): TransactionPage

        suspend fun getSubscriptionStatus(userId: String? = null): SubscriptionStatusResponse
    }