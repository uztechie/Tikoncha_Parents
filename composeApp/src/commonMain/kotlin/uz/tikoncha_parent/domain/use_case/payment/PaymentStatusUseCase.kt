package uz.tikoncha_parent.domain.use_case.payment

import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.mapper.toSubscriptionLimit
import uz.tikoncha_parent.data.remote.model.PaymentStatusData
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.PaymentRepository

class PaymentStatusUseCase(
    private val paymentRepository: PaymentRepository,
) {
    suspend operator fun invoke(merchantTransId: String): Resource<PaymentStatusData> {
        return try {
            val response = paymentRepository.paymentStatus(merchantTransId)
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