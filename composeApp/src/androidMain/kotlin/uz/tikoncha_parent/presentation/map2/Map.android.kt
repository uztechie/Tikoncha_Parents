package uz.tikoncha_parent.presentation.map2

import android.graphics.PointF
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
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
import uz.tikoncha_parent.AppHolder

actual object MapKitInitializer {
    @Volatile
    private var initialized = false

    actual fun initialize(apiKey: String) {
        if (initialized) return
        MapKitFactory.setApiKey(apiKey)
        MapKitFactory.initialize(AppHolder.app)
        initialized = true
    }
}

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
        val map = mapView?.mapWindow?.map ?: return
        val layer = userLocationLayer ?: return
        val pos = layer.cameraPosition() ?: return
        val target = YCameraPosition(pos.target, 16f, 0f, 0f)
        if (animated) map.move(target, Animation(Animation.Type.SMOOTH, 0.4f), null)
        else map.move(target)
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

private fun MarkerStyle.cacheKey(): String =
    "${backgroundColor}_${isSelected}_${avatarUrl ?: text}_${showText}"

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
    val iconCache = remember { mutableMapOf<String, NativeMarkerIcon>() }
    val markerKeys = remember { mutableMapOf<String, String>() }

    val userLocationListener = remember {
        object : UserLocationObjectListener {
            override fun onObjectAdded(view: UserLocationView) {
                Log.d("YandexMap", "🟢 onObjectAdded — JOYLASHUV ANIQLANDI!")
                applyUserLocationIcon(view, userLocationIconState.value)
            }
            override fun onObjectRemoved(view: UserLocationView) {}
            override fun onObjectUpdated(view: UserLocationView, event: ObjectEvent) {
                view.pin.geometry?.let { point ->
                    Log.d("YandexMap", "🟡 onObjectUpdated lat=${point.latitude}")
                    onUserLocationChangedState.value?.invoke(LatLng(point.latitude, point.longitude))
                }
                applyUserLocationIcon(view, userLocationIconState.value)
            }
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            Log.d("YandexMap", "📍 MapView FACTORY")
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
            Log.d("YandexMap", "📍 update — showUserLocation=$showUserLocation, layer=${controller.userLocationLayer != null}")
            val map = view.mapWindow.map

            // ---- MARKERS sinxron ----
            val incoming = markers.associateBy { it.id }
            (placemarks.keys - incoming.keys).toList().forEach { id ->
                placemarks.remove(id)?.let { pm ->
                    placemarkListeners.remove(id)?.let { pm.removeTapListener(it) }
                    map.mapObjects.remove(pm)
                }
                markerKeys.remove(id)
            }
            incoming.forEach { (id, marker) ->
                val pm = placemarks[id] ?: map.mapObjects.addPlacemark(
                    Point(marker.position.lat, marker.position.lon)
                ).also { placemarks[id] = it }
                pm.geometry = Point(marker.position.lat, marker.position.lon)
                pm.zIndex = marker.zIndex
                if (placemarkListeners[id] == null) {
                    val listener = MapObjectTapListener { _, _ ->
                        onMarkerClickState.value(id); true
                    }
                    pm.addTapListener(listener)
                    placemarkListeners[id] = listener
                }
            }

            // ---- CIRCLES sinxron ----
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

            // ---- USER LOCATION LAYER (update blokida — avvalgi ishlagan holat) ----
            if (showUserLocation) {
                if (controller.userLocationLayer == null) {
                    Log.d("YandexMap", "📍 UserLocationLayer YARATILMOQDA")
                    controller.userLocationLayer = MapKitFactory.getInstance()
                        .createUserLocationLayer(view.mapWindow).apply {
                            isVisible = true
                            isHeadingModeActive = true
                            setObjectListener(userLocationListener)
                        }
                    Log.d("YandexMap", "✅ UserLocationLayer YARATILDI")
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

    // ---- MARKER ICONS (async load) ----
    LaunchedEffect(markers) {
        markers.forEach { marker ->
            val newKey = marker.style.cacheKey()
            if (markerKeys[marker.id] == newKey) return@forEach
            markerKeys[marker.id] = newKey

            scope.launch {
                val icon = iconCache[newKey] ?: run {
                    val newIcon = createMarkerIcon(marker.style, density, textMeasurer)
                    iconCache[newKey] = newIcon
                    newIcon
                }
                placemarks[marker.id]?.setIcon(
                    icon.provider,
                    IconStyle().apply {
                        anchor = PointF(MarkerDimensions.ANCHOR_X, MarkerDimensions.ANCHOR_Y)
                    }
                )
            }
        }
    }

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
            markerKeys.clear()
            controller.userLocationLayer?.setObjectListener(null)
            controller.userLocationLayer = null
            controller.mapView = null
        }
    }
}

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