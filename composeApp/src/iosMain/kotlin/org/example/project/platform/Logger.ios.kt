package org.example.project.platform

import platform.Foundation.NSLog

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