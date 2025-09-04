package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.data.remote.model.AddChildRequest
import uz.tikoncha_parent.data.remote.model.AddChildResponse
import uz.tikoncha_parent.data.remote.model.AppUsageResponse
import uz.tikoncha_parent.data.remote.model.ChildrenLocationResponse
import uz.tikoncha_parent.data.remote.model.ChildrenResponse

interface ChildRepository {

    suspend fun addChild(request: AddChildRequest): AddChildResponse
    suspend fun children(): ChildrenResponse

    suspend fun appUsages(params: Map<String, Any>): AppUsageResponse

    suspend fun childrenLocation(): ChildrenLocationResponse
}