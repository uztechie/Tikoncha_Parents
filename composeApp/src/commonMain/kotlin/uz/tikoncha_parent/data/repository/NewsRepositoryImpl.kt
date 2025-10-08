package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.remote.NewApiService
import uz.tikoncha_parent.data.remote.model.NewsResponse
import uz.tikoncha_parent.domain.repository.NewsRepository

class NewsRepositoryImpl (
    private val api: NewApiService
): NewsRepository {
    override suspend fun fetchNews(): NewsResponse {
        return api.fetchNews()
    }

    override suspend fun markNewsRead(id: Long) {
        return api.markNewsRead(id)
    }

    override suspend fun markAllNewsRead() {
        return api.markAllNewsRead()
    }
}