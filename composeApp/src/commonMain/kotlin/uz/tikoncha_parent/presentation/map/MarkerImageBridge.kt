package uz.tikoncha_parent.presentation.map

import androidx.compose.ui.graphics.ImageBitmap

expect class NativeMarkerIcon

expect fun ImageBitmap.toNativeMarkerIcon(): NativeMarkerIcon