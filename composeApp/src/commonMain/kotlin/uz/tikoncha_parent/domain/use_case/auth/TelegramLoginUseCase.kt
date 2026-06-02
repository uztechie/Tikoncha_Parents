package uz.tikoncha_parent.domain.use_case.auth

import kotlinx.io.IOException
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.iltimos_internetga_ulang
import tikoncha_parents.composeapp.generated.resources.kutilmagan_xatolik_qayta_urining
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.saidburxon.newedu.data.model.VerifyOtpResponseData
import uz.tikoncha_parent.data.remote.model.auth.TelegramLoginRequest
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.LoginRepository

class TelegramLoginUseCase(
    private val repository: LoginRepository,
) {
    suspend operator fun invoke(idToken: String): Resource<VerifyOtpResponseData> {
        return try {
            val response = repository.telegramLogin(TelegramLoginRequest(token = idToken))
            if (response.success && response.data != null) {
                Resource.Success(response.data)
            } else {
                Resource.Error(
                    message = response.error,
                    resId = Res.string.server_connection_error,
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