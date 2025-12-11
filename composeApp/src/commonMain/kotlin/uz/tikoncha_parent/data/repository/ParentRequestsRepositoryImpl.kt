package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.remote.ParentRequestsApiService
import uz.tikoncha_parent.data.remote.model.parent_requests.ParentRequestsResponse
import uz.tikoncha_parent.domain.repository.ParentRequestsRepository

class ParentRequestsRepositoryImpl(
    private val api: ParentRequestsApiService
): ParentRequestsRepository {
    override suspend fun getLogout(): ParentRequestsResponse {
        return api.parentRequests()
    }
}