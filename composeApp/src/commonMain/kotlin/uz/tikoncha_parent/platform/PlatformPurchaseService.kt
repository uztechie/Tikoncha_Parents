package uz.tikoncha_parent.platform

import uz.tikoncha_parent.domain.model.PurchaseResult

expect class PlatformPurchaseService() {
    suspend fun purchase(productId: String): PurchaseResult
}