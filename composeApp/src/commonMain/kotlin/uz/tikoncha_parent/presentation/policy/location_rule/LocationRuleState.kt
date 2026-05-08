package uz.tikoncha_parent.presentation.policy.location_rule

import uz.tikoncha_parent.domain.model.GeoType
import uz.tikoncha_parent.domain.model.LocationData
import uz.tikoncha_parent.presentation.map.LatLng

data class LocationRuleState(
    val isLoading: Boolean = false,
    val canUpdate: Boolean = true,
    val geoType: GeoType = GeoType.CIRCLE,
    val center: LatLng? = null,
    val radiusMeters: Int = 200,
    val polygon: List<LocationData> = emptyList(),
    val reverse: Boolean = false,
    val userLocation: LatLng? = null,
    val initialCameraSet: Boolean = false,
    val errorMessage: String? = null,
    val isLocating: Boolean = false,
    val permissionAsked: Boolean = false,
    val permissionDenied: Boolean = false,
    val permissionDeniedAlways: Boolean = false,
    val showGpsDialog: Boolean = false,
    val autoFocusTarget: LatLng? = null,
    val autoFitBoundsTarget: List<LatLng>? = null,
) {
    val canSave: Boolean
        get() = canUpdate && when (geoType) {
            GeoType.CIRCLE -> center != null && radiusMeters in radiusMin..radiusMax
            GeoType.POLYGON -> polygon.size >= 3
        }

    val showBottomPanel: Boolean get() = canUpdate
    val showCenterPin: Boolean get() = canUpdate && geoType == GeoType.CIRCLE
    val showRadiusSlider: Boolean get() = geoType == GeoType.CIRCLE

    val radiusMin: Int = 50
    val radiusMax: Int = 5000
}