package org.example.project.domain.repository

import org.example.project.data.remote.model.AddChildRequest
import org.example.project.data.remote.model.AddChildResponse
import org.example.project.data.remote.model.AppUsageResponse
import org.example.project.data.remote.model.ChildrenResponse

interface ChildRepository {

    suspend fun addChild(request: AddChildRequest): AddChildResponse
    suspend fun children(): ChildrenResponse

    suspend fun appUsages(params: Map<String, Any>): AppUsageResponse
}