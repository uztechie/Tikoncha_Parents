package uz.tikoncha_parent.domain.use_case

import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.remote.model.CreatePolicyRequest
import uz.tikoncha_parent.data.remote.model.CreatePolicyRequestTemp
import uz.tikoncha_parent.data.remote.model.SubscriptionPurchaseData
import uz.tikoncha_parent.data.remote.model.SubscriptionPurchaseRequest
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.PaymentRepository
import uz.tikoncha_parent.domain.repository.PolicyRepository
import uz.tikoncha_parent.domain.repository.RulesRepository

class SubscriptionPurchaseUseCase(
    private val paymentRepository: PaymentRepository,
) {
    suspend operator fun invoke(subscriptionPurchaseRequest: SubscriptionPurchaseRequest): Resource<SubscriptionPurchaseData>{
        return try {
            val response = paymentRepository.subscriptionPurchase(subscriptionPurchaseRequest)
            if (response.success && response.data != null){
                Resource.Success(response.data)
            }
            else {
                Resource.Error(
                    message = response.error,
                    resId = Res.string.server_connection_error
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                resId = Res.string.server_connection_error,
                cause = e
            )
        }

    }
}