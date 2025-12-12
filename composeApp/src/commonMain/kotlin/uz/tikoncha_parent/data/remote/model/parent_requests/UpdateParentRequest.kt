package uz.tikoncha_parent.data.remote.model.parent_requests

import kotlinx.serialization.Serializable

@Serializable
data class UpdateParentRequest(
    val status: String
)

@Serializable
data class UpdateParentRequestResponse(
    val success: Boolean,
    val data: ParentRequestsResponseDto? = null,
    val error: String? = null,
    val code: Int? = null,
)
