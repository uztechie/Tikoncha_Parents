package uz.tikoncha_parent.presentation.map2

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import com.yandex.runtime.image.ImageProvider


actual class NativeMarkerIcon(val provider: ImageProvider)

actual fun ImageBitmap.toNativeMarkerIcon(): NativeMarkerIcon {
    val bmp = this.asAndroidBitmap()
    return NativeMarkerIcon(ImageProvider.fromBitmap(bmp))
}