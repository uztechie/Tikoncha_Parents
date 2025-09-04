package uz.tikoncha_parent.domain.use_case

import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.LoginRepository
import uz.saidburxon.newedu.data.model.SendOtpRequest


class SendOtpUseCase(
    private val repository: LoginRepository,
) {
    suspend operator fun invoke(request: SendOtpRequest): Resource<Boolean> {
        return try {
            val response = repository.sendOtp(request)
            if (response.success){
                Resource.Success(true)
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