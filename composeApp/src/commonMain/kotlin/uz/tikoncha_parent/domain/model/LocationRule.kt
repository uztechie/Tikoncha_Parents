package uz.tikoncha_parent.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class LocationRule(
    val geoType: GeoType,
    val centerLat:Double? = null,
    val centerLng:Double? = null,
    val radiusMeters:Int? = null,
    val polygon: List<LocationData>? = null,
    val reverse: Boolean
)