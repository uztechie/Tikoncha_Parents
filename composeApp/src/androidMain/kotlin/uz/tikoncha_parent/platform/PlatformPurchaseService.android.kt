package uz.tikoncha_parent.platform

import uz.tikoncha_parent.domain.model.PurchaseResult

actual class PlatformPurchaseService actual constructor() {
    actual suspend fun purchase(productId: String): PurchaseResult {
        return PurchaseResult.Error("Android purchase is handled via Click/Payme")
    }
}