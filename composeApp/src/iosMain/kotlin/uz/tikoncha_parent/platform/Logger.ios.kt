package uz.tikoncha_parent.project.platform

import platform.Foundation.NSLog
import uz.tikoncha_parent.platform.KmpLogger

actual object Logger : KmpLogger {
    actual override fun d(tag: String, message: String) {
        NSLog("DEBUG: $tag: $message")
    }

    actual override fun e(
        tag: String,
        message: String,
        throwable: Throwable?
    ) {
        NSLog("ERROR: $tag: $message ${throwable?.message ?: ""}")
    }
}