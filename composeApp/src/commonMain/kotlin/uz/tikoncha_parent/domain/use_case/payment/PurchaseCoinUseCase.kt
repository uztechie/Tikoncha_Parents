package uz.tikoncha_parent.domain.use_case.payment

import kotlinx.io.IOException
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.kutilmagan_xatolik_qayta_urining
import tikoncha_parents.composeapp.generated.resources.no_internet_connection
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.model.subscription.PurchaseCoinData
import uz.tikoncha_parent.domain.model.subscription.PurchaseCoinRequest
import uz.tikoncha_parent.domain.repository.PaymentRepository


class PurchaseCoinUseCase (
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(request: PurchaseCoinRequest): Resource<PurchaseCoinData> {
        return try {
            val response = paymentRepository.purchaseCoin(request)
            if (response.success && response.data != null){
                Resource.Success(response.data)
            }
            else {
                Resource.Error(
                    message = response.error,
                    resId = Res.string.server_connection_error
                )
            }
        } catch (e: IOException) {
            Resource.Error(Res.string.no_internet_connection)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(Res.string.kutilmagan_xatolik_qayta_urining)
        }
    }
}