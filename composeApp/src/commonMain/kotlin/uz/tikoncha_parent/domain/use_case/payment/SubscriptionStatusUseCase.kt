package uz.tikoncha_parent.domain.use_case.payment

import io.ktor.http.cio.Response
import okio.IOException
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.iltimos_internetga_ulang
import tikoncha_parents.composeapp.generated.resources.kutilmagan_xatolik_qayta_urining
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.mapper.toDomain
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.model.subscription.SubscriptionStatus
import uz.tikoncha_parent.domain.repository.PaymentRepository

class GetSubscriptionStatusUseCase(
    private val subscriptionRepository: PaymentRepository
) {
    suspend operator fun invoke(userId: String? = null): Resource<SubscriptionStatus> {
        try {
            val result = subscriptionRepository.getSubscriptionStatus(userId)
            if (result.data != null && result.success) {
                return Resource.Success(result.data.toDomain())
            } else {
                return Resource.Error(
                    message = result.error,
                    resId = Res.string.kutilmagan_xatolik_qayta_urining
                )
            }
        } catch (e: IOException) {
            return Resource.Error(
                resId = Res.string.iltimos_internetga_ulang,
                cause = e
            )
        } catch (e: Exception) {
            e.printStackTrace()
           return Resource.Error(
                resId = Res.string.kutilmagan_xatolik_qayta_urining,
                cause = e
            )
        }
    }
}