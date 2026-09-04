package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.data.remote.model.AvatarDto
import uz.tikoncha_parent.domain.model.UploadPart
import uz.tikoncha_parent.domain.model.app_error.Outcome

interface AvatarRepository {
    suspend fun uploadAvatar(part: UploadPart): Outcome<AvatarDto>
    suspend fun getAvatarFromServer(): Outcome<AvatarDto>
    suspend fun deleteAvatar(): Outcome<Unit>
}