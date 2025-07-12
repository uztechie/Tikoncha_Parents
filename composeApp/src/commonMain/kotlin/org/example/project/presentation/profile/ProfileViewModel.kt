package org.example.project.presentation.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel: ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    fun onEvent(event: ProfileEvent){
        when(event){
            is ProfileEvent.OnChangeProfileImageClicked -> {

            }
        }
    }

}