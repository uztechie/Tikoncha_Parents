package org.example.project.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AddChildResponse(
    val success: Boolean,
    val data: AddChildData?,
    val error: String? = null,
    val code:Int
)

@Serializable
data class AddChildData(
    val code: String
)
