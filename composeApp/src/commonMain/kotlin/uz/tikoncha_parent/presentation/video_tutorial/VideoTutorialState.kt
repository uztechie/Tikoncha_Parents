package uz.tikoncha_parent.presentation.video_tutorial

import uz.tikoncha_parent.domain.model.app_error.Outcome

data class VideoTutorialState(
    val videoUrl: String = "",
    val isLoading: Boolean = false,
    val error: Outcome.Failure? = null,
)