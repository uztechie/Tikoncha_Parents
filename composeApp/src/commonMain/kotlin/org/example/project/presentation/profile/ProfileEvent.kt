package org.example.project.presentation.profile

sealed interface ProfileEvent {
    data class OnChangeProfilePhotoClicked(val image: String?): ProfileEvent
}