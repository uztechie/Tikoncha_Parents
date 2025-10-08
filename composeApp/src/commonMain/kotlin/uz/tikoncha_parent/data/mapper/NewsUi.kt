package uz.tikoncha_parent.data.mapper

import uz.tikoncha_parent.common.DateTimeUtil
import uz.tikoncha_parent.data.remote.model.NewLocalizedText
import uz.tikoncha_parent.data.remote.model.NewsDto


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

fun NewsDto.toUi(lang: String = "uz"): NewsUi {
    val chosenTitle = title.pick(lang)
    val chosenMsg = message.pick(lang)

    val createdAt = DateTimeUtil.toMillisUtc(created_at)
    val modifiedAt = DateTimeUtil.toMillisUtc(modified_at)


    return NewsUi(
        id = id,
        authorId = author_id,
        title = chosenTitle,
        message = chosenMsg,
        published = published,
        createdAt = createdAt,
        modifiedAt = modifiedAt,
        isRead = is_read
    )
}
private fun NewLocalizedText.pick(lang: String): String {
    // Default: uz -> ru -> empty
    return when (lang.lowercase()) {
        "uz" -> uz ?: ru ?: ""
        "ru" -> ru ?: uz ?: ""
        else -> uz ?: ru ?: ""
    }
}