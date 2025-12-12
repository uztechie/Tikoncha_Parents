package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.data.remote.model.parent_requests.UpdateParentRequestResponse
import uz.tikoncha_parent.data.remote.model.parent_requests.ParentRequestsResponse

interface ParentRequestsRepository {
    suspend fun getLogout(): ParentRequestsResponse

    suspend fun updateRequest(requestId: String, status: String): UpdateParentRequestResponse
}