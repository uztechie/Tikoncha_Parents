package org.example.project.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChildrenLocationResponse(
    val success: Boolean,
    val data: ChildrenLocationData?,
    val error: String? = null,
    val code:Int
)
@Serializable
data class ChildrenLocationData(
    val items: List<ChildrenLocationItemDto>
)

@Serializable
data class ChildrenLocationItemDto(
    val child_user_id: String?,
    val first_name: String?,
    val last_name: String?,
    val lat: Double?,
    val lng: Double?,
    val updated_at: String?,
)
