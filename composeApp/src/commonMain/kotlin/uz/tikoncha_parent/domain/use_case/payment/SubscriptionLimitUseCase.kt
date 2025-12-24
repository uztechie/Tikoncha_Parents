package uz.tikoncha_parent.domain.use_case.payment

import okio.IOException
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.no_internet_connection
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.mapper.toSubscriptionLimit
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.PaymentRepository

class SubscriptionLimitUseCase(
    private val paymentRepository: PaymentRepository,
) {
    suspend operator fun invoke(): Resource<String> {
        return try {
            val response = paymentRepository.getSubscriptionLimitsFromServer()
            if (response.success && response.data != null){
                AppSettings.subscriptionLimitList = response.data.children.map {
                    it.toSubscriptionLimit()
                }
                Resource.Success("")
            }
            else {
                Resource.Error(
                    message = response.error,
                    resId = Res.string.server_connection_error
                )
            }
        }
        catch (e: IOException){
            Resource.Error(
                resId = Res.string.no_internet_connection,
                cause = e
            )
        }
        catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                resId = Res.string.server_connection_error,
                cause = e
            )
        }

    }
}