package uz.tikoncha_parent.presentation.home.schedule

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.ui.BorderColor
import uz.tikoncha_parent.ui.CheckBoxCheckBackgroundColor
import uz.tikoncha_parent.ui.CheckBoxCheckBorderColor
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.PrimaryLightColor

@Composable
fun RoundedCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val boxSize = 26.dp
    val shape = RoundedCornerShape(8.dp)
    val green = CheckBoxCheckBackgroundColor
    val borderColor = if (checked) CheckBoxCheckBorderColor else BorderColor

    Box(
        modifier = modifier
            .size(boxSize)
            .clip(shape)
            .background(if (checked) green else Color.Transparent)
            .then(
                if (!checked) Modifier.border(
                    width = 1.dp,
                    color = borderColor,
                    shape = shape
                ) else Modifier.border(
                    width = 1.dp,
                    color = borderColor,
                    shape = shape
                )
            )
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            // Oq "✓" chizib beramiz (asset talab qilmaydi)
            Canvas(modifier = Modifier.size(14.dp)) {
                val strokeWidth = size.minDimension * 0.14f
                val p = Path().apply {
                    // oddiy check: chap pastdan o'ng yuqoriga
                    moveTo(size.width * 0.10f, size.height * 0.55f)
                    lineTo(size.width * 0.42f, size.height * 0.85f)
                    lineTo(size.width * 0.90f, size.height * 0.20f)
                }
                drawPath(
                    path = p,
                    color = PrimaryColor,
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
        }
    }
}
