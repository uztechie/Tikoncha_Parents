@file:OptIn(ExperimentalTime::class)

package uz.tikoncha_parent.presentation.base


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.bed_sleeping
import tikoncha_parents.composeapp.generated.resources.dot
import tikoncha_parents.composeapp.generated.resources.timer
import uz.tikoncha_parent.ui.theme.AppColors
import kotlin.math.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


@Immutable
data class TimeRangePickerColors(
    val trackColor: Color,           // Ellipse 1: #E5E5E6
    val innerRingColor: Color,       // Ellipse 4 stroke: #CECFD2
    val outerDashedColor: Color,     // Tashqi dashed (active arc ichida): #FFFFFF
    val innerDashedColor: Color,     // Ichki dashed (raqamlar halqasida): #CECFD2
    val activeColor: Color,          // Ellipse 2 + icon: #C3955B (icon/accent-primary)
    val labelColor: Color,           // 0/6/12/18: #22262F (text/primary)
    val currentTimeColor: Color      // hozirgi vaqt nuqtasi
)

object TimeRangePickerDefaults {

    @Composable
    fun colors(
        trackColor: Color = AppColors.border.secondary,
        innerRingColor: Color = AppColors.border.primary,
        outerDashedColor: Color = AppColors.border.tertiary,
        innerDashedColor: Color = AppColors.border.primary,
        activeColor: Color = AppColors.border.accentEmphasis,
        labelColor: Color = AppColors.text.primary,
        currentTimeColor: Color = Color(0xFF2F6BFF)
    ): TimeRangePickerColors = TimeRangePickerColors(
        trackColor = trackColor,
        innerRingColor = innerRingColor,
        outerDashedColor = outerDashedColor,
        innerDashedColor = innerDashedColor,
        activeColor = activeColor,
        labelColor = labelColor,
        currentTimeColor = currentTimeColor
    )
}

// -------- LocalTime overload --------

@Composable
fun TimeRangePicker(
    start: LocalTime,
    end: LocalTime,
    onTimeChange: (start: LocalTime, end: LocalTime) -> Unit,
    modifier: Modifier = Modifier,
    reverse: Boolean = false,
    colors: TimeRangePickerColors = TimeRangePickerDefaults.colors(),
    animationDurationMs: Int = 350,
    stepMinutes: Int = 1,  // ← qo'shildi
    startIcon: (@Composable () -> Unit)? = null,
    endIcon: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    centerContent: @Composable () -> Unit = {}
) {
    TimeRangePicker(
        startMinutes = start.hour * 60 + start.minute,
        endMinutes = end.hour * 60 + end.minute,
        onTimeChange = { s, e ->
            onTimeChange(
                LocalTime(hour = s / 60, minute = s % 60),
                LocalTime(hour = e / 60, minute = e % 60)
            )
        },
        modifier = modifier,
        inverted = reverse,
        colors = colors,
        animationDurationMs = animationDurationMs,
        stepMinutes = stepMinutes,
        startIcon = startIcon,
        endIcon = endIcon,
        onClick = onClick,
        centerContent = centerContent
    )
}



/**
 * 24 soatlik doiraviy vaqt oralig'i tanlagich.
 *
 * Kenglik `modifier` dan, balandlik `aspectRatio(1f)` orqali avtomatik bir xil.
 *
 * Default qiymatlar Figma spec'iga aniq mos (300dp bazaviy):
 * - Track stroke: 36dp
 * - Active arc stroke: 31dp
 * - Inner dashed ring: 15.83% inset = markazdan 34% masofada
 * - Outer dashed ring: 5.33% inset = 16dp chegaradan
 * - Icons: start 35dp, end 31dp
 * - Center (Time Crystal): 100dp
 * - Label font: 14sp (Golos Text Medium)
 *
 * MUHIM: inner dashed ring va raqamlar AYNI RADIUSDA turadi —
 * raqamlar dashed chiziq ustida markazlashgan.
 */
