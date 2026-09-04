package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.remote.AvatarApiService
import uz.tikoncha_parent.data.remote.app_error.ApiErrorMapper
import uz.tikoncha_parent.data.remote.model.AvatarDto
import uz.tikoncha_parent.domain.model.UploadPart
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.repository.AvatarRepository

class AvatarRepositoryImpl(
    private val api: AvatarApiService
) : AvatarRepository {

    override suspend fun uploadAvatar(part: UploadPart): Outcome<AvatarDto> = apiCall(TAG) {
        val r = api.uploadAvatar(part)
        val data = r.data
        if (r.success && data != null) Outcome.Success(data)
        else Outcome.Failure(ApiErrorMapper.fromCode(r.code), r.error)
    }

    override suspend fun getAvatarFromServer(): Outcome<AvatarDto> = apiCall(TAG) {
        val r = api.loadAvatar()
        val data = r.data
        if (r.success && data != null) Outcome.Success(data)
        else Outcome.Failure(ApiErrorMapper.fromCode(r.code), r.error)
    }

    override suspend fun deleteAvatar(): Outcome<Unit> = apiCall(TAG) {
        val r = api.deleteAvatar()
        if (r.success) Outcome.Success(Unit)
        else Outcome.Failure(ApiErrorMapper.fromCode(r.code), r.error)
    }

    private companion object { const val TAG = "AvatarRepository" }
}