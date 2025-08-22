package org.example.project.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.data.local.AppSettings
import org.example.project.data.mapper.toUserInfo
import org.example.project.data.remote.model.RegisterUserRequest
import org.example.project.domain.model.GenderType
import org.example.project.domain.model.Resource
import org.example.project.domain.use_case.ChildrenUseCase
import org.example.project.domain.use_case.UserInfoUseCase

class ProfileViewModel(
    private val userInfoUseCase: UserInfoUseCase,
    private val childrenUseCase: ChildrenUseCase
): ViewModel() {

    var userInfoJob: Job? = null
    var childrenJob: Job? = null

    private val _state = MutableStateFlow(ProfileState())

    val state = _state.asStateFlow()

    init {
        _state.update {
            it.copy(
                userInfo = AppSettings.userInfo,
                children = AppSettings.children
            )
        }
        userInfoJob()
        getChildren()
    }

    fun onEvent(event: ProfileEvent){
        when(event){
            is ProfileEvent.OnChangeProfilePhotoClicked -> {

            }
        }
    }


    private fun userInfoJob(){
        userInfoJob?.cancel()
        userInfoJob = viewModelScope.launch {
            val result = userInfoUseCase()
            when(result){
                is Resource.Loading -> {}
                is Resource.Error -> {

                }
                is Resource.Success -> {
                    AppSettings.userInfo = result.data.toUserInfo()
                    _state.update {
                        it.copy(
                            children = AppSettings.children
                        )
                    }
                }
            }
        }
    }

    private fun getChildren(){
        childrenJob?.cancel()
        childrenJob = viewModelScope.launch {
            val result = childrenUseCase()
            when(result){
                is Resource.Loading -> {}
                is Resource.Error -> {

                }
                is Resource.Success -> {
                    AppSettings.children = result.data.map { it.toUserInfo() }
                    _state.update {
                        it.copy(
                            children = AppSettings.children
                        )
                    }
                }
            }
        }
    }
}