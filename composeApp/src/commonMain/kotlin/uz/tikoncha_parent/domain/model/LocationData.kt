package uz.tikoncha_parent.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LocationData(val lat: Double, val lng: Double)