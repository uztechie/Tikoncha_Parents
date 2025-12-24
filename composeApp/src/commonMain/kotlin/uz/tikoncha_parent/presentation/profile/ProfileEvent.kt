package uz.tikoncha_parent.presentation.profile

import androidx.compose.ui.graphics.ImageBitmap
import uz.tikoncha_parent.domain.model.UploadPart

sealed interface ProfileEvent {
    data class OnChangeProfilePhotoClicked(val image: String?): ProfileEvent
    data class OnAvatarPhotoSelected(val part: UploadPart): ProfileEvent
    data class OnAvatarPreviewSelected(val bitmap: ImageBitmap?) : ProfileEvent
    object LoadAvatarFromServer: ProfileEvent
}