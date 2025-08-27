package org.example.project.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

data class PickedImage(
    val bytes: ByteArray,
    val mimeType: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PickedImage

        if (!bytes.contentEquals(other.bytes)) return false
        if (mimeType != other.mimeType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bytes.contentHashCode()
        result = 31 * result + (mimeType?.hashCode() ?: 0)
        return result
    }
}

@Composable
expect fun rememberImagePicker(onPickedImage: (PickedImage) -> Unit): () -> Unit

expect fun decodeImageBitmapOrNull(bytes: ByteArray): ImageBitmap?