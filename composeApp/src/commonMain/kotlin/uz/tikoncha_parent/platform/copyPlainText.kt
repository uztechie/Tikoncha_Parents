package uz.tikoncha_parent.platform

import androidx.compose.ui.platform.Clipboard

expect suspend fun copyPlainText(
    clipboard: Clipboard?,   // Android’ga uzatiladi
    text: String
)