@Composable
fun TimeRangePicker(
    startMinutes: Int,
    endMinutes: Int,
    onTimeChange: (startMinutes: Int, endMinutes: Int) -> Unit,
    modifier: Modifier = Modifier,
    inverted: Boolean = false,
    colors: TimeRangePickerColors = TimeRangePickerDefaults.colors(),
    animationDurationMs: Int = 350,
    // --- O'lchamlar (Figma qiymatlari) ---
    trackStrokeWidth: Dp = 36.dp,
    activeStrokeWidth: Dp = 31.dp,
    dashStrokeWidth: Dp = 1.5.dp,
    labelFontSize: TextUnit = 14.sp,
    activeArcCap: StrokeCap = StrokeCap.Round,
    iconStartSize: Dp = 35.dp,
    iconEndSize: Dp = 31.dp,
    // Protsent qiymatlar — ichki halqa chegaradan 15.83% masofada
    innerRingGapFromTrack: Dp = 18.dp,
    // Tashqi dashed chegaradan 16dp
    centerSize: Dp = 100.dp,
    iconGap: Dp = 10.dp,
    stepMinutes: Int = 1,  // 1..60
    // --- Slotlar ---
    startIcon: (@Composable () -> Unit)? = null,
    endIcon: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    centerContent: @Composable () -> Unit = {}
) {
    BoxWithConstraints(
        modifier = modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        val density = LocalDensity.current
        val sizePx = with(density) { maxWidth.toPx() }

        val LABEL_GAP_MINUTES = 100f

        // --- Dp → px ---
        val trackStrokePx = with(density) { trackStrokeWidth.toPx() }
        val activeStrokePx = with(density) { activeStrokeWidth.toPx() }
        val dashStrokePx = with(density) { dashStrokeWidth.toPx() }
        val iconStartPx = with(density) { iconStartSize.toPx() }
        val iconEndPx = with(density) { iconEndSize.toPx() }
        val iconGapPx = with(density) { iconGap.toPx() }



        // ... Dp → px konversiyalari ...
        val innerRingGapPx = with(density) { innerRingGapFromTrack.toPx() }  // ← YANGI

        // Radiuslar
        val arcRadius = (sizePx - trackStrokePx) / 2f
        val trackInnerEdge = arcRadius - trackStrokePx / 2f
        val innerRingRadius = trackInnerEdge - innerRingGapPx
        val labelRadiusPx = innerRingRadius



        // Radiuslar

        // --- Animatsiya state ---
        val startAnim = remember { Animatable(startMinutes.toFloat()) }
        val endAnim = remember { Animatable(endMinutes.toFloat()) }
        val scope = rememberCoroutineScope()
        var isDragging by remember { mutableStateOf(false) }

        val dashedPathEffect = PathEffect.dashPathEffect(
            floatArrayOf(with(density) { 5.dp.toPx() }, with(density) { 5.dp.toPx() }), 0f
        )

        LaunchedEffect(startMinutes) {
            if (!isDragging) {
                startAnim.animateTo(
                    targetValue = startMinutes.toFloat(),
                    animationSpec = tween(animationDurationMs, easing = FastOutSlowInEasing)
                )
            }
        }
        LaunchedEffect(endMinutes) {
            if (!isDragging) {
                endAnim.animateTo(
                    targetValue = endMinutes.toFloat(),
                    animationSpec = tween(animationDurationMs, easing = FastOutSlowInEasing)
                )
            }
        }

        val currentTimeMinutes by produceState(initialValue = currentMinutesOfDay()) {
            while (true) {
                value = currentMinutesOfDay()
                delay(30_000L)
            }
        }

        val textMeasurer = rememberTextMeasurer()
        val labelStyle = TextStyle(
            color = colors.labelColor,
            fontSize = labelFontSize,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        val center = Offset(sizePx / 2f, sizePx / 2f)
        var activeHandle by remember { mutableStateOf<Handle?>(null) }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { pos ->
                            val handle = detectHandleHit(
                                pos = pos,
                                center = center,
                                radius = arcRadius,
                                startHandleRadiusPx = iconStartPx / 2f,
                                endHandleRadiusPx = iconEndPx / 2f,
                                startMin = startAnim.value,
                                endMin = endAnim.value
                            )
                            if (handle == null) onClick?.invoke()
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { pos ->
                            activeHandle = detectHandleHit(
                                pos = pos,
                                center = center,
                                radius = arcRadius,
                                startHandleRadiusPx = iconStartPx,
                                endHandleRadiusPx = iconEndPx,
                                startMin = startAnim.value,
                                endMin = endAnim.value
                            )
                            if (activeHandle != null) isDragging = true
                        },
                        onDragEnd = {
                            activeHandle = null
                            isDragging = false
                            onTimeChange(
                                snapToStep(startAnim.value.roundToInt(), stepMinutes),
                                snapToStep(endAnim.value.roundToInt(), stepMinutes)
                            )
                        },
                        onDragCancel = {
                            activeHandle = null
                            isDragging = false
                        },
                        onDrag = { change, _ ->
                            val h = activeHandle ?: return@detectDragGestures
                            change.consume()
                            val rawMin = positionToMinutes(change.position, center)
                            val newMin = snapToStep(rawMin, stepMinutes)
                            scope.launch {
                                when (h) {
                                    Handle.Start -> startAnim.snapTo(newMin.toFloat())
                                    Handle.End -> endAnim.snapTo(newMin.toFloat())
                                }
                                onTimeChange(
                                    startAnim.value.roundToInt().mod(1440),
                                    endAnim.value.roundToInt().mod(1440)
                                )
                            }
                        }
                    )
                }
        ) {
            val startMin = startAnim.value
            val endMin = endAnim.value

            // 1) Track — to'liq halqa
            drawCircle(
                color = colors.trackColor,
                radius = arcRadius,
                center = center,
                style = Stroke(width = trackStrokePx)
            )

            val half = LABEL_GAP_MINUTES / 2f  // 50

            // 2) Ichki dashed — 4 ta segment raqamlar ORASIDA
            // (raqamlar ustida dashed yo'q, faqat orasida)
            drawInnerArcRangeDashed(
                startMin = 0f + half,
                endMin = 360f - half,
                center = center,
                radius = innerRingRadius,
                strokeWidth = dashStrokePx,
                color = colors.innerDashedColor,
                strokeCap = StrokeCap.Butt
            )
            drawInnerArcRangeDashed(
                startMin = 360f + half,
                endMin = 720f - half,
                center = center,
                radius = innerRingRadius,
                strokeWidth = dashStrokePx,
                color = colors.innerDashedColor,
                strokeCap = StrokeCap.Butt
            )
            drawInnerArcRangeDashed(
                startMin = 720f + half,
                endMin = 1080f - half,
                center = center,
                radius = innerRingRadius,
                strokeWidth = dashStrokePx,
                color = colors.innerDashedColor,
                strokeCap = StrokeCap.Butt
            )
            drawInnerArcRangeDashed(
                startMin = 1080f + half,
                endMin = 1440f - half,
                center = center,
                radius = innerRingRadius,
                strokeWidth = dashStrokePx,
                color = colors.innerDashedColor,
                strokeCap = StrokeCap.Butt
            )

            // 3) Active arc + tashqi dashed
            val iconGapMinutes = (iconGapPx / (2f * PI.toFloat() * arcRadius)) * 1440f

            if (!inverted) {
                drawActiveArcRange(
                    startMin = startMin,
                    endMin = endMin,
                    center = center,
                    radius = arcRadius,
                    strokeWidth = activeStrokePx,
                    color = colors.activeColor,
                    strokeCap = activeArcCap
                )
                drawArcRangeDashed(
                    startMin = startMin + iconGapMinutes,
                    endMin = endMin - iconGapMinutes,
                    center = center,
                    radius = arcRadius,
                    strokeWidth = dashStrokePx,
                    color = colors.outerDashedColor,
                    pathEffect = dashedPathEffect,
                    strokeCap = StrokeCap.Butt
                )
            } else {
                drawActiveArcRange(
                    startMin = endMin,
                    endMin = startMin,
                    center = center,
                    radius = arcRadius,
                    strokeWidth = activeStrokePx,
                    color = colors.activeColor,
                    strokeCap = activeArcCap
                )
                drawArcRangeDashed(
                    startMin = endMin + iconGapMinutes,
                    endMin = startMin - iconGapMinutes,
                    center = center,
                    radius = arcRadius,
                    strokeWidth = dashStrokePx,
                    color = colors.outerDashedColor,
                    pathEffect = dashedPathEffect,
                    strokeCap = StrokeCap.Butt
                )
            }

            // 4) 0/6/12/18 raqamlari — dashed ring bilan AYNI RADIUSDA
            // (labelRadiusPx = innerRingRadius — matn markazi dashed chizig'i ustida)
            val labels = listOf(0 to "0", 6 to "6", 12 to "12", 18 to "18")
            labels.forEach { (hour, text) ->
                val angle = minutesToAngleRad(hour * 60)
                val p = Offset(
                    center.x + labelRadiusPx * cos(angle),
                    center.y + labelRadiusPx * sin(angle)
                )
                val layout = textMeasurer.measure(text, labelStyle)
                drawText(
                    textLayoutResult = layout,
                    topLeft = Offset(
                        p.x - layout.size.width / 2f,
                        p.y - layout.size.height / 2f
                    )
                )
            }
        }

        // 5) Handle iconlari
        HandleIconsOverlay(
            sizePx = sizePx,
            arcRadius = arcRadius,
            startHandleRadiusPx = iconStartPx / 2f,
            endHandleRadiusPx = iconEndPx / 2f,
            startMinutes = startAnim.value,
            endMinutes = endAnim.value,
            startIcon = startIcon,
            endIcon = endIcon
        )

        // 6) Markaziy slot
        Box(
            modifier = Modifier.size(centerSize),
            contentAlignment = Alignment.Center
        ) {
            centerContent()
        }
    }
}

