package uz.tikoncha_parent.domain.use_case

import uz.tikoncha_parent.data.remote.model.RegisterUserRequest
import uz.tikoncha_parent.data.remote.model.UserInfoDto
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.LoginRepository


class RegisterUseCase(
    private val repository: LoginRepository,
) {
    suspend operator fun invoke(request: RegisterUserRequest): Resource<UserInfoDto> {
        return try {
            val response = repository.registerUser(request)
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