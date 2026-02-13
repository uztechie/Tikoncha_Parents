package uz.tikoncha_parent.data.mapper

import io.ktor.http.ContentType
import uz.tikoncha_parent.data.remote.TikonchaClient
import uz.tikoncha_parent.data.remote.TikonchaClient.Companion.BASE_URL_WITH_HTTPS
import uz.tikoncha_parent.domain.model.UploadPart
import uz.tikoncha_parent.platform.PickedImage

fun PickedImage.toUploadPart(defaultName: String = "avatar.jpg"): UploadPart{
    val ct = ContentType.parse(mimeType ?: "image/jpeg")
    return UploadPart(bytes = bytes, fileName = defaultName, contentType = ct)
}

fun String?.prepareAvatar(): String{
    if (this == null){
        return ""
    }
    return this
}