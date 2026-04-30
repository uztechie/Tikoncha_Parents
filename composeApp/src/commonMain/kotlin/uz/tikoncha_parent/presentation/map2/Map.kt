package uz.tikoncha_parent.presentation.map2

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier

expect object MapKitInitializer {
    fun initialize(apiKey: String)
}

@Stable
expect class MapController() {
    fun moveTo(position: CameraPosition, animated: Boolean = true)
    fun fitBounds(points: List<LatLng>, paddingDp: Float = 48f, animated: Boolean = true)
    fun moveToUserLocation(animated: Boolean = true)
    fun tryMoveToUserLocation(animated: Boolean = true): Boolean   // ← QO'SHING
}

@Composable
expect fun rememberMapController(): MapController

@Composable
expect fun YandexMap(
    controller: MapController,
    initialCamera: CameraPosition,
    markers: List<MapMarker>,
    circles: List<MapCircle> = emptyList(),
    onMarkerClick: (String) -> Unit = {},
    onMapTap: (LatLng) -> Unit = {},
    showUserLocation: Boolean = false,                                  // YOQISH/O'CHIRISH
    userLocationIcon: NativeMarkerIcon? = null,                          // ← YANGI: custom icon
    onUserLocationChanged: ((LatLng) -> Unit)? = null,                   // ← YANGI: callback
    isDark: Boolean = false,
    modifier: Modifier = Modifier
)

internal fun MarkerStyle.cacheKey(): String =
    "${backgroundColor}_${isSelected}_${avatarUrl ?: text}_${showText}"