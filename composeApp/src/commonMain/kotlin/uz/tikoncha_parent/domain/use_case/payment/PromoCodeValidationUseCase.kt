package uz.tikoncha_parent.domain.use_case.payment

import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.remote.model.subscription.PromoCodeValidationData
import uz.tikoncha_parent.data.remote.model.subscription.PromoCodeValidationRequest
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.PaymentRepository


class PromoCodeValidationUseCase(
    private val paymentRepository: PaymentRepository,
) {
    suspend operator fun invoke(promoCodeValidationRequest: PromoCodeValidationRequest): Resource<PromoCodeValidationData> {
        return try {
            val response = paymentRepository.promoCodeValidation(promoCodeValidationRequest)
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