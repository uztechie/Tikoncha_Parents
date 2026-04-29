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
    val backgroundColor: Long = 0xFF22C55E,
    val borderColor: Long = 0xFFFFFFFF,
    val textColor: Long = 0xFFFFFFFF,
    val showPersonIcon: Boolean = false,
    val avatarUrl: String? = null,
    val isSelected: Boolean = false,
    val placeholderBitmap: ImageBitmap? = null,
    val showText: Boolean = false,         // ← rasm o'rniga matn chizish
    val text: String = ""                   // ← chiziladigan matn (masalan "Siz")
) {
    companion object {
        // SELF — matn bilan, jigarrang
        fun Self(
            text: String,
            placeholderBitmap: ImageBitmap? = null
        ) = MarkerStyle(
            label = text,
            backgroundColor = 0xFFB8916F,   // ← jigarrang/altin
            avatarUrl = null,                // self uchun rasm yo'q
            placeholderBitmap = placeholderBitmap,
            showText = true,                 // ← matn ko'rsatish
            text = text
        )

        // CHILD — rasm bilan, yashil
        fun Child(
            label: String,
            avatarUrl: String? = null,
            placeholderBitmap: ImageBitmap? = null
        ) = MarkerStyle(
            label = label,
            backgroundColor = 0xFF4BB462,
            avatarUrl = avatarUrl,
            placeholderBitmap = placeholderBitmap
        )

        fun ChildSelected(
            label: String,
            avatarUrl: String? = null,
            placeholderBitmap: ImageBitmap? = null
        ) = MarkerStyle(
            label = label,
            backgroundColor = 0xFF4BB462,
            avatarUrl = avatarUrl,
            isSelected = true,
            placeholderBitmap = placeholderBitmap
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