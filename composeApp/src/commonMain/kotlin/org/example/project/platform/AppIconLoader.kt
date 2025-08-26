package org.example.project.platform

import androidx.compose.ui.graphics.ImageBitmap


interface AppIconLoader {
    fun load(packageName: String): ImageBitmap?
}
