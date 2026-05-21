package uz.tikoncha_parent.domain.use_case.payment

import uz.tikoncha_parent.domain.model.subscription.SubscriptionStatus
import uz.tikoncha_parent.domain.repository.PaymentRepository

class GetSubscriptionStatusUseCase(
    private val subscriptionRepository: PaymentRepository
) {
    suspend operator fun invoke(userId: String? = null): SubscriptionStatus =
        subscriptionRepository.getSubscriptionStatus(userId)
}