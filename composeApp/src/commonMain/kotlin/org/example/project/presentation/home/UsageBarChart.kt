package org.example.project.presentation.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.ui.PrimaryColor
import kotlin.math.ceil

@Composable
fun UsageBarChart(
    data: Map<Int, Double>,
    modifier: Modifier = Modifier,
    barColor: Color = PrimaryColor
) {
    val max: Float = data.values.maxOrNull()?.toFloat()?:0f
    val maxHour = ceil(max/60).toInt()
    val durationLabels = getDurationLabels(maxHour)

    val daysOfWeek = listOf("Dush", "Sesh", "Chor", "Pay", "Jum", "Shan", "Yak")
    val showHours = data.size > 7
    val keys = data.keys.sorted()

    Row(modifier = modifier) {
        // ⬅ Y-o‘qi (label)
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(end = 10.dp, top = 10.dp, bottom = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            durationLabels.forEach { label ->
                var ext = if (label == "30" || label=="0") "daq" else "soat"
                Text(
                    text = "$label $ext",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.offset(y = (-6).dp)
                )
            }
        }

        // 📊 Bar chizma va label
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier
                .weight(1f)
                .padding(vertical = 10.dp)
            ) {
                AnimatedUsageBarChartCanvas(
                    data = data,
                    barColor = barColor
                )
            }

            // ⬇ Pastki label (X o‘qi)

            if (showHours){
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (i in 4..20 step 4) {
                        Text(
                            text = if (i < 10) "0$i" else "$i",
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            else{
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    daysOfWeek.forEach {
                        Text(
                            text = it,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }
}



fun getDurationLabels(hours:Int): List<String> {
    return when {
        hours <= 1 -> listOf("1", "30", "0")
        hours <= 2 -> listOf("2", "1", "0")
        hours <= 4 -> listOf("4", "2", "0")
        hours <= 6 -> listOf("6", "3", "0")
        hours <= 8 -> listOf("8", "4", "0")
        hours <= 10 -> listOf("10", "5", "0")
        hours <= 12 -> listOf("12", "6", "0")
        hours <= 14 -> listOf("14", "7", "0")
        hours <= 16 -> listOf("16", "8", "0")
        hours <= 18 -> listOf("18", "9", "0")
        hours <= 20 -> listOf("20", "10", "0")
        hours <= 22 -> listOf("22", "11", "0")
        else -> listOf("24", "12", "0")
    }
}

@Composable
fun AnimatedUsageBarChartCanvas(
    data: Map<Int, Double>,
    modifier: Modifier = Modifier,
    barColor: Color = PrimaryColor,
    cornerRadius: Dp = 6.dp
) {
    val sortedKeys = data.keys.sorted()
    val maxValueInMinutes = data.values.maxOrNull()?.takeIf { it > 0 } ?: 1.0

    // Define available hour steps (must be sorted ascending)
    val hourSteps = listOf(1, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24)

    // Convert max value to hours and round up
    val maxValueInHours = ceil(maxValueInMinutes / 60).toInt()
    val targetHour = hourSteps.firstOrNull { it >= maxValueInHours } ?: 24
    val maxValueAdjusted = targetHour * 60.0 // in minutes

    val animatedProgress = remember { Animatable(0f) }



    LaunchedEffect(data.toList()) {
        animatedProgress.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 0, easing = FastOutLinearInEasing)
        )
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        )
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val barCount = sortedKeys.size
        val spacing = if (barCount > 7) 4.dp.toPx() else 15.dp.toPx()

        val totalSpacing = spacing * (barCount - 1)
        val availableWidth = canvasWidth - totalSpacing
        val barWidth = (availableWidth / barCount).coerceAtLeast(2f)

        val corner = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx())

        // Horizontal lines (top = max, middle = 0.5 max, bottom = 0)
        val lineHeights = listOf(0f, canvasHeight / 2, canvasHeight)
        lineHeights.forEach { y ->
            drawLine(
                color = Color.Gray.copy(alpha = 0.3f),
                start = Offset(0f, y),
                end = Offset(canvasWidth, y),
                strokeWidth = 1.dp.toPx()
            )
        }

        sortedKeys.forEachIndexed { index, key ->
            val value = data[key] ?: 0.0
            val percent = (value / maxValueAdjusted).toFloat()
            val barHeight = canvasHeight * percent * animatedProgress.value

            val left = index * (barWidth + spacing)
            val top = canvasHeight - barHeight
            val rectSize = Size(barWidth, barHeight)

            drawRoundRect(
                color = barColor,
                topLeft = Offset(left, top),
                size = rectSize,
                cornerRadius = corner
            )

        }
    }
}