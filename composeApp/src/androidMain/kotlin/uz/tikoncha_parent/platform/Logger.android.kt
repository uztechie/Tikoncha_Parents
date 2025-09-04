package uz.tikoncha_parent.platform

import android.util.Log

actual object Logger : KmpLogger {
    actual override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    actual override fun e(
        tag: String,
        message: String,
        throwable: Throwable?
    ) {
        Log.e(tag, message, throwable)
    }
}