package uz.tikoncha_parent.presentation.video_tutorial

data class VideoTutorialState(
    val videoUrl: String = "",
    val isLoading: Boolean = false,
    val error: String = ""
)
