package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class DeviceRegisterRequest(
    val manufacturer: String,
    val model_name: String,
    val os: String,
    val os_version: String,
    val fcm_token: String
)