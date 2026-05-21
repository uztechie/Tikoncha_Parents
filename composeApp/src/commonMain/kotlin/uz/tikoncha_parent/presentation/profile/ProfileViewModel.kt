package uz.tikoncha_parent.presentation.profile

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.mapper.toUserInfo
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.model.UploadPart
import uz.tikoncha_parent.domain.use_case.ChildrenUseCase
import uz.tikoncha_parent.domain.use_case.DeleteAvatarFromServerUseCase
import uz.tikoncha_parent.domain.use_case.LoadAvatarFromServerUseCase
import uz.tikoncha_parent.domain.use_case.UploadAvatarToServerUseCase
import uz.tikoncha_parent.domain.use_case.UserInfoUseCase
import uz.tikoncha_parent.domain.use_case.device.LogoutUseCase
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class ProfileViewModel(
    private val userInfoUseCase: UserInfoUseCase,
    private val childrenUseCase: ChildrenUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val loadAvatarFromServerUseCase: LoadAvatarFromServerUseCase,
    private val uploadAvatarToServerUseCase: UploadAvatarToServerUseCase,
    private val deleteAvatarFromServerUseCase: DeleteAvatarFromServerUseCase
): ScreenModel {
    var userInfoJob: Job? = null
    var childrenJob: Job? = null
    var avatarJob: Job? = null
    var deleteAvatarJob: Job? = null

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {
        Logger.d("ProfileViewModel"," AppSettings.userInfo=${AppSettings.userInfo}")
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

            is ProfileEvent.OnChangeProfilePhotoClicked -> {

            }

            is ProfileEvent.OnAvatarPreviewSelected -> {
                _state.update {
                    it.copy(
                        localAvatar = event.bitmap
                    )
                }
            }

            ProfileEvent.Refresh -> {
                userInfoJob()
                getChildren()
                getAvatar()
            }

            ProfileEvent.RequestLogout -> {
                logoutRequest()
            }

            ProfileEvent.Clear -> {
                _state.update {
                    it.copy(
                        logoutState = ResponseState.Idle
                    )
                }
            }

            ProfileEvent.ClearDeleteAvatarState -> {
                _state.update {
                    it.copy(
                        deleteAvatarState = ResponseState.Idle
                    )
                }
            }
            ProfileEvent.RequestDeleteAvatar -> {
                deleteAvatar()
            }
        }
    }


    private fun deleteAvatar(){
        deleteAvatarJob?.cancel()
        _state.update {
            it.copy(
                deleteAvatarState = ResponseState.Loading
            )
        }

        deleteAvatarJob = screenModelScope.launch {
            val result = deleteAvatarFromServerUseCase()
            when(result){
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            deleteAvatarState = ResponseState.Error(
                                message = result.message,
                                res = result.resId
                            )
                        )
                    }
                }
                is Resource.Success -> {
                    AppSettings.profileImageUrl = ""
                    _state.update {
                        it.copy(
                            profileImageUrl = "",
                            localAvatar = null,
                            deleteAvatarState = ResponseState.Success()
                        )
                    }
                }
            }
        }
    }
    private fun uploadAvatar(part: UploadPart){
        avatarJob?.cancel()

        avatarJob = screenModelScope.launch {
            when (val res = uploadAvatarToServerUseCase(part)) {
                is Resource.Success -> {
                    val url = res.data.avatar_url ?:""
                    AppSettings.profileImageUrl = url
                    _state.update { it.copy(profileImageUrl = url) }
                }
                else -> Unit
            }
        }
    }
    private fun getAvatar(){
        avatarJob?.cancel()

        avatarJob = screenModelScope.launch {
            when (val res = loadAvatarFromServerUseCase()) {
                is Resource.Success -> {
                    val url = res.data.avatar_url?:""
                    AppSettings.profileImageUrl = url
                    _state.update { it.copy(profileImageUrl = url) }
                }
                else -> Unit
            }
        }
    }

    private fun logoutRequest(){
        _state.update {
            it.copy(
                logoutState = ResponseState.Loading
            )
        }
        screenModelScope.launch {
            val result = logoutUseCase.invoke(AppSettings.fcmToken)
            when(result){
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            logoutState = ResponseState.Error(
                                message = result.message,
                                res = result.resId
                            )
                        )
                    }
                }
                is Resource.Success -> {
                    AppSettings.clearSession()
                    _state.update {
                        it.copy(
                            logoutState = ResponseState.Success()
                        )
                    }
                }

            }
        }
    }

    private fun userInfoJob(){
        userInfoJob?.cancel()
        userInfoJob = screenModelScope.launch {
            val result = userInfoUseCase()
            when(result){
                is Resource.Loading -> {}
                is Resource.Error -> {

                }
                is Resource.Success -> {
                    AppSettings.userInfo = result.data.toUserInfo()
                    _state.update {
                        it.copy(
                            children = AppSettings.children,
                            userInfo = result.data.toUserInfo()
                        )
                    }
                }
            }
        }
    }

    fun getChildren(){
        childrenJob?.cancel()
        childrenJob = screenModelScope.launch {
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