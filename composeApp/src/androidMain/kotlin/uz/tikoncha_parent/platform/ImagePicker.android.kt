package uz.tikoncha_parent.platform

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@Composable
actual fun rememberImagePicker(onPickedImage: (PickedImage) -> Unit): () -> Unit {
    val context = LocalContext.current
    val activity = context.findActivity() ?: error("Activity not found")

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            runBlocking {
                val bytes = withContext(Dispatchers.IO) {
                    activity.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                }
                if (bytes != null) {
                    val type = activity.contentResolver.getType(uri)
                    onPickedImage(PickedImage(bytes, type))
                }
            }
        }
    }
    return remember { { launcher.launch("image/*") } }
}

actual fun decodeImageBitmapOrNull(bytes: ByteArray): ImageBitmap? =
    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
