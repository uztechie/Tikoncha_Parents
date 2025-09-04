package uz.tikoncha_parent.domain.use_case

import uz.tikoncha_parent.data.remote.model.AvatarDto
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.AvatarRepository
import uz.tikoncha_parent.platform.Logger

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