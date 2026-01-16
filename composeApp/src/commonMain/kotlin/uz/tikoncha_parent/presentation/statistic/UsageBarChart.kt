package uz.tikoncha_parent.presentation.statistic

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.tikoncha_parent.ui.PrimaryColor
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.chor
import tikoncha_parents.composeapp.generated.resources.dush
import tikoncha_parents.composeapp.generated.resources.jum
import tikoncha_parents.composeapp.generated.resources.pay
import tikoncha_parents.composeapp.generated.resources.sesh
import tikoncha_parents.composeapp.generated.resources.shan
import tikoncha_parents.composeapp.generated.resources.yak
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor
import kotlin.math.ceil
@Composable
fun UsageBarChart(
    data: Map<Int, Double>,
    modifier: Modifier = Modifier,
    barColor: Color = PrimaryColor,
    state: StatisticState = StatisticState(),
    overlayPainter: Painter? = null,
    isWeekly: Boolean,
    onSubscriptionClick: () -> Unit = {},
    onHourlyBinClick: (startHour: Int, endHour: Int) -> Unit = { _, _ -> },
    onWeeklyDayClick: (dayIndex: Int) -> Unit = {},
) {
    val isEmpty = remember(data) { data.isEmpty() || data.values.all { it <= 0.0 } }
    val blur by animateDpAsState(if (state.showBlur) 10.dp else 0.dp, label = "blur")
    val overlayInteraction = remember { MutableInteractionSource() }

    Box(modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .blur(blur)
        ) {
            if (isWeekly) {
                WeeklyChart(
                    data = data,
                    barColor = barColor,
                    isEmpty = isEmpty,
                    onDayClick = onWeeklyDayClick
                )
            } else {
                HourlyChart(
                    data = data,
                    barColor = barColor,
                    isEmpty = isEmpty,
                    onBinClick = onHourlyBinClick
                )
            }
        }

        if (state.showBlur && overlayPainter != null) {
            androidx.compose.foundation.Image(
                painter = overlayPainter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .aspectRatio(2f)
                    .align(Alignment.Center)
                    .clickable(indication = null, interactionSource = overlayInteraction) {
                        onSubscriptionClick()
                    }
            )
        }
    }
}

@Composable
private fun WeeklyChart(
    data: Map<Int, Double>,
    barColor: Color,
    isEmpty: Boolean,
    onDayClick: (Int) -> Unit,
) {
    val days = remember {
        listOf(
            Res.string.dush, Res.string.sesh, Res.string.chor, Res.string.pay,
            Res.string.jum, Res.string.shan, Res.string.yak
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(vertical = 10.dp)
    ) {
        AnimatedUsageBarChartCanvas(
            data = data,
            isWeekly = true,
            barColor = barColor,
            showEmpty = isEmpty
        )
        WeeklyClickLayer(onDayClick)
    }

    Row(Modifier.fillMaxWidth()) {
        repeat(7) { i ->
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                CustomText(
                    text = stringResource(days[i]),
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.extendedColor.hintColor
                )
            }
        }
    }
}

