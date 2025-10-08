package uz.tikoncha_parent.domain.use_case

import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.remote.model.NewsData
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.NewsRepository


class NewsUseCase (
    private val repository: NewsRepository,
) {

    suspend operator fun invoke(): Resource<List<NewsData>> {
        return try {
            val response = repository.fetchNews()
            if (response.success && response.data != null){
                Resource.Success(listOf(response.data))
            } else {
                Resource.Error((Res.string.server_connection_error))
            }
        } catch (e: Exception){
            e.printStackTrace()
            Resource.Error((Res.string.server_connection_error))
        }
    }
}