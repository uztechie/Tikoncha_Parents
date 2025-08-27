package org.example.project.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.toByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.data.local.AppSettings
import org.example.project.data.mapper.toUserInfo
import org.example.project.domain.model.Resource
import org.example.project.domain.model.UploadPart
import org.example.project.domain.use_case.ChildrenUseCase
import org.example.project.domain.use_case.LoadAvatarFromServerUseCase
import org.example.project.domain.use_case.UploadAvatarToServerUseCase
import org.example.project.domain.use_case.UserInfoUseCase
import org.example.project.platform.Logger
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class ProfileViewModel(
    private val userInfoUseCase: UserInfoUseCase,
    private val childrenUseCase: ChildrenUseCase,
    private val loadAvatarFromServerUseCase: LoadAvatarFromServerUseCase,
    private val uploadAvatarToServerUseCase: UploadAvatarToServerUseCase,
    private val httpClient: HttpClient
): ViewModel() {

    val BASE_URL = "https://api.tikoncha.uz"
    var userInfoJob: Job? = null
    var childrenJob: Job? = null
    var avatarJob: Job? = null

    private val _state = MutableStateFlow(ProfileState())

    val state = _state.asStateFlow()

    init {
        _state.update {
            it.copy(
                userInfo = AppSettings.userInfo,
                children = AppSettings.children,
                profileImageUrl = AppSettings.profileImageUrl
            )
        }
        userInfoJob()
        getChildren()
        getAvatar()
    }

    fun onEvent(event: ProfileEvent){
        when(event){
            is ProfileEvent.OnAvatarPhotoSelected -> {
                uploadAvatar(event.part)
            }

            ProfileEvent.LoadAvatarFromServer -> {
                getAvatar()
            }
        }
    }
    private fun uploadAvatar(part: UploadPart){
        avatarJob?.cancel()

        avatarJob = viewModelScope.launch {
            when (val res = uploadAvatarToServerUseCase(part)) {
                is Resource.Success -> {
                    val url = "${BASE_URL}${res.data.avatar_url}"
                    AppSettings.profileImageUrl = url
                    _state.update { it.copy(profileImageUrl = url) }
                }
                else -> Unit
            }
        }
    }
    private fun getAvatar(){
        avatarJob?.cancel()

        avatarJob = viewModelScope.launch {
            when (val res = loadAvatarFromServerUseCase()) {
                is Resource.Success -> {
                    val url = "${BASE_URL}${res.data.avatar_url}"
                    AppSettings.profileImageUrl = url
                    _state.update { it.copy(profileImageUrl = url) }
                }
                else -> Unit
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