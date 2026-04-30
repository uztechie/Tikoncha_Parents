package uz.tikoncha_parent.presentation.map2

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.ImageBitmap
import org.jetbrains.compose.resources.DrawableResource

@Immutable
data class LatLng(val lat: Double, val lon: Double)

@Immutable
data class CameraPosition(
    val target: LatLng,
    val zoom: Float = 14f,
    val azimuth: Float = 0f,
    val tilt: Float = 0f
)

@Immutable
data class MapMarker(
    val id: String,
    val position: LatLng,
    val style: MarkerStyle,
    val zIndex: Float = 0f
)

data class MarkerStyle(
    val label: String,
    val backgroundColor: Long = 0xFF4BB462,   // ← bitta rang: border, pin, indikator hammasi shu
    val borderColor: Long = 0xFFFFFFFF,
    val textColor: Long = 0xFFFFFFFF,
    val showPersonIcon: Boolean = false,
    val avatarUrl: String? = null,
    val isSelected: Boolean = false,
    val placeholderBitmap: ImageBitmap? = null,
    val showText: Boolean = false,
    val text: String = ""
) {
    companion object {
        fun Self(text: String, placeholderBitmap: ImageBitmap? = null) = MarkerStyle(
            label = text,
            backgroundColor = 0xFFC3955B,   // jigarrang
            avatarUrl = null,
            placeholderBitmap = placeholderBitmap,
            showText = true,
            text = text
        )

        // CHILD — yashil
        fun Child(
            label: String,
            avatarUrl: String? = null,
            placeholderBitmap: ImageBitmap? = null
        ) = MarkerStyle(
            label = label,
            backgroundColor = 0xFF4BB462,   // ← yashil
            avatarUrl = avatarUrl,
            placeholderBitmap = placeholderBitmap,
            isSelected = false
        )

        // CHILD SELECTED — olov rang (jigarrang/altin)
        fun ChildSelected(
            label: String,
            avatarUrl: String? = null,
            placeholderBitmap: ImageBitmap? = null
        ) = MarkerStyle(
            label = label,
            backgroundColor = 0xFFFF8A00,   // ← OLOV RANG (to'q sariq)
            avatarUrl = avatarUrl,
            placeholderBitmap = placeholderBitmap,
            isSelected = true
        )
    }
}

@Immutable
data class MapCircle(
    val id: String,
    val center: LatLng,
    val radiusMeters: Double,
    val fillColor: Long = 0x3322C55E,
    val strokeColor: Long = 0xFF22C55E,
    val strokeWidthDp: Float = 2f
)