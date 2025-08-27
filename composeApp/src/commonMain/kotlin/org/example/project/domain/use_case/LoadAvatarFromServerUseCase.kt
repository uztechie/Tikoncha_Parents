package org.example.project.domain.use_case

import org.example.project.data.remote.model.AvatarDto
import org.example.project.domain.model.Resource
import org.example.project.domain.repository.AvatarRepository
import org.example.project.platform.Logger

class LoadAvatarFromServerUseCase(
    private val repository: AvatarRepository
){
    suspend operator fun invoke(): Resource<AvatarDto> {
        return try {
            val response = repository.getAvatarFromServer()
            if (response.success && response.data != null){
                Resource.Success(response.data)
            }else{
                Resource.Error(response.error ?: "")
            }
        }catch (e: Exception){
            e.printStackTrace()
            Logger.e("AvatarApi", "message: ", e)
            Resource.Error(e.message ?: "Xatolik")
        }
    }
}