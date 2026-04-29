package uz.tikoncha_parent.presentation.map2

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.toBitmap
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.getDrawableResourceBytes
import org.jetbrains.compose.resources.getSystemResourceEnvironment
import uz.tikoncha_parent.AppHolder

actual suspend fun loadImageBitmap(url: String): ImageBitmap? {
    val ctx = AppHolder.app
    val loader = ImageLoader(ctx)
    val request = ImageRequest.Builder(ctx).data(url).build()
    val result = loader.execute(request)
    return if (result is SuccessResult) {
        result.image.toBitmap().asImageBitmap()
    } else null
}
