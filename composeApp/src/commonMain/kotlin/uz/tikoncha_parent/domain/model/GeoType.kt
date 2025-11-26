package uz.tikoncha_parent.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class GeoType{
    POLYGON, CIRCLE
}