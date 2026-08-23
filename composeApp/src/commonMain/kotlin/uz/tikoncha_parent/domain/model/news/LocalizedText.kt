package uz.tikoncha_parent.domain.model.news

data class LocalizedText(
    val uz: String?,
    val ru: String?,
)

data class News(
    val id: Long,
    val authorId: String,
    val title: LocalizedText,
    val message: LocalizedText,
    val published: Boolean,
    val isRead: Boolean,
    val createdAt: Long,
    val modifiedAt: Long,
)