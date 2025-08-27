package org.example.project.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.uikit.LocalUIViewController
import com.seiko.imageloader.component.decoder.SkiaImageDecoder
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.create
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePNGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.darwin.NSObject
import platform.objc.OBJC_ASSOCIATION_RETAIN_NONATOMIC
import platform.objc.objc_setAssociatedObject
import platform.posix.memcpy
import org.jetbrains.skia.Image as SkiaImage

@Composable
actual fun rememberImagePicker(onPickedImage: (PickedImage) -> Unit): () -> Unit {
    val presenter = LocalUIViewController.current

    return remember {
        {
            presentImagePicker(presenter) { bytes ->
                onPickedImage(PickedImage(bytes, mimeType = "image/jpeg"))
            }
        }
    }
}

actual fun decodeImageBitmapOrNull(bytes: ByteArray): ImageBitmap?  = runCatching{
    SkiaImage.makeFromEncoded(bytes).toComposeImageBitmap()
}.getOrNull()

@OptIn(ExperimentalForeignApi::class)
private fun presentImagePicker(
    presenter: UIViewController,
    onPickedBytes: (ByteArray) -> Unit
) {
    val picker = UIImagePickerController().apply {
        sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
        allowsEditing = false
    }

    val delegate = PickerDelegate(
        onCancel = { picker.dismissViewControllerAnimated(true, completion = null) },
        onPicked = { uiImage ->
            val jpeg: NSData? = UIImageJPEGRepresentation(uiImage, 0.92)
            val data: NSData = jpeg ?: (UIImagePNGRepresentation(uiImage)
                ?: error("Failed to encode image"))
            onPickedBytes(data.toByteArray())
            picker.dismissViewControllerAnimated(true, completion = null)
        }
    )

    picker.delegate = delegate
    objc_setAssociatedObject(
        picker,
        delegateAssociationKey,
        delegate,
        OBJC_ASSOCIATION_RETAIN_NONATOMIC
    )
    presenter.presentViewController(picker, true, null)
}

private class PickerDelegate(
    private val onCancel: () -> Unit,
    private val onPicked: (UIImage) -> Unit
) : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {
    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) = onCancel()
    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>
    ) {
        val img = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
        if (img != null) onPicked(img) else onCancel()
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray {
    val len = length.toInt()
    val out = ByteArray(len)
    if (len > 0) {
        memScoped {
            val src = bytes
            out.usePinned { dst -> memcpy(dst.addressOf(0), src, length) }
        }
    }
    return out
}

@OptIn(ExperimentalForeignApi::class)
private val delegateAssociationKey = StableRef.create(Any()).asCPointer()