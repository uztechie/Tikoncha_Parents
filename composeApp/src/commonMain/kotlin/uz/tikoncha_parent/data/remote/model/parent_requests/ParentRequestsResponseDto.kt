package uz.tikoncha_parent.data.remote.model.parent_requests

import kotlinx.serialization.Serializable

@Serializable
data class ParentRequestsResponseDto(
    val id: String,
    val user_id: String,
    val action: String,
    val status: String,
    val created_at: String,
    val modified_at: String,
    val packages: List<String>? = null,
    val first_name: String? = null,
    val last_name: String? = null,
    val phone_number: String? = null,
    val school_name: String? = null,
    val class_name: String? = null
)