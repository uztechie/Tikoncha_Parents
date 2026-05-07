package uz.tikoncha_parent.presentation.map2

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

internal const val EARTH_RADIUS_METERS = 6371000.0

/** Circle'ni polygon vertices'ga aylantiradi (reverse fill uchun). */
internal fun circleToPolygonPoints(
    center: LatLng,
    radiusMeters: Double,
    segments: Int = 64
): List<LatLng> {
    val latRad = center.lat * PI / 180.0
    return List(segments) { i ->
        val angle = 2 * PI * i / segments
        val dLat = radiusMeters * cos(angle) / EARTH_RADIUS_METERS * 180.0 / PI
        val dLon = radiusMeters * sin(angle) / (EARTH_RADIUS_METERS * cos(latRad)) * 180.0 / PI
        LatLng(center.lat + dLat, center.lon + dLon)
    }
}

/** Reverse polygon uchun "butun dunyo" outer ring. */
internal val WORLD_OUTER_RING: List<LatLng> = listOf(
    LatLng(85.0, -179.99),
    LatLng(-85.0, -179.99),
    LatLng(-85.0, 179.99),
    LatLng(85.0, 179.99),
)

fun zoomForCircleRadius(radiusMeters: Int): Float = when {
    radiusMeters <= 50    -> 18f
    radiusMeters <= 100   -> 17f
    radiusMeters <= 250   -> 16f
    radiusMeters <= 500   -> 15f
    radiusMeters <= 1000  -> 14f
    radiusMeters <= 2500  -> 13f
    radiusMeters <= 5000  -> 12f
    radiusMeters <= 10000 -> 11f
    else                  -> 10f
}