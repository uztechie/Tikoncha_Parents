package org.example.project.presentation.map

import dev.icerock.moko.geo.LatLng

data class LocationState(
    val showGpsDialog: Boolean? = null,
    val locationData: LatLng? = null,
)