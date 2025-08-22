package org.example.project.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AddChildRequest(
    val phone_number: String
)
