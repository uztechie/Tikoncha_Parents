package uz.tikoncha_parent.data.mapper

import dev.icerock.moko.geo.Location
import uz.tikoncha_parent.domain.model.LocationData

fun Location.toLocationData(): LocationData =
    LocationData(lat = coordinates.latitude, lng = coordinates.longitude)