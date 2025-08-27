package org.example.project.domain.repository

import org.example.project.data.remote.model.AvatarResponse
import org.example.project.domain.model.UploadPart

interface AvatarRepository {
    suspend fun uploadAvatar(part: UploadPart): AvatarResponse
    suspend fun getAvatarFromServer(): AvatarResponse
}