package uz.tikoncha_parent.presentation.map2

import androidx.compose.ui.graphics.ImageBitmap

expect class NativeMarkerIcon

expect fun ImageBitmap.toNativeMarkerIcon(): NativeMarkerIcon