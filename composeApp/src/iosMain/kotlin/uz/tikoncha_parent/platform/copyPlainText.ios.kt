package uz.tikoncha_parent.platform

import androidx.compose.ui.platform.Clipboard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.UIKit.UIPasteboard

actual suspend fun copyPlainText(
    clipboard: Clipboard?,   // keltirilgan, lekin iOS’da hozircha yaroqsiz
    text: String
) = withContext(Dispatchers.Main.immediate) {
    UIPasteboard.generalPasteboard.string = text        // native API
}