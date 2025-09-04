package uz.tikoncha_parent.presentation.map

import dev.icerock.moko.geo.LatLng
import uz.tikoncha_parent.data.remote.model.ChildrenLocationItemDto

data class LocationState(
    val showGpsDialog: Boolean? = null,
    val locationData: LatLng? = null,
    val childrenLocationList: List<ChildrenLocationItemDto> = emptyList(),
    val childrenLocationError: String = "",
    val childrenLocationLoading: Boolean = false,
)