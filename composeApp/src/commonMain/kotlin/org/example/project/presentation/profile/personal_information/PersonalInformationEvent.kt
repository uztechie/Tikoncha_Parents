package org.example.project.presentation.profile.personal_information

sealed interface PersonalInformationEvent {
    data class OnChangeProfilePhotoClicked(val image: String?): PersonalInformationEvent
}