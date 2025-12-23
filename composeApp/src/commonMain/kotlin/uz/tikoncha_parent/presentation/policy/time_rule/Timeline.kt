package uz.tikoncha_parent.presentation.policy.time_rule

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.domain.model.MinuteRange
import uz.tikoncha_parent.ui.HintTextColor
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.SmallTextSize
import uz.tikoncha_parent.ui.theme.extendedColor
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun Timeline(
    ranges: List<MinuteRange>,
    allDay: Boolean = false,
    height: Dp = 15.dp
){
    val barColor = MaterialTheme.extendedColor.primaryColor
    val dashColor = MaterialTheme.extendedColor.hintColor

    val segmentMinutes = 30
    val segmentGap = 2.dp
    val corner = 2.dp

    Column {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
        ) {
            val w = size.width
            val h = size.height

            val totalMin = 24f * 60f
            val pxPerMin = w / totalMin
            val gapPx = 2.dp.toPx()
            val rPx = 2.dp.toPx()

            // 🟰 YAGONA PASTKI CHIZIQ (baseline)
            val baseline = h * 0.90f           // pastga yaqinroq (xohlasangiz 0.88–0.92 oralig‘ida sozlang)

            // Yashil va kulrang balandliklar
            val greenHeight = h * 0.80f
            val grayHeight  = greenHeight * 0.50f

            // Top larni umumiy baseline’dan hisoblaymiz (pastlar bir xil!)
            val greenTop = (baseline - greenHeight).coerceAtLeast(0f)
            val grayTop  = (baseline - grayHeight).coerceAtLeast(0f)

            // --- Kulrang fon segmentlar (30 daq grid) ---
            val totalSeg = (24 * 60) / 30
            for (seg in 0 until totalSeg) {
                val segStartMin = seg * 30
                val segEndMin = (seg + 1) * 30
                val sx = segStartMin * pxPerMin
                val ex = segEndMin * pxPerMin

                val left = sx + gapPx / 2f
                val right = ex - gapPx / 2f
                if (right <= left) continue

                drawRoundRect(
                    color = HintTextColor.copy(alpha = 0.35f),
                    topLeft = Offset(left, grayTop),
                    size = Size(right - left, grayHeight),
                    cornerRadius = CornerRadius(rPx, rPx),
                    style = Fill
                )
            }

            if (allDay){

                val totalSeg = (24 * 60) / 30
                for (seg in 0 until totalSeg) {
                    val segStartMin = seg * 30
                    val segEndMin = (seg + 1) * 30
                    val sx = segStartMin * pxPerMin
                    val ex = segEndMin * pxPerMin

                    val left = sx + gapPx / 2f
                    val right = ex - gapPx / 2f
                    if (right <= left) continue

                    drawRoundRect(
                        color = PrimaryColor,
                        topLeft = Offset(left, greenTop),
                        size = Size(right - left, greenHeight),
                        cornerRadius = CornerRadius(rPx, rPx),
                        style = Fill
                    )
                }

            }
            else{
                // --- Yashil segmentlar (ustidan) ---
                ranges.forEach { r ->
                    val startSeg = floor(r.start / 30f).toInt()
                    val endSegEx = ceil(r.end / 30f).toInt()

                    for (seg in startSeg until endSegEx) {
                        val segStartMin = seg * 30
                        val segEndMin = (seg + 1) * 30
                        val sMin = maxOf(segStartMin, r.start)
                        val eMin = minOf(segEndMin, r.end)
                        if (eMin <= sMin) continue

                        val sx = sMin * pxPerMin
                        val ex = eMin * pxPerMin
                        val left = sx + gapPx / 2f
                        val right = ex - gapPx / 2f
                        if (right <= left) continue

                        drawRoundRect(
                            color = PrimaryColor,
                            topLeft = Offset(left, greenTop),
                            size = Size(right - left, greenHeight),
                            cornerRadius = CornerRadius(rPx, rPx),
                            style = Fill
                        )
                    }
                }
            }




        }

        // Pastdagi soatlar (0–6–12–18–24)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf(0, 6, 12, 18, 24).forEach { t ->
                CustomText(
                    text = "$t",
                    color = MaterialTheme.extendedColor.hintColor,
                    fontSize = SmallTextSize
                )
            }
        }
    }
}