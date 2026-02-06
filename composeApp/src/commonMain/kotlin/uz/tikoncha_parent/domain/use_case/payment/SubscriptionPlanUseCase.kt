package uz.tikoncha_parent.domain.use_case.payment

import okio.IOException
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.iltimos_internetga_ulang
import tikoncha_parents.composeapp.generated.resources.kutilmagan_xatolik_qayta_urining
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
        } catch (e: IOException) {
            Resource.Error(
                resId = Res.string.iltimos_internetga_ulang,
                cause = e
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                resId = Res.string.kutilmagan_xatolik_qayta_urining,
                cause = e
            )
        }
    }
}