@Composable
fun AnimatedUsageBarChartCanvas(
    data: Map<Int, Double>,
    modifier: Modifier = Modifier,
    barColor: Color = MaterialTheme.extendedColor.primaryColor,
    cornerRadius: Dp = 10.dp,
    showEmpty: Boolean = false,
    hourBinSize: Int = 2,
    isWeekly: Boolean,
) {
    val isEmpty = showEmpty || data.isEmpty() || data.values.all { it <= 0.0 }

    val binned = remember(data, hourBinSize, isWeekly) {
        if (isWeekly) emptyMap()
        else buildMap {
            data.forEach { (hour, minutes) ->
                val binStart = (hour / hourBinSize) * hourBinSize
                put(binStart, (get(binStart) ?: 0.0) + minutes)
            }
        }
    }

    val maxMinutes = remember(data, binned, isWeekly) {
        val m = (if (isWeekly) data.values.maxOrNull() else binned.values.maxOrNull()) ?: 0.0
        if (m > 0) m else 1.0
    }

    val topHour = remember(maxMinutes, isWeekly) {
        val raw = ceil(maxMinutes / 60.0).toInt().coerceAtLeast(1)
        if (isWeekly) weeklyTopHourCapped(raw) else raw
    }
    val maxAdjusted = remember(topHour) { topHour * 60.0 }

    val anim = remember { Animatable(0f) }

    val animKey = remember(isWeekly, data, binned) {
        val src = if (isWeekly) data else binned
        src.entries.sortedBy { it.key }.map { it.key to it.value }
    }

    LaunchedEffect(animKey, isEmpty) {
        if (isEmpty) anim.snapTo(1f)
        else {
            anim.snapTo(0f)
            anim.animateTo(1f, tween(900, easing = FastOutSlowInEasing))
        }
    }

    val hint = MaterialTheme.extendedColor.hintColor.copy(alpha = 0.3f)
    val dash = remember { PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f) }

    Canvas(modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // grid
        val stroke = 1.dp.toPx()
        listOf(0f, h / 3f, 2f * h / 3f, h).forEach { y ->
            val yy = if (y == h) h - 0.5f else y
            drawLine(
                color = hint,
                start = Offset(0f, yy),
                end = Offset(w, yy),
                strokeWidth = stroke,
                pathEffect = dash
            )
        }

        val gap = 2.dp.toPx()
        val tickH = 3.dp.toPx()
        val tickY = h - tickH - gap
        val tickCorner = CornerRadius(6.dp.toPx(), 6.dp.toPx())
        val barCorner = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx())
        val minBarH = 4.dp.toPx()

        fun drawBar(index: Int, count: Int, value: Double) {
            val slotW = w / count
            val barW = (slotW * 0.60f).coerceAtLeast(6.dp.toPx())
            val cx = (index + 0.5f) * slotW
            val left = (cx - barW / 2f).coerceIn(0f, w - barW)

            if (value <= 0.0) {
                drawRoundRect(hint, Offset(left, tickY), Size(barW, tickH), tickCorner)
                return
            }

            val p = (value / maxAdjusted).toFloat().coerceIn(0f, 1f)
            val rawH = (h * p * anim.value) - gap
            val barH = rawH.coerceAtLeast(minBarH)
            val top = h - barH - gap

            drawRoundRect(barColor, Offset(left, top), Size(barW, barH), barCorner)
        }

        if (isWeekly) {
            repeat(7) { i -> drawBar(i, 7, data[i] ?: 0.0) }
        } else {
            val bins = (0..22 step hourBinSize).toList()
            bins.forEachIndexed { idx, binStart -> drawBar(idx, bins.size, binned[binStart] ?: 0.0) }
        }
    }
}

fun normalizeWeeklyKeys(raw: Map<Int, Double>): Map<Int, Double> {
    if (raw.isEmpty()) return emptyMap()

    val keys = raw.keys
    return when {

        keys.minOrNull() == 1 && keys.maxOrNull() == 7 ->
            raw.mapKeys { (k, _) -> k - 1 }

        keys.minOrNull() == 0 && keys.maxOrNull() == 6 ->
            raw else -> raw
    }
}

