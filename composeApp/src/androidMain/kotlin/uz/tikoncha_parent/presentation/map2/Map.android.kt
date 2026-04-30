package uz.tikoncha_parent.presentation.map2

import android.graphics.PointF
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraPosition as YCameraPosition
import com.yandex.mapkit.map.CircleMapObject
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map as YMap
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.mapkit.geometry.Circle as YCircle
import kotlinx.coroutines.launch

// ============================================================
// MAP KIT INITIALIZER
// ============================================================

actual object MapKitInitializer {
    @Volatile
    private var initialized = false

    actual fun initialize(apiKey: String) {
        if (initialized) return
        MapKitFactory.setApiKey(apiKey)
        initialized = true
    }
}

// ============================================================
// MAP CONTROLLER
// ============================================================

@Stable
actual class MapController actual constructor() {
    internal var mapView: MapView? = null
    internal var userLocationLayer: UserLocationLayer? = null

    actual fun moveTo(position: CameraPosition, animated: Boolean) {
        val map = mapView?.mapWindow?.map ?: return
        val target = YCameraPosition(
            Point(position.target.lat, position.target.lon),
            position.zoom, position.azimuth, position.tilt
        )
        if (animated) map.move(target, Animation(Animation.Type.SMOOTH, 0.4f), null)
        else map.move(target)
    }

    actual fun fitBounds(points: List<LatLng>, paddingDp: Float, animated: Boolean) {
        val map = mapView?.mapWindow?.map ?: return
        if (points.isEmpty()) return
        if (points.size == 1) {
            moveTo(CameraPosition(points.first(), 16f), animated)
            return
        }
        val pts = points.map { Point(it.lat, it.lon) }
        val box = BoundingBox(
            Point(pts.minOf { it.latitude }, pts.minOf { it.longitude }),
            Point(pts.maxOf { it.latitude }, pts.maxOf { it.longitude })
        )
        val cam = map.cameraPosition(Geometry.fromBoundingBox(box))
        val padded = YCameraPosition(cam.target, cam.zoom - 0.4f, cam.azimuth, cam.tilt)
        if (animated) map.move(padded, Animation(Animation.Type.SMOOTH, 0.4f), null)
        else map.move(padded)
    }

    actual fun moveToUserLocation(animated: Boolean) {
        tryMoveToUserLocation(animated)
    }

    actual fun tryMoveToUserLocation(animated: Boolean): Boolean {
        val map = mapView?.mapWindow?.map ?: return false
        val pos = userLocationLayer?.cameraPosition() ?: return false
        val target = YCameraPosition(pos.target, 16f, 0f, 0f)
        if (animated) map.move(target, Animation(Animation.Type.SMOOTH, 0.5f), null)
        else map.move(target)
        return true
    }
}

@Composable
actual fun rememberMapController(): MapController = remember { MapController() }

// ============================================================
// USER LOCATION HELPER
// ============================================================

private fun applyUserLocationIcon(
    view: UserLocationView,
    icon: NativeMarkerIcon?
) {
    if (icon == null) return
    val style = IconStyle().apply {
        anchor = PointF(MarkerDimensions.ANCHOR_X, MarkerDimensions.ANCHOR_Y)
    }
    view.pin.setIcon(icon.provider, style)
    view.arrow.setIcon(icon.provider, style)
    view.accuracyCircle.fillColor = android.graphics.Color.TRANSPARENT
}

// ============================================================
// COMPOSABLE — YandexMap
// ============================================================

