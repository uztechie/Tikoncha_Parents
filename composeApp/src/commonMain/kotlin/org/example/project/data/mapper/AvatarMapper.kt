package org.example.project.data.mapper

import io.ktor.http.ContentType
import org.example.project.domain.model.UploadPart
import org.example.project.platform.PickedImage

fun PickedImage.toUploadPart(defaultName: String = "avatar.jpg"): UploadPart{
    val ct = ContentType.parse(mimeType ?: "image/jpeg")
    return UploadPart(bytes = bytes, fileName = defaultName, contentType = ct)
}