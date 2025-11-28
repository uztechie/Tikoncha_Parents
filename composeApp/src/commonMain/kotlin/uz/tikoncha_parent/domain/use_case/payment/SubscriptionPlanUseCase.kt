package uz.tikoncha_parent.domain.use_case.payment

import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.mapper.toSubscriptionPlanUi
import uz.tikoncha_parent.data.remote.model.CreatePolicyRequest
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.PaymentRepository
import uz.tikoncha_parent.presentation.profile.subscription.SubscriptionUi

class SubscriptionPlanUseCase(
    private val paymentRepository: PaymentRepository,
) {
    suspend operator fun invoke(): Resource<List<SubscriptionUi>> {
        return try {
            val response = paymentRepository.subscriptionPlans()
            if (response.success && response.data != null){
                val ui = response.data.map { it.toSubscriptionPlanUi() }
                return Resource.Success(ui)
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