package uz.tikoncha_parent.domain.model

import io.ktor.http.ContentType

data class UploadPart(
    val bytes: ByteArray,
    val fileName: String,
    val contentType: ContentType
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as UploadPart

        if (!bytes.contentEquals(other.bytes)) return false
        if (fileName != other.fileName) return false
        if (contentType != other.contentType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bytes.contentHashCode()
        result = 31 * result + fileName.hashCode()
        result = 31 * result + contentType.hashCode()
        return result
    }
}
