package org.example.project.platform

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.createBitmap

class AndroidAppIconLoader(
    private val context: Context
) : AppIconLoader {

    override fun load(packageName: String): ImageBitmap? = try {
        val pm = context.packageManager
        val drawable = pm.getApplicationIcon(packageName)

        when (drawable) {
            is BitmapDrawable -> drawable.bitmap.asImageBitmap()
            else -> {
                val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 128
                val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 128
                val bmp = createBitmap(width, height)
                val canvas = Canvas(bmp)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                bmp.asImageBitmap()
            }
        }
    } catch (e: Exception) {
        null
    }
}