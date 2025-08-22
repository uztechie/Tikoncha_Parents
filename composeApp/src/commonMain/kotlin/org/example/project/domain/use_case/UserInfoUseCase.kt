package org.example.project.domain.use_case

import org.example.project.data.remote.model.RegisterUserRequest
import org.example.project.data.remote.model.RegisterUserResponse
import org.example.project.data.remote.model.UserInfoDto
import org.example.project.domain.model.Resource
import org.example.project.domain.repository.LoginRepository
import uz.saidburxon.newedu.data.model.SendOtpRequest


class UserInfoUseCase(
    private val repository: LoginRepository,
) {
    suspend operator fun invoke(): Resource<UserInfoDto> {
        return try {
            val response = repository.userInfo()
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