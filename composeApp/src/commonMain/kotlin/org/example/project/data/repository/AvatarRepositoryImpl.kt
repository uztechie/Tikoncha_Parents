package org.example.project.data.repository

import org.example.project.data.remote.AvatarApiService
import org.example.project.data.remote.model.AvatarResponse
import org.example.project.domain.model.UploadPart
import org.example.project.domain.repository.AvatarRepository

class AvatarRepositoryImpl(
    private val api: AvatarApiService
): AvatarRepository {
    override suspend fun uploadAvatar(part: UploadPart): AvatarResponse = api.uploadAvatar(part)
    override suspend fun getAvatarFromServer(): AvatarResponse = api.loadAvatar()
}