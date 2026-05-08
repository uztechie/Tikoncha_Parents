package uz.tikoncha_parent.presentation.map

import androidx.compose.ui.graphics.ImageBitmap

expect suspend fun loadImageBitmap(url: String): ImageBitmap?


