package uz.tikoncha_parent.domain.use_case.payment

import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.domain.model.PurchaseResult
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.platform.PlatformPurchaseService

class PurchaseIApPremiumUseCase (
    private val platform: PlatformPurchaseService
) {
    suspend operator fun invoke(productId: String): PurchaseResult {
        val result = platform.purchase(productId)
        Logger.e("PurchaseIApPremiumUseCase", "result=$result")
        return result
    }
}