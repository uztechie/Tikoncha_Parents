package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.data.remote.model.NewsResponse

interface NewsRepository {
    suspend fun fetchNews(): NewsResponse
    suspend fun markNewsRead(id: Long)
    suspend fun markAllNewsRead()
}