package org.example.project.presentation.profile.personal_information

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PersonalInformationViewModel: ViewModel() {

    private val _state = MutableStateFlow(PersonalInformationState())

    val state = _state.asStateFlow()

    fun onEvent(event: PersonalInformationEvent){
        when(event){
            is PersonalInformationEvent.OnChangeProfilePhotoClicked -> {

            }
        }
    }
}