/**
 * LocalTime bilan ishlash uchun overload.
 */
@Composable
fun TimeRangePicker(
    start: LocalTime,
    end: LocalTime,
    onTimeChange: (start: LocalTime, end: LocalTime) -> Unit,
    modifier: Modifier = Modifier,
    inverted: Boolean = false,
    colors: TimeRangePickerColors = TimeRangePickerDefaults.colors(),
    animationDurationMs: Int = 350,
    trackStrokeWidth: Dp = 36.dp,
    activeStrokeWidth: Dp = 31.dp,
    dashStrokeWidth: Dp = 1.5.dp,
    labelFontSize: TextUnit = 14.sp,
    activeArcCap: StrokeCap = StrokeCap.Round,
    iconStartSize: Dp = 35.dp,
    iconEndSize: Dp = 31.dp,
    innerRingGapFromTrack: Dp = 18.dp,
    centerSize: Dp = 100.dp,
    iconGap: Dp = 10.dp,
    startIcon: (@Composable () -> Unit)? = null,
    endIcon: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    centerContent: @Composable () -> Unit = {}
) {
    TimeRangePicker(
        startMinutes = start.hour * 60 + start.minute,
        endMinutes = end.hour * 60 + end.minute,
        onTimeChange = { s, e ->
            onTimeChange(
                LocalTime(hour = s / 60, minute = s % 60),
                LocalTime(hour = e / 60, minute = e % 60)
            )
        },
        modifier = modifier,
        inverted = inverted,
        colors = colors,
        animationDurationMs = animationDurationMs,
        trackStrokeWidth = trackStrokeWidth,
        activeStrokeWidth = activeStrokeWidth,
        dashStrokeWidth = dashStrokeWidth,
        labelFontSize = labelFontSize,
        activeArcCap = activeArcCap,
        iconStartSize = iconStartSize,
        iconEndSize = iconEndSize,
        innerRingGapFromTrack = innerRingGapFromTrack,
        centerSize = centerSize,
        iconGap = iconGap,
        startIcon = startIcon,
        endIcon = endIcon,
        onClick = onClick,
        centerContent = centerContent
    )
}

