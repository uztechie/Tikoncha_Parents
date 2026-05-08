package uz.tikoncha_parent.presentation.map

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import com.yandex.runtime.image.ImageProvider


actual class NativeMarkerIcon(val provider: ImageProvider)

actual fun ImageBitmap.toNativeMarkerIcon(): NativeMarkerIcon {
    val bmp = this.asAndroidBitmap()
    android.util.Log.d("MARKER_DBG", "android bmp=${bmp.width}x${bmp.height} config=${bmp.config} hasAlpha=${bmp.hasAlpha()}")
    return NativeMarkerIcon(ImageProvider.fromBitmap(bmp))
}