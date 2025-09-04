package uz.tikoncha_parent.platform

import android.content.ClipData
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.toClipEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

actual suspend fun copyPlainText(
    clipboard: Clipboard?,
    text: String
) = withContext(Dispatchers.Main.immediate) {
    // ClipData → ClipEntry → setClipEntry (suspend)
    val entry = ClipData.newPlainText("plain", text).toClipEntry()
    val cb    = clipboard ?: error("Clipboard not passed on Android")
    cb.setClipEntry(entry)
}