package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val success: Boolean,
    val data: NewsData? = null,
    val error: String? = null,
    val code: Int? = null
)

@Serializable
data class NewsData(
    val items: List<NewsDto>
)

@Serializable
data class NewLocalizedText(
    val ru: String? = null,
    val uz: String? = null
)

@Serializable
data class NewsDto(
    val id: Long,
    val author_id: String,
    val title: NewLocalizedText = NewLocalizedText(),
    val message: NewLocalizedText = NewLocalizedText(),
    val published: Boolean = false,
    val is_read: Boolean = false,
    val created_at: String = "",
    val modified_at: String = ""
)