// ---------- Yordamchi ----------

private enum class Handle { Start, End }

private fun minutesToAngleRad(minutes: Number): Float {
    val m = minutes.toFloat().mod(1440f)
    val fraction = m / 1440f
    return (fraction * 2f * PI.toFloat()) - (PI.toFloat() / 2f)
}

private fun pointAtMin(minutes: Float, center: Offset, radius: Float): Offset {
    val a = minutesToAngleRad(minutes)
    return Offset(center.x + radius * cos(a), center.y + radius * sin(a))
}

private fun positionToMinutes(pos: Offset, center: Offset): Int {
    val dx = pos.x - center.x
    val dy = pos.y - center.y
    var angle = atan2(dy, dx) + PI.toFloat() / 2f
    if (angle < 0) angle += 2f * PI.toFloat()
    val fraction = angle / (2f * PI.toFloat())
    return (fraction * 1440f).roundToInt().mod(1440)
}

private fun detectHandleHit(
    pos: Offset,
    center: Offset,
    radius: Float,
    startHandleRadiusPx: Float,
    endHandleRadiusPx: Float,
    startMin: Float,
    endMin: Float
): Handle? {
    val startPos = pointAtMin(startMin, center, radius)
    val endPos = pointAtMin(endMin, center, radius)
    val dStart = (pos - startPos).getDistance()
    val dEnd = (pos - endPos).getDistance()
    val startHit = dStart <= startHandleRadiusPx
    val endHit = dEnd <= endHandleRadiusPx
    return when {
        startHit && (!endHit || dStart <= dEnd) -> Handle.Start
        endHit -> Handle.End
        else -> null
    }
}

