package uz.tikoncha_parent.presentation.video_tutorial

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.repository.TutorialRepository

class VideoTutorialScreenModel(
    private val tutorialRepository: TutorialRepository
) : ScreenModel {

    private val _state = MutableStateFlow(VideoTutorialState())
    val state = _state.asStateFlow()

    fun loadVideoTutorial(type: TutorialType) {
        screenModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            when (val res = tutorialRepository.videoTutorials()) {
                is Outcome.Failure -> _state.update {
                    it.copy(isLoading = false, error = res)
                }

                is Outcome.Success -> {
                    val url = when (type) {
                        TutorialType.TIKONCHA -> {
                            AppSettings.showTikonchaTutorial = false
                            res.data.tikoncha
                        }
                        TutorialType.POLICY -> {
                            AppSettings.showPolicyTutorial = false
                            res.data.policy
                        }
                        TutorialType.BIND_CHILD -> {
                            AppSettings.showBindChildTutorial = false
                            res.data.bindChild
                        }
                    }
                    _state.update {
                        it.copy(isLoading = false, videoUrl = url ?: "")
                    }
                }
            }
        }
    }
}