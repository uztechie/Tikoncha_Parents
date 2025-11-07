package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class LocationRuleDto(
    val polygon: List<List<Double>>,
    val circle_radius: Int,
    val center_latitude: Double,
    val center_longitude: Double,
    val location_include: Boolean,
    val type: String,
)
