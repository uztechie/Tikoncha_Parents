package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.data.remote.model.AvatarResponse
import uz.tikoncha_parent.domain.model.UploadPart

interface AvatarRepository {
    suspend fun uploadAvatar(part: UploadPart): AvatarResponse
    suspend fun getAvatarFromServer(): AvatarResponse
}