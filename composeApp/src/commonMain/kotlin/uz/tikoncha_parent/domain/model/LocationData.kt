package uz.tikoncha_parent.domain.model

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import kotlinx.serialization.Serializable

@Serializable
data class LocationData(val lat: Double, val lng: Double): JavaSerializable