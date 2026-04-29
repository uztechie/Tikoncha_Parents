@file:OptIn(BetaInteropApi::class)

package uz.tikoncha_parent.presentation.map2

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import kotlinx.cinterop.BetaInteropApi
import org.jetbrains.skia.Image
import platform.Foundation.NSData
import platform.Foundation.create
import platform.UIKit.UIImage
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned


actual class NativeMarkerIcon(val image: UIImage)

@OptIn(ExperimentalForeignApi::class)
actual fun ImageBitmap.toNativeMarkerIcon(): NativeMarkerIcon {
    val skiaImage = Image.makeFromBitmap(this.asSkiaBitmap())
    val pngBytes = skiaImage.encodeToData()?.bytes
        ?: error("Failed to encode marker bitmap")

    val nsData = pngBytes.usePinned { pinned ->
        NSData.create(bytes = pinned.addressOf(0), length = pngBytes.size.toULong())
    }
    val uiImage = UIImage.imageWithData(nsData) ?: error("Failed to create UIImage")
    return NativeMarkerIcon(uiImage)
}