private fun DrawScope.drawActiveArcRange(
    startMin: Float,
    endMin: Float,
    center: Offset,
    radius: Float,
    strokeWidth: Float,
    color: Color,
    strokeCap: StrokeCap = StrokeCap.Round
) {
    val startDeg = (startMin / 1440f) * 360f - 90f
    val endDeg = (endMin / 1440f) * 360f - 90f

    var sweep = endDeg - startDeg
    if (sweep <= 0f) sweep += 360f
    if (sweep > 360f) sweep -= 360f

    val arcSize = Size(radius * 2f, radius * 2f)
    val topLeft = Offset(center.x - radius, center.y - radius)

    drawArc(
        color = color,
        startAngle = startDeg,
        sweepAngle = sweep,
        useCenter = false,
        topLeft = topLeft,
        size = arcSize,
        style = Stroke(width = strokeWidth, cap = strokeCap)
    )
}

private fun DrawScope.drawArcRangeDashed(
    startMin: Float,
    endMin: Float,
    center: Offset,
    radius: Float,
    strokeWidth: Float,
    color: Color,
    pathEffect: PathEffect? = null,
    strokeCap: StrokeCap = StrokeCap.Round
) {
    val startDeg = (startMin / 1440f) * 360f - 90f
    val endDeg = (endMin / 1440f) * 360f - 90f

    var sweep = endDeg - startDeg
    if (sweep <= 0f) sweep += 360f
    if (sweep > 360f) sweep -= 360f

    val arcSize = Size(radius * 2f, radius * 2f)
    val topLeft = Offset(center.x - radius, center.y - radius)

    drawArc(
        color = color,
        startAngle = startDeg,
        sweepAngle = sweep,
        useCenter = false,
        topLeft = topLeft,
        size = arcSize,
        style = Stroke(
            width = strokeWidth,
            cap = strokeCap,
            pathEffect = pathEffect
        )
    )
}

private fun DrawScope.drawInnerArcRangeDashed(
    startMin: Float,
    endMin: Float,
    center: Offset,
    radius: Float,
    strokeWidth: Float,
    color: Color,
    strokeCap: StrokeCap = StrokeCap.Butt,
    dashCount: Int = 5
) {
    val startDeg = (startMin / 1440f) * 360f - 90f
    val endDeg = (endMin / 1440f) * 360f - 90f

    var sweep = endDeg - startDeg
    if (sweep <= 0f) sweep += 360f
    if (sweep > 360f) sweep -= 360f
    if (sweep <= 0.01f) return

    val arcLengthPx = radius * (sweep * PI.toFloat() / 180f)
    val totalSegments = (dashCount * 2 - 1).coerceAtLeast(1)
    val segmentPx = arcLengthPx / totalSegments

    val pathEffect = PathEffect.dashPathEffect(
        floatArrayOf(segmentPx, segmentPx), 0f
    )

    val arcSize = Size(radius * 2f, radius * 2f)
    val topLeft = Offset(center.x - radius, center.y - radius)

    drawArc(
        color = color,
        startAngle = startDeg,
        sweepAngle = sweep,
        useCenter = false,
        topLeft = topLeft,
        size = arcSize,
        style = Stroke(
            width = strokeWidth,
            cap = strokeCap,
            pathEffect = pathEffect
        )
    )
}

