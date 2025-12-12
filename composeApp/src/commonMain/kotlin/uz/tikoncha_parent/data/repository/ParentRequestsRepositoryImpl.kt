package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.remote.ParentRequestsApiService
import uz.tikoncha_parent.data.remote.model.parent_requests.UpdateParentRequestResponse
import uz.tikoncha_parent.data.remote.model.parent_requests.ParentRequestsResponse
import uz.tikoncha_parent.domain.repository.ParentRequestsRepository
import uz.tikoncha_parent.platform.Logger

class ParentRequestsRepositoryImpl(
    private val api: ParentRequestsApiService
): ParentRequestsRepository {
    override suspend fun getLogout(): ParentRequestsResponse {
        return api.parentRequests()
    }

    override suspend fun updateRequest(
        requestId: String,
        status: String
    ): UpdateParentRequestResponse {
        Logger.d("ParentRequestsRepositoryImpl", "updateRequest: $requestId $status")
        return api.updateParentRequestStatus(requestId, status)
    }
}