package uz.tikoncha_parent.data.mapper

import uz.tikoncha_parent.common.DateTimeUtil
import uz.tikoncha_parent.data.remote.model.NewsDto
import uz.tikoncha_parent.domain.model.news.LocalizedText
import uz.tikoncha_parent.domain.model.news.News

fun NewsDto.toNews(): News = News(
    id = id,
    authorId = author_id,
    title = LocalizedText(title.uz, title.ru),
    message = LocalizedText(message.uz, message.ru),
    published = published,
    isRead = is_read,
    createdAt = DateTimeUtil.toMillisUtc(created_at),
    modifiedAt = DateTimeUtil.toMillisUtc(modified_at),
)