@Composable
private fun HourlyChart(
    data: Map<Int, Double>,
    barColor: Color,
    isEmpty: Boolean,
    onBinClick: (startHour: Int, endHour: Int) -> Unit,
) {
    val binStep = 2
    val bins = remember { (0..22 step binStep).toList() }
    val scroll = rememberScrollState()

    var viewportPx by remember { mutableIntStateOf(0) }
    var contentPx by remember { mutableIntStateOf(0) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .onSizeChanged { viewportPx = it.width }
    ) {
        val binWidth = (maxWidth / 8).coerceIn(40.dp, 64.dp)
        val contentWidth = binWidth * bins.size

        Column(Modifier.fillMaxWidth()) {
            Column(Modifier.fillMaxWidth().horizontalScroll(scroll)) {
                Column(
                    modifier = Modifier
                        .width(contentWidth)
                        .onSizeChanged { contentPx = it.width }
                ) {
                    Box(
                        modifier = Modifier
                            .height(160.dp)
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        AnimatedUsageBarChartCanvas(
                            data = data,
                            isWeekly = false,
                            barColor = barColor,
                            showEmpty = isEmpty,
                            hourBinSize = binStep
                        )

                        HourlyClickLayer(
                            bins = bins,
                            binWidth = binWidth,
                            onBinClick = { start ->
                                val end = (start + binStep - 1).coerceAtMost(23)
                                onBinClick(start, end)
                            }
                        )
                    }

                    Row(Modifier.fillMaxWidth()) {
                        bins.forEach { startHour ->
                            Box(Modifier.width(binWidth), contentAlignment = Alignment.Center) {
                                CustomText(
                                    text = startHour.toString().padStart(2, '0'),
                                    fontSize = 10.sp,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.extendedColor.hintColor
                                )
                            }
                        }
                    }
                }
            }

            HorizontalScrollIndicator(
                scrollState = scroll,
                viewportPx = viewportPx,
                contentPx = contentPx,
                modifier = Modifier.fillMaxWidth().padding(top = 6.dp)
            )
        }
    }
}

@Composable
private fun HorizontalScrollIndicator(
    scrollState: ScrollState,
    viewportPx: Int,
    contentPx: Int,
    modifier: Modifier = Modifier,
    height: Dp = 3.dp,
    minThumb: Dp = 28.dp,
    trackColor: Color = PrimaryColor.copy(alpha = 0.2f),
    thumbColor: Color = PrimaryColor,
) {
    val maxScroll = scrollState.maxValue
    if (viewportPx <= 0 || contentPx <= 0 || maxScroll <= 0) return

    Canvas(modifier.height(height)) {
        val w = size.width
        val h = size.height

        drawRoundRect(trackColor, Offset.Zero, Size(w, h), CornerRadius(h, h))

        val thumbW = ((viewportPx.toFloat() / contentPx.toFloat()) * w)
            .coerceAtLeast(minThumb.toPx())
            .coerceAtMost(w)

        val p = (scrollState.value.toFloat() / maxScroll.toFloat()).coerceIn(0f, 1f)
        val x = (w - thumbW) * p

        drawRoundRect(thumbColor, Offset(x, 0f), Size(thumbW, h), CornerRadius(h, h))
    }
}

@Composable
private fun HourlyClickLayer(
    bins: List<Int>,
    binWidth: Dp,
    onBinClick: (startHour: Int) -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    Row(Modifier.fillMaxSize()) {
        repeat(bins.size) { i ->
            Box(
                Modifier
                    .width(binWidth)
                    .fillMaxHeight()
                    .clickable(indication = null, interactionSource = interaction) { onBinClick(bins[i]) }
            )
        }
    }
}

@Composable
private fun WeeklyClickLayer(onDayClick: (Int) -> Unit) {
    val interaction = remember { MutableInteractionSource() }
    Row(Modifier.fillMaxSize()) {
        repeat(7) { i ->
            Box(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable(indication = null, interactionSource = interaction) { onDayClick(i) }
            )
        }
    }
}
private const val WEEKLY_MAX_HOUR = 300
private val weeklySteps = listOf(
    1, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24,
    30, 36, 48, 60, 72, 96, 120, 144, 168, 240, 300
)
private fun weeklyTopHourCapped(maxHour: Int): Int {
    val capped = maxHour.coerceAtMost(WEEKLY_MAX_HOUR)
    return weeklySteps.firstOrNull { it >= capped } ?: WEEKLY_MAX_HOUR
}

@Preview
@Composable
private fun Pre() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        UsageBarChart(
            data = mapOf(
                0 to 12.0,
                1 to 34.0,
                2 to 5.5
            ),
            isWeekly = true
        )
    }
}