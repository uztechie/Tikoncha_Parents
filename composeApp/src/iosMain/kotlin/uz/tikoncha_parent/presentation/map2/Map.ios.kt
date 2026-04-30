package uz.tikoncha_parent.presentation.map2

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.viewinterop.UIKitInteropInteractionMode
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import cocoapods.YandexMapsMobile.YMKAnimation
import cocoapods.YandexMapsMobile.YMKAnimationType
import cocoapods.YandexMapsMobile.YMKCameraPosition
import cocoapods.YandexMapsMobile.YMKCircle
import cocoapods.YandexMapsMobile.YMKCircleMapObject
import cocoapods.YandexMapsMobile.YMKIconStyle
import cocoapods.YandexMapsMobile.YMKMap
import cocoapods.YandexMapsMobile.YMKMapInputListenerProtocol
import cocoapods.YandexMapsMobile.YMKMapKit
import cocoapods.YandexMapsMobile.YMKMapObject
import cocoapods.YandexMapsMobile.YMKMapObjectTapListenerProtocol
import cocoapods.YandexMapsMobile.YMKMapView
import cocoapods.YandexMapsMobile.YMKObjectEvent
import cocoapods.YandexMapsMobile.YMKPlacemarkMapObject
import cocoapods.YandexMapsMobile.YMKPoint
import cocoapods.YandexMapsMobile.YMKUserLocationLayer
import cocoapods.YandexMapsMobile.YMKUserLocationObjectListenerProtocol
import cocoapods.YandexMapsMobile.YMKUserLocationView
import cocoapods.YandexMapsMobile.setApiKey
import cocoapods.YandexMapsMobile.sharedInstance
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import kotlinx.coroutines.launch
import platform.CoreGraphics.CGPointMake
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSValue
import platform.UIKit.UIColor
import platform.UIKit.UIView
import platform.UIKit.valueWithCGPoint
import platform.darwin.NSObject
import kotlin.concurrent.Volatile

// ============================================================
// HELPER FUNKSIYALAR
// ============================================================

