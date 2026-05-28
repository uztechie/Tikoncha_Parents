package uz.tikoncha_parent.domain.use_case.auth

import okio.IOException
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.iltimos_internetga_ulang
import tikoncha_parents.composeapp.generated.resources.kirish_bekor_qilindi
import tikoncha_parents.composeapp.generated.resources.kutilmagan_xatolik_qayta_urining
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import tikoncha_parents.composeapp.generated.resources.xatolik_yuz_berdi
import uz.saidburxon.newedu.data.model.SendOtpRequest
import uz.saidburxon.newedu.data.model.VerifyOtpRequest
import uz.saidburxon.newedu.data.model.VerifyOtpResponseData
import uz.tikoncha_parent.data.remote.model.auth.TelegramLoginRequest
import uz.tikoncha_parent.data.remote.telegram.TelegramAuthClient
import uz.tikoncha_parent.data.remote.telegram.TelegramAuthResult
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.LoginRepository

class TelegramLoginUseCase(
    private val repository: LoginRepository,
    private val telegramAuthClient: TelegramAuthClient
) {

    suspend operator fun invoke(): Resource<VerifyOtpResponseData> {
        return try {

            val result = telegramAuthClient.login()

            when(result){
                TelegramAuthResult.Cancelled -> {
                    Resource.Error(
                        message = "",
                        resId = Res.string.kirish_bekor_qilindi
                    )
                }
                is TelegramAuthResult.Error -> {
                    Resource.Error(
                        message = result.message,
                        resId = Res.string.xatolik_yuz_berdi
                    )
                }
                is TelegramAuthResult.Success -> {
                    val idToken = result.idToken
                    val request = TelegramLoginRequest(idToken)

                    val response = repository.telegramLogin(request)
                    if (response.success && response.data != null){
                        Resource.Success(response.data)
                    }
                    else {
                        Resource.Error(
                            message = response.error,
                            resId = Res.string.server_connection_error
                        )
                    }
                }
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