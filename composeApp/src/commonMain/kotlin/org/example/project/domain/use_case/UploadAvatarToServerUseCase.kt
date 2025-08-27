package org.example.project.domain.use_case

import org.example.project.data.remote.model.AvatarDto
import org.example.project.domain.model.Resource
import org.example.project.domain.model.UploadPart
import org.example.project.domain.repository.AvatarRepository
import org.example.project.platform.Logger

class UploadAvatarToServerUseCase(
    private val repository: AvatarRepository
) {
    suspend operator fun invoke(part: UploadPart): Resource<AvatarDto>{
        return try {
            val response = repository.uploadAvatar(part)
            if (response.success && response.data != null){
                Resource.Success(response.data)
            }else{
                Resource.Error(response.error ?: "0")
            }
        }catch (e: Exception){
            e.printStackTrace()
            Logger.e("AvatarApi", "message: ", e)
            Resource.Error(e.message ?: "Xatolik")
        }
    }
}