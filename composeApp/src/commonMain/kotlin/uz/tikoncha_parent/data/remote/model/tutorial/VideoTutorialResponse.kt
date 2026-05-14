package uz.tikoncha_parent.data.remote.model.tutorial

import kotlinx.serialization.Serializable
import uz.tikoncha_parent.data.remote.model.NewsData

@Serializable
data class VideoTutorialResponse(
    val success: Boolean,
    val data: VideoTutorialData? = null,
    val error: String? = null,
    val code: Int? = null
)

@Serializable
data class VideoTutorialData(
    val tikoncha_tutorial_url: String?,
    val policy_tutorial_url: String?,
    val bind_child_tutorial_url: String?

    )
