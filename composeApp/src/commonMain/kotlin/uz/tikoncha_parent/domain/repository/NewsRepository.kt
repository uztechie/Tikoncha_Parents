package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.model.news.News

interface NewsRepository {
    suspend fun fetchNews(): Outcome<List<News>>
    suspend fun markNewsRead(id: Long)
    suspend fun markAllNewsRead()
}