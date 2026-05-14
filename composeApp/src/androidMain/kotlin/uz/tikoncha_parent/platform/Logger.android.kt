package uz.tikoncha_parent.platform

import android.util.Log

private const val MAX_LOG_LENGTH = 3000

actual object Logger : KmpLogger {
    actual override fun d(tag: String, message: String) {
        if (message.length <= MAX_LOG_LENGTH) {
            Log.d(tag, message)
        } else {
            message.chunked(MAX_LOG_LENGTH).forEach { Log.d(tag, it) }
        }
    }

    actual override fun e(
        tag: String,
        message: String,
        throwable: Throwable?
    ) {
        if (message.length <= MAX_LOG_LENGTH) {
            Log.e(tag, message, throwable)
        } else {
            val chunks = message.chunked(MAX_LOG_LENGTH)
            chunks.forEachIndexed { i, chunk ->
                if (i == chunks.lastIndex) Log.e(tag, chunk, throwable)
                else Log.e(tag, chunk)
            }
        }
    }
}