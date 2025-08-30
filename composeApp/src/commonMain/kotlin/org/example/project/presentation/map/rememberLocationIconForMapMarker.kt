package org.example.project.presentation.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.ui.SmallTextSize
import org.jetbrains.compose.resources.painterResource
import ru.sulgik.mapkit.compose.imageProvider
import ru.sulgik.mapkit.compose.user_location.UserLocationConfig
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.person
import kotlin.math.max

@Composable
fun rememberLocationIconForMapMarker(
    title: String,
    // MapMarker paramlariga mos:
    icon: Painter = painterResource(Res.drawable.person),
    backgroundColor: Color = Color(0xFF4CAF50),
    contentColor: Color = Color.White,
    cornerRadius: Dp = 8.dp,
    pointerWidth: Dp = 16.dp,
    pointerHeight: Dp = 10.dp,
    padding: Dp = 8.dp,
    iconSize: Dp = 20.dp,
    textSizeSp: TextUnit = SmallTextSize,
    // Dinamik o‘lcham chegaralari:
    minWidth: Dp = 50.dp,
    maxWidth: Dp = 100.dp,
    textMaxLines: Int = Int.MAX_VALUE, // xohlasangiz 2 qiling
    spacingBetweenIconAndText: Dp = 4.dp
): UserLocationConfig.LocationIcon {
    val measurer = rememberTextMeasurer()
    val density = LocalDensity.current

    // px ga o'tkazamiz
    val paddingX = with(density) { (padding * 2).roundToPx() }
    val paddingY = with(density) { (padding * 2).roundToPx() }
    val pointerHpx = with(density) { pointerHeight.roundToPx() }
    val iconWpx = with(density) { iconSize.roundToPx() }
    val iconHpx = with(density) { iconSize.roundToPx() }
    val spacingPx = with(density) { spacingBetweenIconAndText.roundToPx() }
    val minWpx = with(density) { minWidth.roundToPx() }
    val maxWpx = with(density) { maxWidth.roundToPx() }

    // Matn uslubi (MapMarker dagi bilan mos)
    val textStyle = TextStyle(
        color = contentColor,
        fontSize = textSizeSp
    )

    // 1) Cheklanmagan kenglikda xom o‘lchov (single line bo‘lsa qancha?)
    val first = measurer.measure(
        text = AnnotatedString(title),
        style = textStyle,
        maxLines = textMaxLines,
        constraints = Constraints(maxWidth = Int.MAX_VALUE)
    )
    val rawTextW = first.size.width

    // 2) Kutilayotgan kontent kengligi (icon + gap + text + padding)
    val rawContentW = iconWpx + spacingPx + rawTextW + paddingX
    // 3) Kenglikni [min..max] ga keltiramiz
    val finalWpx = rawContentW.coerceIn(minWpx, maxWpx)

    // 4) Agar matn siqilishi kerak bo‘lsa, max text kengligiga qayta o‘lchaymiz
    val availableTextW = (finalWpx - paddingX - iconWpx - spacingPx).coerceAtLeast(0)
    val second = measurer.measure(
        text = AnnotatedString(title),
        style = textStyle,
        maxLines = textMaxLines,
        constraints = Constraints(maxWidth = availableTextW)
    )
    val textHpx = second.size.height

    // 5) Balandlik: icon yoki text dan kattasini olamiz + vertical padding + pointer
    val contentHpx = max(iconHpx, textHpx) + paddingY + pointerHpx

    val size = with(density) { DpSize(finalWpx.toDp(), contentHpx.toDp()) }

    // 6) Nihoyat icon yaratamiz: imageProvider berilgan o‘lchamda snapshot oladi
    return UserLocationConfig.LocationIcon(
        image = imageProvider(size = size) {
            // Bu yerda endi sizning mavjud MapMarker’ingizni to‘liq ishlatamiz
            MapMarker(
                title = title,
                icon = icon,
                backgroundColor = backgroundColor,
                contentColor = contentColor,
                cornerRadius = cornerRadius,
                pointerWidth = pointerWidth,
                pointerHeight = pointerHeight,
                padding = padding,
                iconSize = iconSize,
                textSizeSp = textSizeSp,
                // fillMaxSize: imageProvider bergan o‘lchamni to‘liq egallasin
                modifier = Modifier.fillMaxSize()
            )
        }
    )
}
