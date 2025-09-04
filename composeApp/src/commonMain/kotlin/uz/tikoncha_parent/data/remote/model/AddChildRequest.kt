package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AddChildRequest(
    val phone_number: String
)
