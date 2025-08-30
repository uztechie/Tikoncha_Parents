package org.example.project.presentation.map

import dev.icerock.moko.geo.LatLng
import org.example.project.data.remote.model.ChildrenLocationItemDto

data class LocationState(
    val showGpsDialog: Boolean? = null,
    val locationData: LatLng? = null,
    val childrenLocationList: List<ChildrenLocationItemDto> = emptyList(),
    val childrenLocationError: String = "",
    val childrenLocationLoading: Boolean = false,
)