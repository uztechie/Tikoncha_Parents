package uz.tikoncha_parent.presentation.profile

import androidx.compose.ui.graphics.ImageBitmap
import uz.tikoncha_parent.domain.model.UploadPart
import uz.tikoncha_parent.domain.model.UserInfo

sealed interface ProfileEvent {
    data class OnChangeProfilePhotoClicked(val image: String?): ProfileEvent
    data class OnAvatarPhotoSelected(val part: UploadPart): ProfileEvent
    data class OnAvatarPreviewSelected(val bitmap: ImageBitmap?) : ProfileEvent
    object LoadAvatarFromServer: ProfileEvent
    data object Refresh : ProfileEvent
    data object RequestLogout : ProfileEvent
    data object Clear : ProfileEvent

    data object RequestDeleteAvatar : ProfileEvent
    data object ClearDeleteAvatarState : ProfileEvent

    data class OnUnlinkClicked(val child: UserInfo): ProfileEvent
    data object DismissUnlinkDialog: ProfileEvent
    data object ConfirmUnlink: ProfileEvent
    data object ClearUnlinkState: ProfileEvent
}