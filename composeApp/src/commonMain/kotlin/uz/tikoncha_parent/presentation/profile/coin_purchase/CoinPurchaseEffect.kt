package uz.tikoncha_parent.presentation.profile.coin_purchase

sealed class CoinPurchaseEffect {
    data class ShowPromoCodeErrorToast(val message: String): CoinPurchaseEffect()

    data object PaymentSuccess: CoinPurchaseEffect()
    data class PaymentFailed(val message: String): CoinPurchaseEffect()
    data object ShowPromoCodeSuccessToast: CoinPurchaseEffect()

    data class OpenClickPayment(
        val serviceId: Int,
        val merchantId: Int,
        val amount: Int,
        val transactionId: String,
    ) : CoinPurchaseEffect()
}