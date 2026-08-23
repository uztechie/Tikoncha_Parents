package uz.tikoncha_parent.data.mapper

import uz.tikoncha_parent.domain.model.news.LocalizedText
import uz.tikoncha_parent.domain.model.news.News


data class NewsUi(
    val id: Long,
    val authorId: String,
    val title: String,
    val message: String,
    val published: Boolean,
    val isRead: Boolean,
    val createdAt: Long,
    val modifiedAt: Long
)

fun News.toUi(lang: String = "uz"): NewsUi {
    return NewsUi(
        id = id,
        authorId = authorId,
        title = title.pick(lang),
        message = message.pick(lang),
        published = published,
        createdAt = createdAt,
        modifiedAt = modifiedAt,
        isRead = isRead,
    )
}

private fun LocalizedText.pick(lang: String): String = when (lang.lowercase()) {
    "ru" -> ru ?: uz ?: ""
    else -> uz ?: ru ?: ""
}