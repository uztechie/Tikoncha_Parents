package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AppsResponse(
    val success: Boolean,
    val data: AppsData?,
    val error: String?,
    val code:Int
)

@Serializable
data class AppsData(
    val items: List<AppDto>
)

@Serializable
data class AppDto(
    val `package`: String,
    val name: String? = null,
    val category: String? = null,
    val logo: String? = null,
    val order: Int
)