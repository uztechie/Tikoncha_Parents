package org.example.project.presentation.map

import androidx.compose.runtime.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalDensity
import kotlin.math.max

@Composable
fun rememberPlacemarkSizeForMapMarker(
    title: String,
    // MapMarker dagi bilan mos:
    iconSize: Dp = 20.dp,
    padding: Dp = 8.dp,
    pointerHeight: Dp = 10.dp,
    spacingBetweenIconAndText: Dp = 4.dp,
    textSizeSp: Int = 14,
    // Chegaralar:
    minWidth: Dp = 72.dp,
    maxWidth: Dp = 280.dp,
    textMaxLines: Int = Int.MAX_VALUE,
    // Agar MapMarker’da boshqa text style ishlatsangiz, shu yerda moslang:
    textStyle: TextStyle = TextStyle(fontSize = textSizeSp.sp)
): DpSize {
    val measurer = rememberTextMeasurer()
    val density = LocalDensity.current

    val pxIcon  = with(density) { iconSize.roundToPx() }
    val pxPadX  = with(density) { (padding * 2).roundToPx() }
    val pxPadY  = with(density) { (padding * 2).roundToPx() }
    val pxPtrH  = with(density) { pointerHeight.roundToPx() }
    val pxGap   = with(density) { spacingBetweenIconAndText.roundToPx() }
    val pxMinW  = with(density) { minWidth.roundToPx() }
    val pxMaxW  = with(density) { maxWidth.roundToPx() }

    // 1) Dastlab cheklanmagan kenglikda matn o'lchami
    val first = measurer.measure(
        text = AnnotatedString(title),
        style = textStyle,
        maxLines = textMaxLines,
        constraints = Constraints(maxWidth = Int.MAX_VALUE)
    )
    val rawTextW = first.size.width

    // 2) Ikonka + gap + matn + padding asosida "raw" kenglik
    val rawW = pxIcon + pxGap + rawTextW + pxPadX

    // 3) Kenglikni chegaralash
    val finalW = rawW.coerceIn(pxMinW, pxMaxW)

    // 4) Matnni shu kenglikka moslab qayta o'lchash
    val availableTextW = (finalW - pxPadX - pxIcon - pxGap).coerceAtLeast(0)
    val second = measurer.measure(
        text = AnnotatedString(title),
        style = textStyle,
        maxLines = textMaxLines,
        constraints = Constraints(maxWidth = availableTextW)
    )
    val textH = second.size.height

    // 5) Balandlik: max(icon, text) + vert pad + pointer
    val finalH = max(pxIcon, textH) + pxPadY + pxPtrH

    return with(density) { DpSize(finalW.toDp(), finalH.toDp()) }
}
