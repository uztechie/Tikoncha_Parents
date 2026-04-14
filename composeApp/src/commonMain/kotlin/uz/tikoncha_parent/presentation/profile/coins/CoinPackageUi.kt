package uz.tikoncha_parent.presentation.profile.coins

data class CoinPackageUi(
    val coins: Int,
    val priceInString: String,
    val discountPercent: Int,
    val priceWithDiscountInString: String,
    val price: Long,
    val priceWithDiscount: Long,
    val discountedPrice: Long
)
