package uz.tikoncha_parent.presentation.video_tutorial

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.tutorial.VideoTutorialUseCase
import uz.tikoncha_parent.platform.Logger

class VideoTutorialScreenModel(
    private val videoTutorialUseCase: VideoTutorialUseCase
): ScreenModel {
    private val _state = MutableStateFlow(VideoTutorialState())
    val state = _state.asStateFlow()


    fun loadVideoTutorial(type: TutorialType){
        screenModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = videoTutorialUseCase.invoke()
            when(result){
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message?:""
                        )
                    }
                }
                is Resource.Success -> {
                    val url = when(type){
                        TutorialType.TIKONCHA -> {
                            AppSettings.showTikonchaTutorial = false
                            result.data.tikoncha_tutorial_url
                        }
                        TutorialType.POLICY -> {
                            AppSettings.showPolicyTutorial = false
                            result.data.policy_tutorial_url
                        }
                        TutorialType.BIND_CHILD -> {
                            AppSettings.showBindChildTutorial = false
                            result.data.bind_child_tutorial_url
                        }
                    }
                    _state.update {
                        it.copy(
                            isLoading = false,
                            videoUrl = url?:""
                        )
                    }
                }
            }
        }
    }


}