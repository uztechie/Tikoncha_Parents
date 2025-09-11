package uz.tikoncha_parent.domain.use_case

import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.remote.model.AvatarDto
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.model.UploadPart
import uz.tikoncha_parent.domain.repository.AvatarRepository
import uz.tikoncha_parent.platform.Logger

class UploadAvatarToServerUseCase(
    private val repository: AvatarRepository
) {
    suspend operator fun invoke(part: UploadPart): Resource<AvatarDto>{
        return try {
            val response = repository.uploadAvatar(part)
            if (response.success && response.data != null){
                Resource.Success(response.data)
            }else {
                Resource.Error(
                    message = response.error,
                    resId = Res.string.server_connection_error
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                resId = Res.string.server_connection_error,
                cause = e
            )
        }
    }
}