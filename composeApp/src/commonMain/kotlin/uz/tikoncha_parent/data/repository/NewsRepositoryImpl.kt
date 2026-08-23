package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.mapper.toNews
import uz.tikoncha_parent.data.remote.NewApiService
import uz.tikoncha_parent.data.remote.app_error.ApiErrorMapper
import uz.tikoncha_parent.data.remote.model.NewsResponse
import uz.tikoncha_parent.domain.model.app_error.ErrorCause
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.model.news.News
import uz.tikoncha_parent.domain.repository.NewsRepository

class NewsRepositoryImpl (
    private val api: NewApiService
): NewsRepository {
    override suspend fun fetchNews(): Outcome<List<News>> =
        apiCall(TAG) {
            val r = api.fetchNews()
            val body = r.data
            when {
                r.success && body != null -> Outcome.Success(body.items.map { it.toNews() })
                r.success -> Outcome.Failure(ErrorCause.InvalidResponse)
                else -> Outcome.Failure(ApiErrorMapper.fromCode(r.code), r.error)
            }
        }

    override suspend fun markNewsRead(id: Long) {
        return api.markNewsRead(id)
    }

    override suspend fun markAllNewsRead() {
        return api.markAllNewsRead()
    }

    private companion object { const val TAG = "NewsRepository" }
}