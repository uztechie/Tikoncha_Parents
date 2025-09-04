package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.remote.ChildApiService
import uz.tikoncha_parent.data.remote.model.AddChildRequest
import uz.tikoncha_parent.data.remote.model.AddChildResponse
import uz.tikoncha_parent.data.remote.model.AppUsageResponse
import uz.tikoncha_parent.data.remote.model.ChildrenLocationResponse
import uz.tikoncha_parent.data.remote.model.ChildrenResponse
import uz.tikoncha_parent.domain.repository.ChildRepository

class ChildRepositoryImpl(
    private val api: ChildApiService
): ChildRepository {
    override suspend fun addChild(request: AddChildRequest): AddChildResponse {
        return api.addChild(request)
    }

    override suspend fun children(): ChildrenResponse {
        return api.children()
    }

    override suspend fun appUsages(params: Map<String, Any>): AppUsageResponse {
        return api.appUsages(params)
    }

    override suspend fun childrenLocation(): ChildrenLocationResponse {
        return api.childrenLocation()
    }
}