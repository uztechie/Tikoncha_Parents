package uz.tikoncha_parent.presentation.profile.coin_purchase

sealed interface CoinPurchaseEvent {
    data class ApplyPromoCode(val promoCode: String): CoinPurchaseEvent
    data class SetupData(
        val coins: Int,
        val totalPrice: Int,
        val discountPrice: Int,
    ): CoinPurchaseEvent

    data object StartPayment: CoinPurchaseEvent
}