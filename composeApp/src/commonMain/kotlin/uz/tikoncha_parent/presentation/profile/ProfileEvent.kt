package uz.tikoncha_parent.presentation.profile

import uz.tikoncha_parent.domain.model.UploadPart

sealed interface ProfileEvent {
    data class OnChangeProfilePhotoClicked(val image: String?): ProfileEvent
    data class OnAvatarPhotoSelected(val part: UploadPart): ProfileEvent
    object LoadAvatarFromServer: ProfileEvent
}