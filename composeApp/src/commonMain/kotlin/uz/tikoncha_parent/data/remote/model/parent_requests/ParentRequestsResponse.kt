package uz.tikoncha_parent.data.remote.model.parent_requests

import kotlinx.serialization.Serializable

@Serializable
data class ParentRequestsResponse(
    val success: Boolean,
    val data: ParentRequestsData? = null,
    val error: String? = null,
    val code: Int? = null,
)

@Serializable
data class ParentRequestsData(
    val items: List<ParentRequestsResponseDto>
)