@Composable
private fun HandleIconsOverlay(
    sizePx: Float,
    arcRadius: Float,
    startHandleRadiusPx: Float,
    endHandleRadiusPx: Float,
    startMinutes: Float,
    endMinutes: Float,
    startIcon: (@Composable () -> Unit)?,
    endIcon: (@Composable () -> Unit)?
) {
    val density = LocalDensity.current

    val startAngle = minutesToAngleRad(startMinutes)
    val startPx = sizePx / 2f + arcRadius * cos(startAngle)
    val startPy = sizePx / 2f + arcRadius * sin(startAngle)

    val endAngle = minutesToAngleRad(endMinutes)
    val endPx = sizePx / 2f + arcRadius * cos(endAngle)
    val endPy = sizePx / 2f + arcRadius * sin(endAngle)

    val dx = endPx - startPx
    val dy = endPy - startPy
    val distance = sqrt(dx * dx + dy * dy)

    val overlapThreshold = (startHandleRadiusPx + endHandleRadiusPx) * 0.5f
    val isOverlapping = distance < overlapThreshold

    Box(modifier = Modifier.fillMaxSize()) {
        if (startIcon != null && !isOverlapping) {
            val sizeD = with(density) { (startHandleRadiusPx * 2f).toDp() }
            Box(
                modifier = Modifier
                    .absoluteOffsetPx(
                        startPx - startHandleRadiusPx,
                        startPy - startHandleRadiusPx
                    )
                    .size(sizeD),
                contentAlignment = Alignment.Center
            ) {
                startIcon()
            }
        }

        if (endIcon != null) {
            val sizeD = with(density) { (endHandleRadiusPx * 2f).toDp() }
            Box(
                modifier = Modifier
                    .absoluteOffsetPx(
                        endPx - endHandleRadiusPx,
                        endPy - endHandleRadiusPx
                    )
                    .size(sizeD),
                contentAlignment = Alignment.Center
            ) {
                endIcon()
            }
        }
    }
}

private fun Modifier.absoluteOffsetPx(x: Float, y: Float): Modifier =
    this.layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.width, placeable.height) {
            placeable.place(x.roundToInt(), y.roundToInt())
        }
    }

private fun currentMinutesOfDay(): Int {
    val now = Clock.System.now()
    val tz = TimeZone.currentSystemDefault()
    val local = now.toLocalDateTime(tz)
    return local.hour * 60 + local.minute
}

private fun snapToStep(minutes: Int, stepMinutes: Int): Int {
    val step = stepMinutes.coerceIn(1, 60)
    if (step == 1) return minutes.mod(1440)
    val snapped = ((minutes.toFloat() / step).roundToInt() * step)
    return snapped.mod(1440)
}








// ---------- Preview ----------

@Preview
@Composable
private fun TimeRangePickerPreview_Default() {
    PreviewSurface {
        TimeRangePicker(
            start = LocalTime(8, 0),
            end = LocalTime(15, 0),
            onTimeChange = { _, _ -> },
            modifier = Modifier.fillMaxWidth(1f),
            startIcon = {
                Icon(
                    painter = painterResource(Res.drawable.dot),
                    contentDescription = "",
                    modifier = Modifier.size(10.dp),
                    tint = AppColors.icon.inverse
                )
            },
            endIcon = {
                Icon(
                    painter = painterResource(Res.drawable.dot),
                    contentDescription = "",
                    modifier = Modifier.size(10.dp),
                    tint = AppColors.icon.inverse
                )
            },
            centerContent = { PreviewCrystal() }
        )
    }
}

@Composable
private fun PreviewSurface(
    bg: Color = Color(0xFFF9F3E9),
    content: @Composable () -> Unit
) {
    Surface(color = bg) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Composable
private fun PreviewCrystal() {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(Color(0xFF5EC3E0).copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
    ) {
        Text("⏳", fontSize = 56.sp)
    }
}