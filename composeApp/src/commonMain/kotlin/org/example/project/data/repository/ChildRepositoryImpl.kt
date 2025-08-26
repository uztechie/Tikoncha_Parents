package org.example.project.data.repository

import org.example.project.data.remote.ChildApiService
import org.example.project.data.remote.model.AddChildRequest
import org.example.project.data.remote.model.AddChildResponse
import org.example.project.data.remote.model.AppUsageResponse
import org.example.project.data.remote.model.ChildrenResponse
import org.example.project.domain.repository.ChildRepository

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
}