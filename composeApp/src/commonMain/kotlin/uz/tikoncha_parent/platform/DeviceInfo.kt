package uz.tikoncha_parent.platform

data class DeviceInfo(
    val manufacturer: String,
    val modelName: String,
    val os: String,
    val osVersion: String,
)

expect fun getDeviceInfo(): DeviceInfo