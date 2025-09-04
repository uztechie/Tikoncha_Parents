package uz.tikoncha_parent.presentation.map

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.Dp

class MarkerShape(
    private val cornerRadius: Dp,
    private val pointerWidth: Dp,
    private val pointerHeight: Dp
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        with(density) {
            val r = cornerRadius.toPx()
            val pw = pointerWidth.toPx()
            val ph = pointerHeight.toPx()
            val rectH = size.height - ph

            // 1) joylashuv uchun to'rtburchak
            val rect = Rect(0f, 0f, size.width, rectH)
            // 2) barcha burchak radiusi
            val radius = CornerRadius(r, r)
            // 3) RoundRect obyektini yaratamiz
            val roundRect = RoundRect(
                rect = rect,
                topLeft = radius,
                topRight = radius,
                bottomRight = radius,
                bottomLeft = radius
            )

            val path = Path().apply {
                // asosiy rounded rectangle
                addRoundRect(roundRect)
                // pointerni chizamiz
                val cx = size.width / 2f
                moveTo(cx - pw / 2, rectH)
                lineTo(cx, size.height)
                lineTo(cx + pw / 2, rectH)
                close()
            }
            return Outline.Generic(path)
        }
    }
}
