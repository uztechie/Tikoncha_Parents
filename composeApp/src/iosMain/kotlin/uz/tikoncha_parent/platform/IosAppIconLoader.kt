package uz.tikoncha_parent.platform

import androidx.compose.ui.graphics.ImageBitmap
import uz.tikoncha_parent.platform.AppIconLoader

class IosAppIconLoader: AppIconLoader {
    override fun load(packageName: String): ImageBitmap? = null
}