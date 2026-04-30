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
import kotlinx.cinterop.useContents
import kotlinx.cinterop.usePinned


actual class NativeMarkerIcon(val image: UIImage)


@OptIn(ExperimentalForeignApi::class)
actual fun ImageBitmap.toNativeMarkerIcon(): NativeMarkerIcon {
    println("YMK_BRIDGE: 🎨 Converting ${this.width}x${this.height}")

    val skiaBitmap = this.asSkiaBitmap()
    val skiaImage = Image.makeFromBitmap(skiaBitmap)
    val pngData = skiaImage.encodeToData()
        ?: error("YMK_BRIDGE: ❌ encodeToData returned null")
    val pngBytes = pngData.bytes
    println("YMK_BRIDGE: PNG bytes = ${pngBytes.size}")

    if (pngBytes.isEmpty()) {
        error("YMK_BRIDGE: ❌ PNG bytes are empty!")
    }

    val nsData = pngBytes.usePinned { pinned ->
        NSData.create(bytes = pinned.addressOf(0), length = pngBytes.size.toULong())
    }
    val uiImage = UIImage.imageWithData(nsData)
        ?: error("YMK_BRIDGE: ❌ UIImage.imageWithData returned null")

    val sizeStr = uiImage.size.useContents { "$width x $height" }
    println("YMK_BRIDGE: ✅ UIImage created, size=$sizeStr")

    return NativeMarkerIcon(uiImage)
}