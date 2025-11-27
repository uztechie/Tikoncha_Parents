package uz.tikoncha_parent.domain.use_case.payment

import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.remote.model.SubscriptionPaymentRequest
import uz.tikoncha_parent.data.remote.model.SubscriptionPurchaseData
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.PaymentRepository

class SubscriptionPaymentUseCase(
    private val paymentRepository: PaymentRepository,
) {
    suspend operator fun invoke(subscriptionPaymentRequest: SubscriptionPaymentRequest): Resource<SubscriptionPurchaseData> {
        return try {
            val response = paymentRepository.subscriptionPayment(subscriptionPaymentRequest)
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