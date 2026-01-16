package uz.tikoncha_parent.presentation.statistic

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import uz.tikoncha_parent.presentation.domain.model.UsagePeriod
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.SmallTextSize
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.date_selection_arrow_left
import tikoncha_parents.composeapp.generated.resources.date_selection_arrow_right
import tikoncha_parents.composeapp.generated.resources.haftalik
import tikoncha_parents.composeapp.generated.resources.kunlik
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.ui.LargeTextSize
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun DateSelectorSlider(
    type: DateSelectionType,
    averageTimeText: String,
    modifier: Modifier = Modifier,
    periodsDate: List<UsagePeriod>,
    onDateSelected:(UsagePeriod) -> Unit,
    onLastItemSelected: (Boolean) -> Unit = {}
) {
    if (periodsDate.isEmpty()) return

    val pagerState = rememberPagerState(initialPage = periodsDate.lastIndex){periodsDate.size}

    var didInit by remember(type) { mutableStateOf(false) }
    var lastSize by remember(type) { mutableIntStateOf(-1) }

    LaunchedEffect(periodsDate.size, type) {
        // faqat list uzunligi o'zgarsa yoki birinchi marta
        val size = periodsDate.size
        if (size <= 0) return@LaunchedEffect

        if (!didInit || lastSize != size) {
            didInit = true
            lastSize = size
            pagerState.scrollToPage(periodsDate.lastIndex) // animate ham bo'ladi, lekin scroll barqarorroq
        } else {
            // agar hozirgi page range’dan chiqib ketsa
            val cur = pagerState.currentPage
            if (cur > periodsDate.lastIndex) {
                pagerState.scrollToPage(periodsDate.lastIndex)
            }
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        val index = pagerState.currentPage
        onDateSelected(periodsDate[index])
        onLastItemSelected(index == periodsDate.lastIndex)
    }

    val selectionType = if (type == DateSelectionType.DAY){
        stringResource(Res.string.kunlik)
    } else{
        stringResource(Res.string.haftalik)
    }

    val textSize = SmallTextSize

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PagerDots(
            slotWidth = 6.dp,
            total = periodsDate.size,
            maxDots = periodsDate.size,
            current = pagerState.currentPage,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp)
        )

        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
        ) {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = true,
                beyondViewportPageCount = 1,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val date = periodsDate[page].startDate
                    val dateText = "${date.day.toString().padStart(2, '0')}." +
                            "${date.month.number.toString().padStart(2, '0')}." +
                            date.year

                    CustomText(
                        text = "$selectionType $dateText",
                        fontSize = textSize,
                    )

                    CustomText(
                        text = averageTimeText,
                        color = MaterialTheme.extendedColor.primaryColor,
                        fontSize = LargeTextSize,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun PagerDots(
    total: Int,
    current: Int,
    modifier: Modifier = Modifier,
    maxDots: Int = 9,
    dot: Dp = 5.dp,
    selectedDot: Dp = 7.dp,
    slotWidth: Dp = 6.dp,
    inactiveAlpha: Float = 0.35f
) {
    if (total <= 1) return

    val safeCurrent = remember(total, current) { current.coerceIn(0, total - 1) }
    val visibleCount = remember(total, maxDots) { minOf(total, maxDots) }

    // window start (center current)
    val start = remember(total, maxDots, safeCurrent) {
        if (total <= maxDots) 0
        else {
            val half = maxDots / 2
            val raw = safeCurrent - half
            when {
                raw < 0 -> 0
                raw > total - maxDots -> total - maxDots
                else -> raw
            }
        }
    }

    // bitta transition (yengilroq)
    val t = updateTransition(
        targetState = safeCurrent,
        label = "dotsCurrent"
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(visibleCount) { i ->
            val realIndex = start + i

            val size by t.animateDp(label = "size_$realIndex") { selected ->
                if (realIndex == selected) selectedDot else dot
            }
            val alpha by t.animateFloat(label = "alpha_$realIndex") { selected ->
                if (realIndex == selected) 1f else inactiveAlpha
            }

            Box(
                modifier = Modifier
                    .width(slotWidth)
                    .height(selectedDot),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(size)) {
                    drawCircle(
                        color = PrimaryColor.copy(alpha = alpha),
                        radius = size.toPx() / 2f,
                        center = center
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        DateSelectorSlider(
            type = DateSelectionType.WEEK,
            periodsDate = listOf(
                UsagePeriod(
                    type = DateSelectionType.DAY,
                    label = "12.04.2023",
                    startDate = LocalDate(2023, 4, 12),
                )
            ),
            onDateSelected = {},
            averageTimeText = ""
        )
    }
}