package uz.tikoncha_parent.domain.use_case

import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.LoginRepository
import uz.saidburxon.newedu.data.model.VerifyOtpRequest
import uz.saidburxon.newedu.data.model.VerifyOtpResponseData


class VerifyOtpUseCase(
    private val repository: LoginRepository,
) {
    suspend operator fun invoke(request: VerifyOtpRequest): Resource<VerifyOtpResponseData> {
        return try {
            val response = repository.verifyOtp(request)
            if (response.success && response.data != null){
                Resource.Success(response.data)
            }
            else{
                Resource.Error(response.error?: "")
            }


        }
        catch (e: Exception){
            Resource.Error("Xatolik")

        }
        catch (e: Exception){
            e.printStackTrace()
            Resource.Error("Xatolik")
        }

    }
}