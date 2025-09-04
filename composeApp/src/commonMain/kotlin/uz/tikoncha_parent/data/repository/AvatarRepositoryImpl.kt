package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.remote.AvatarApiService
import uz.tikoncha_parent.data.remote.model.AvatarResponse
import uz.tikoncha_parent.domain.model.UploadPart
import uz.tikoncha_parent.domain.repository.AvatarRepository

class AvatarRepositoryImpl(
    private val api: AvatarApiService
): AvatarRepository {
    override suspend fun uploadAvatar(part: UploadPart): AvatarResponse = api.uploadAvatar(part)
    override suspend fun getAvatarFromServer(): AvatarResponse = api.loadAvatar()
}