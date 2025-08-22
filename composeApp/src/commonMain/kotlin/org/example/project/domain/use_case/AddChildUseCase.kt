package org.example.project.domain.use_case

import androidx.navigation.NavUri
import org.example.project.data.remote.model.AddChildRequest
import org.example.project.domain.model.Resource
import org.example.project.domain.repository.ChildRepository
import org.example.project.domain.repository.LoginRepository
import org.example.project.platform.Logger
import uz.saidburxon.newedu.data.model.SendOtpRequest


class AddChildUseCase(
    private val repository: ChildRepository,
) {
    suspend operator fun invoke(request: AddChildRequest): Resource<String> {
        return try {
            val response = repository.addChild(request)
            if (response.success && response.data != null){
                Resource.Success(response.data.code)
            }
            else{
                Resource.Error(response.error?: "")
            }


        }

        catch (e: Exception){
            e.printStackTrace()
            Logger.e("XATOOOO", "message: ",e)
            Resource.Error("Xatolik")
        }

    }
}