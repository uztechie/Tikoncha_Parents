package uz.tikoncha_parent.presentation.map2

import androidx.compose.ui.graphics.ImageBitmap
import org.jetbrains.compose.resources.DrawableResource

expect suspend fun loadImageBitmap(url: String): ImageBitmap?