@OptIn(ExperimentalForeignApi::class)
private fun Long.toUIColor(): UIColor {
    val a = ((this shr 24) and 0xFF) / 255.0
    val r = ((this shr 16) and 0xFF) / 255.0
    val g = ((this shr 8) and 0xFF) / 255.0
    val b = (this and 0xFF) / 255.0
    return UIColor.colorWithRed(
        red = r,
        green = g,
        blue = b,
        alpha = if (a == 0.0) 1.0 else a
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun ymkPoint(lat: Double, lon: Double): YMKPoint =
    YMKPoint.pointWithLatitude(latitude = lat, longitude = lon)

@OptIn(ExperimentalForeignApi::class)
private fun LatLng.toYMK(): YMKPoint = ymkPoint(lat, lon)

@OptIn(ExperimentalForeignApi::class)
private fun ymkCameraPosition(
    target: YMKPoint,
    zoom: Float,
    azimuth: Float = 0f,
    tilt: Float = 0f
): YMKCameraPosition =
    YMKCameraPosition.cameraPositionWithTarget(
        target = target,
        zoom = zoom,
        azimuth = azimuth,
        tilt = tilt
    )

@OptIn(ExperimentalForeignApi::class)
private fun smoothAnimation(duration: Float = 0.4f): YMKAnimation =
    YMKAnimation.animationWithType(
        type = YMKAnimationType.YMKAnimationTypeSmooth,
        duration = duration
    )

private fun zoomForSpan(maxSpanDegrees: Double): Float = when {
    maxSpanDegrees > 180.0 -> 1f
    maxSpanDegrees > 90.0 -> 2f
    maxSpanDegrees > 45.0 -> 3f
    maxSpanDegrees > 20.0 -> 4f
    maxSpanDegrees > 10.0 -> 5f
    maxSpanDegrees > 5.0 -> 6f
    maxSpanDegrees > 2.0 -> 7f
    maxSpanDegrees > 1.0 -> 8f
    maxSpanDegrees > 0.5 -> 9f
    maxSpanDegrees > 0.25 -> 10f
    maxSpanDegrees > 0.1 -> 11f
    maxSpanDegrees > 0.05 -> 12f
    maxSpanDegrees > 0.025 -> 13f
    maxSpanDegrees > 0.01 -> 14f
    maxSpanDegrees > 0.005 -> 15f
    else -> 16f
}

@OptIn(ExperimentalForeignApi::class)
private fun anchorIconStyle(): YMKIconStyle = YMKIconStyle().apply {
    setAnchor(
        NSValue.valueWithCGPoint(
            CGPointMake(
                MarkerDimensions.ANCHOR_X.toDouble(),
                MarkerDimensions.ANCHOR_Y.toDouble()
            )
        )
    )
}

// ============================================================
// MAP KIT INITIALIZER
// ============================================================

@OptIn(ExperimentalForeignApi::class)
actual object MapKitInitializer {
    @Volatile
    private var initialized = false

    actual fun initialize(apiKey: String) {
        if (initialized) return
        YMKMapKit.setApiKey(apiKey)
        YMKMapKit.sharedInstance()
        initialized = true
    }
}

// ============================================================
// MAP CONTROLLER
// ============================================================

@OptIn(ExperimentalForeignApi::class)
@Stable
actual class MapController actual constructor() {
    internal var mapView: YMKMapView? = null
    internal var userLocationLayer: YMKUserLocationLayer? = null

    actual fun moveTo(position: CameraPosition, animated: Boolean) {
        val map = mapView?.mapWindow?.map ?: return
        val target = ymkCameraPosition(
            target = position.target.toYMK(),
            zoom = position.zoom,
            azimuth = position.azimuth,
            tilt = position.tilt
        )
        if (animated) {
            map.moveWithCameraPosition(
                cameraPosition = target,
                animation = smoothAnimation(),
                cameraCallback = null
            )
        } else {
            map.moveWithCameraPosition(target)
        }
    }

    actual fun fitBounds(points: List<LatLng>, paddingDp: Float, animated: Boolean) {
        if (points.isEmpty()) return
        val map = mapView?.mapWindow?.map ?: return

        if (points.size == 1) {
            moveTo(
                position = CameraPosition(target = points.first(), zoom = 16f),
                animated = animated
            )
            return
        }

        val minLat = points.minOf { it.lat }
        val maxLat = points.maxOf { it.lat }
        val minLon = points.minOf { it.lon }
        val maxLon = points.maxOf { it.lon }

        val centerLat = (minLat + maxLat) / 2.0
        val centerLon = (minLon + maxLon) / 2.0

        val maxSpan = maxOf(maxLat - minLat, maxLon - minLon)
        val zoom = zoomForSpan(maxSpan)
        val finalZoom = (zoom - 0.5f).coerceAtLeast(1f)

        val target = ymkCameraPosition(
            target = ymkPoint(centerLat, centerLon),
            zoom = finalZoom
        )

        if (animated) {
            map.moveWithCameraPosition(
                cameraPosition = target,
                animation = smoothAnimation(),
                cameraCallback = null
            )
        } else {
            map.moveWithCameraPosition(target)
        }
    }

    actual fun moveToUserLocation(animated: Boolean) {
        tryMoveToUserLocation(animated)
    }

    actual fun tryMoveToUserLocation(animated: Boolean): Boolean {
        val map = mapView?.mapWindow?.map ?: return false
        val layer = userLocationLayer ?: return false
        val pos = layer.cameraPosition() ?: return false
        val target = ymkCameraPosition(target = pos.target, zoom = 16f)
        if (animated) {
            map.moveWithCameraPosition(
                cameraPosition = target,
                animation = smoothAnimation(),
                cameraCallback = null
            )
        } else {
            map.moveWithCameraPosition(target)
        }
        return true
    }
}

@Composable
actual fun rememberMapController(): MapController = remember { MapController() }

// ============================================================
// LISTENERS
// ============================================================

@OptIn(ExperimentalForeignApi::class)
private class PlacemarkTapListener(
    private val markerId: String,
    private val onTap: (String) -> Unit
) : NSObject(), YMKMapObjectTapListenerProtocol {
    override fun onMapObjectTapWithMapObject(
        mapObject: YMKMapObject,
        point: YMKPoint
    ): Boolean {
        onTap(markerId)
        return true
    }
}

@OptIn(ExperimentalForeignApi::class)
private class MapInputListener(
    private val onTap: (LatLng) -> Unit
) : NSObject(), YMKMapInputListenerProtocol {
    override fun onMapTapWithMap(map: YMKMap, point: YMKPoint) {
        onTap(LatLng(point.latitude, point.longitude))
    }
    override fun onMapLongTapWithMap(map: YMKMap, point: YMKPoint) {}
}

@OptIn(ExperimentalForeignApi::class)
private class UserLocationListener(
    private val getIcon: () -> NativeMarkerIcon?,
    private val onLocationChanged: (LatLng) -> Unit
) : NSObject(), YMKUserLocationObjectListenerProtocol {

    override fun onObjectAddedWithView(view: YMKUserLocationView) {
        applyIcon(view)
    }

    override fun onObjectRemovedWithView(view: YMKUserLocationView) {}

    override fun onObjectUpdatedWithView(view: YMKUserLocationView, event: YMKObjectEvent) {
        view.pin().geometry.let { point ->
            onLocationChanged(LatLng(point.latitude, point.longitude))
        }
        applyIcon(view)
    }

    private fun applyIcon(view: YMKUserLocationView) {
        val icon = getIcon() ?: return
        val style = anchorIconStyle()
        view.pin().setIconWithImage(icon.image)
        view.pin().setIconStyleWithStyle(style)
        view.arrow().setIconWithImage(icon.image)
        view.arrow().setIconStyleWithStyle(style)
        view.accuracyCircle().setFillColor(UIColor.clearColor)
    }
}

// ============================================================
// COMPOSABLE — YandexMap
// ============================================================

@OptIn(ExperimentalForeignApi::class)
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
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val textMeasurer = rememberTextMeasurer()

    val onMarkerClickState = rememberUpdatedState(onMarkerClick)
    val onMapTapState = rememberUpdatedState(onMapTap)
    val onUserLocationChangedState = rememberUpdatedState(onUserLocationChanged)
    val userLocationIconState = rememberUpdatedState(userLocationIcon)

    val placemarks = remember { mutableMapOf<String, YMKPlacemarkMapObject>() }
    val listeners = remember { mutableMapOf<String, PlacemarkTapListener>() }
    val circleObjects = remember { mutableMapOf<String, YMKCircleMapObject>() }
    val mapInputListener = remember { MapInputListener { onMapTapState.value(it) } }

    // ⬇️ ASOSIY: STATE map — yangi key qo'shilganda recomposition triggerlanadi
    val iconCache = remember { mutableStateMapOf<String, NativeMarkerIcon>() }

    val userLocationListener = remember {
        UserLocationListener(
            getIcon = { userLocationIconState.value },
            onLocationChanged = { latLng ->
                onUserLocationChangedState.value?.invoke(latLng)
            }
        )
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
                    println("YMK_MARK: ❌ icon failed for ${marker.id}: ${e.message}")
                }
            }
        }
    }

    UIKitView(
        modifier = modifier,
        factory = {
            val view = YMKMapView(frame = CGRectZero.readValue())
            controller.mapView = view

            view.mapWindow?.map?.moveWithCameraPosition(
                ymkCameraPosition(
                    target = initialCamera.target.toYMK(),
                    zoom = initialCamera.zoom,
                    azimuth = initialCamera.azimuth,
                    tilt = initialCamera.tilt
                )
            )
            view.mapWindow?.map?.addInputListenerWithInputListener(mapInputListener)
            view as UIView
        },
        update = { uiView ->
            val view = uiView as YMKMapView
            val map = view.mapWindow?.map ?: return@UIKitView

            // Dark mode
            map.setNightModeEnabled(isDark)

            // -------- MARKERS sinxron --------
            val incoming = markers.associateBy { it.id }

            // Olib tashlash
            (placemarks.keys - incoming.keys).toList().forEach { id ->
                placemarks.remove(id)?.let { pm ->
                    listeners.remove(id)?.let { listener ->
                        pm.removeTapListenerWithTapListener(listener)
                    }
                    map.mapObjects.removeWithMapObject(pm)
                }
            }

            // Qo'shish/yangilash
            incoming.forEach { (id, marker) ->
                val point = marker.position.toYMK()

                val pm = placemarks[id] ?: map.mapObjects
                    .addPlacemarkWithPoint(point)
                    .also { placemarks[id] = it }

                pm.setGeometry(point)
                pm.setZIndex(marker.zIndex)

                // Tap listener
                if (listeners[id] == null) {
                    val listener = PlacemarkTapListener(id) { onMarkerClickState.value(it) }
                    pm.addTapListenerWithTapListener(listener)
                    listeners[id] = listener
                }

                // ⬇️ ASOSIY: Icon cache'da bo'lsa darhol o'rnatamiz.
                // Cache'da hali yo'q bo'lsa — LaunchedEffect tayyorlagach,
                // iconCache STATE o'zgaradi → recomposition → bu blok yana ishlaydi.
                val key = marker.style.cacheKey()
                iconCache[key]?.let { icon ->
                    pm.setIconWithImage(icon.image)
                    pm.setIconStyleWithStyle(anchorIconStyle())
                }
            }

            // -------- CIRCLES sinxron --------
            val inCircles = circles.associateBy { it.id }

            (circleObjects.keys - inCircles.keys).toList().forEach { id ->
                circleObjects.remove(id)?.let { map.mapObjects.removeWithMapObject(it) }
            }

            inCircles.forEach { (id, c) ->
                val geom = YMKCircle.circleWithCenter(
                    center = c.center.toYMK(),
                    radius = c.radiusMeters.toFloat()
                )

                val obj = circleObjects[id]
                    ?: map.mapObjects.addCircleWithCircle(geom).also {
                        circleObjects[id] = it
                    }

                obj.setGeometry(geom)
                obj.setFillColor(c.fillColor.toUIColor())
                obj.setStrokeColor(c.strokeColor.toUIColor())
                obj.setStrokeWidth(c.strokeWidthDp)
            }

            // -------- USER LOCATION LAYER --------
            if (showUserLocation) {
                if (controller.userLocationLayer == null) {
                    val window = view.mapWindow ?: return@UIKitView
                    controller.userLocationLayer = YMKMapKit.sharedInstance()
                        .createUserLocationLayerWithMapWindow(window).apply {
                            setVisibleWithOn(true)
                            setHeadingModeActive(true)
                            setObjectListenerWithObjectListener(userLocationListener)
                        }
                }
            } else {
                controller.userLocationLayer?.let { layer ->
                    layer.setVisibleWithOn(false)
                    layer.setObjectListenerWithObjectListener(null)
                    controller.userLocationLayer = null
                }
            }
        },
        properties = UIKitInteropProperties(
            interactionMode = UIKitInteropInteractionMode.NonCooperative,
            isNativeAccessibilityEnabled = false
        )
    )

    DisposableEffect(Unit) {
        onDispose {
            placemarks.clear()
            listeners.clear()
            circleObjects.clear()
            iconCache.clear()
            controller.userLocationLayer?.setObjectListenerWithObjectListener(null)
            controller.userLocationLayer = null
            controller.mapView = null
        }
    }
}