package org.example.project.presentation.profile

import org.example.project.domain.model.UploadPart

sealed interface ProfileEvent {
    data class OnChangeProfilePhotoClicked(val image: String?): ProfileEvent
    data class OnAvatarPhotoSelected(val part: UploadPart): ProfileEvent
    object LoadAvatarFromServer: ProfileEvent
}