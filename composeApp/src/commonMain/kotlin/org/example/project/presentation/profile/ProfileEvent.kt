package org.example.project.presentation.profile

sealed interface ProfileEvent {
    data class OnChangeProfileImageClicked(val image: String?): ProfileEvent
}