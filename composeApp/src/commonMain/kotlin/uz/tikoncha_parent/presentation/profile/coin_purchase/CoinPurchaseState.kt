package uz.tikoncha_parent.presentation.profile.coin_purchase

import uz.tikoncha_parent.domain.model.PaymentStatus

data class CoinPurchaseState(
    val coins: Int = 0,
    val totalPrice: Int = 100,
    val promoCodeDiscountPrice: Int = 0,
    val discountPrice: Int = 0,
    val paymentLoading: Boolean = false,
    val paymentActive: Boolean = false,
    val merchantTransId: String = "",
    val serviceId: Int = 0,
    val merchantId: Int = 46788,
    val paymentStatus: PaymentStatus = PaymentStatus.START,
    val paymentStatusError: String = "",
    val planId: String = "",
    val promoActivated: Boolean = false,
    val promoCode: String = "",
    val discountAmount: Int = 0,
    val discountSaving: Int = 0,
    val discountPercentage: Int = 0,
    val promoCodeLoading: Boolean = false,
){
    val finalPrice: Int
        get() = totalPrice - promoCodeDiscountPrice - discountPrice
}