@Composable
actual fun YandexMap(
    controller: MapController,
    initialCamera: CameraPosition,
    markers: List<MapMarker>,
    circles: List<MapCircle>,
    onMarkerClick: (String) -> Unit,
    onMapTap: (LatLng) -> Unit,
    showUserLocation: Boolean,
    userLocationIcon: NativeMarkerIcon?,
    onUserLocationChanged: ((LatLng) -> Unit)?,
    isDark: Boolean,
    modifier: Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()
    val scope = rememberCoroutineScope()

    val onMarkerClickState = rememberUpdatedState(onMarkerClick)
    val onMapTapState = rememberUpdatedState(onMapTap)
    val onUserLocationChangedState = rememberUpdatedState(onUserLocationChanged)
    val userLocationIconState = rememberUpdatedState(userLocationIcon)

    val placemarks = remember { mutableMapOf<String, PlacemarkMapObject>() }
    val placemarkListeners = remember { mutableMapOf<String, MapObjectTapListener>() }
    val circleObjects = remember { mutableMapOf<String, CircleMapObject>() }

    // ⬇️ ASOSIY: STATE map — yangi key qo'shilganda recomposition triggerlanadi
    val iconCache = remember { mutableStateMapOf<String, NativeMarkerIcon>() }

    val userLocationListener = remember {
        object : UserLocationObjectListener {
            override fun onObjectAdded(view: UserLocationView) {
                applyUserLocationIcon(view, userLocationIconState.value)
            }

            override fun onObjectRemoved(view: UserLocationView) {}

            override fun onObjectUpdated(view: UserLocationView, event: ObjectEvent) {
                view.pin.geometry?.let { point ->
                    onUserLocationChangedState.value?.invoke(
                        LatLng(point.latitude, point.longitude)
                    )
                }
                applyUserLocationIcon(view, userLocationIconState.value)
            }
        }
    }

    // ============================================================
    // ICON YARATISH — markerlar o'zgarganda async cache to'ldirish
    // ============================================================
    LaunchedEffect(markers) {
        markers.forEach { marker ->
            val key = marker.style.cacheKey()
            if (iconCache.containsKey(key)) return@forEach

            scope.launch {
                try {
                    val icon = createMarkerIcon(
                        style = marker.style,
                        density = density,
                        textMeasurer = textMeasurer
                    )
                    iconCache[key] = icon
                } catch (e: Throwable) {
                    android.util.Log.e("YMK_MARK", "icon failed for ${marker.id}", e)
                }
            }
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            MapView(ctx).also { view ->
                controller.mapView = view
                view.mapWindow.map.move(
                    YCameraPosition(
                        Point(initialCamera.target.lat, initialCamera.target.lon),
                        initialCamera.zoom,
                        initialCamera.azimuth,
                        initialCamera.tilt
                    )
                )
                view.mapWindow.map.addInputListener(object : InputListener {
                    override fun onMapTap(map: YMap, point: Point) {
                        onMapTapState.value(LatLng(point.latitude, point.longitude))
                    }
                    override fun onMapLongTap(map: YMap, point: Point) {}
                })
            }
        },
        update = { view ->
            val map = view.mapWindow.map

            // Dark mode
            map.isNightModeEnabled = isDark

            // -------- MARKERS sinxron --------
            val incoming = markers.associateBy { it.id }

            // Olib tashlash
            (placemarks.keys - incoming.keys).toList().forEach { id ->
                placemarks.remove(id)?.let { pm ->
                    placemarkListeners.remove(id)?.let { pm.removeTapListener(it) }
                    map.mapObjects.remove(pm)
                }
            }

            // Qo'shish/yangilash
            incoming.forEach { (id, marker) ->
                val pm = placemarks[id] ?: map.mapObjects.addPlacemark(
                    Point(marker.position.lat, marker.position.lon)
                ).also { placemarks[id] = it }

                pm.geometry = Point(marker.position.lat, marker.position.lon)
                pm.zIndex = marker.zIndex

                // Tap listener
                if (placemarkListeners[id] == null) {
                    val listener = MapObjectTapListener { _, _ ->
                        onMarkerClickState.value(id); true
                    }
                    pm.addTapListener(listener)
                    placemarkListeners[id] = listener
                }

                // ⬇️ ASOSIY: Icon cache'da bo'lsa darhol o'rnatamiz.
                // Cache'da hali yo'q bo'lsa — LaunchedEffect tayyorlagach,
                // iconCache STATE o'zgaradi → recomposition → bu blok yana ishlaydi.
                val key = marker.style.cacheKey()
                iconCache[key]?.let { icon ->
                    pm.setIcon(
                        icon.provider,
                        IconStyle().apply {
                            anchor = PointF(
                                MarkerDimensions.ANCHOR_X,
                                MarkerDimensions.ANCHOR_Y
                            )
                        }
                    )
                }
            }

            // -------- CIRCLES sinxron --------
            val inCircles = circles.associateBy { it.id }
            (circleObjects.keys - inCircles.keys).toList().forEach { id ->
                circleObjects.remove(id)?.let { map.mapObjects.remove(it) }
            }
            inCircles.forEach { (id, c) ->
                val obj = circleObjects[id] ?: map.mapObjects.addCircle(
                    YCircle(Point(c.center.lat, c.center.lon), c.radiusMeters.toFloat())
                ).also { circleObjects[id] = it }
                obj.geometry = YCircle(
                    Point(c.center.lat, c.center.lon),
                    c.radiusMeters.toFloat()
                )
                obj.fillColor = c.fillColor.toInt()
                obj.strokeColor = c.strokeColor.toInt()
                obj.strokeWidth = c.strokeWidthDp
            }

            // -------- USER LOCATION LAYER --------
            if (showUserLocation) {
                if (controller.userLocationLayer == null) {
                    controller.userLocationLayer = MapKitFactory.getInstance()
                        .createUserLocationLayer(view.mapWindow).apply {
                            isVisible = true
                            isHeadingModeActive = true
                            setObjectListener(userLocationListener)
                        }
                }
            } else {
                controller.userLocationLayer?.let { layer ->
                    layer.isVisible = false
                    layer.setObjectListener(null)
                    controller.userLocationLayer = null
                }
            }
        }
    )

    DisposableEffect(lifecycleOwner) {
        val obs = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    MapKitFactory.getInstance().onStart()
                    controller.mapView?.onStart()
                }
                Lifecycle.Event.ON_STOP -> {
                    controller.mapView?.onStop()
                    MapKitFactory.getInstance().onStop()
                }
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(obs)
            placemarks.clear()
            placemarkListeners.clear()
            circleObjects.clear()
            iconCache.clear()
            controller.userLocationLayer?.setObjectListener(null)
            controller.userLocationLayer = null
            controller.mapView = null
        }
    }
}