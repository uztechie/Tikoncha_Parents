package org.example.project.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppUsageResponse(
    val success: Boolean,
    val data: AppUsageDataDto?,
    val error: String? = null,
    val code:Int
)
@Serializable
data class AppUsageDataDto(
    val items: List<AppUsageItemDto>
)

@Serializable
data class AppUsageItemDto(
    @SerialName("package") val packageName: String,
    val name: String,
    val usage: Map<String, Map<String, Long>>
)
