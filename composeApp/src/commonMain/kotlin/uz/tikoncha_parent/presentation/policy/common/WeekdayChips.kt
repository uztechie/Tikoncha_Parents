package uz.tikoncha_parent.presentation.policy.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.tikoncha_parent.domain.model.WeekDay
import uz.tikoncha_parent.domain.model.weekdayLabel
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

private val MaxChipSize = 44.dp       // Figma nominal
private val MinChipSize = 32.dp       // juda kichik ekran uchun pastki chegara
private val MinGap = 4.dp             // chiplar orasidagi eng kichik bo'shliq
private const val DisabledAlpha = 0.4f

/**
 * Hafta kunlari chiplar qatori — 7 ta pill chip.
 *
 * Figma: Parent-design, node 40002579:8308 (Weekly)
 * - Nominal size: 44×44 circle
 * - Kichik ekranlarda chip hajmi proportional kamayadi (32dp gacha)
 * - Selected: brand accent fon + inverse matn
 * - Unselected: neytral section fon + primary matn
 * - Disabled: alpha pasaytirilgan va click ignored
 */
@Composable
fun WeekdayChips(
    chips: List<WeekDayChipUi>,
    onToggle: (WeekDay) -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        val chipSize = calculateChipSize(
            availableWidth = maxWidth,
            chipCount = chips.size,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            chips.forEach { chip ->
                WeekdayChip(
                    chip = chip,
                    size = chipSize,
                    onClick = { onToggle(chip.day) },
                )
            }
        }
    }
}

private fun calculateChipSize(availableWidth: Dp, chipCount: Int): Dp {
    if (chipCount <= 0) return MaxChipSize
    val totalGap = MinGap * (chipCount - 1)
    val spaceForChips = availableWidth - totalGap
    val proposed = spaceForChips / chipCount
    return proposed.coerceIn(MinChipSize, MaxChipSize)
}

@Composable
private fun WeekdayChip(
    chip: WeekDayChipUi,
    size: Dp,
    onClick: () -> Unit,
) {
    val backgroundColor = if (chip.selected) {
        AppColors.button.primary
    } else {
        AppColors.section.tertiary
    }

    val textColor = if (chip.selected) {
        AppColors.text.inverse
    } else {
        AppColors.text.primary
    }

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .size(size)
            .alpha(if (chip.enabled) 1f else DisabledAlpha)
            .background(color = backgroundColor, shape = CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = chip.enabled,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = chip.day.weekdayLabel(),
            style = AppTypography.titleSmMedium,
            color = textColor,
            textAlign = TextAlign.Center,
        )
    }
}

// ═════════════════════════════════════════════════════════════
// Preview
// ═════════════════════════════════════════════════════════════

@Preview(widthDp = 412)
@Composable
private fun WeekdayChipsWideLight() {
    TikonchaParentTheme(ThemeMode.LIGHT) {
        WeekdayChipsPreviewContent()
    }
}

@Preview(widthDp = 412)
@Composable
private fun WeekdayChipsWideDark() {
    TikonchaParentTheme(ThemeMode.DARK) {
        WeekdayChipsPreviewContent()
    }
}

@Preview(widthDp = 320)
@Composable
private fun WeekdayChipsNarrowLight() {
    TikonchaParentTheme(ThemeMode.LIGHT) {
        WeekdayChipsPreviewContent()
    }
}

@Composable
private fun WeekdayChipsPreviewContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.bg.secondary)
            .padding(16.dp),
    ) {
        // ── Mixed: bir nechta selected ─────────────
        Text(
            text = "Enabled + mixed",
            style = AppTypography.titleSmMedium,
            color = AppColors.text.secondary,
        )
        Spacer(Modifier.height(8.dp))
        WeekdayChips(
            chips = WeekDay.ordered.map { day ->
                WeekDayChipUi(
                    day = day,
                    selected = day in setOf(WeekDay.MON, WeekDay.WED, WeekDay.FRI),
                    enabled = true,
                )
            },
            onToggle = {},
        )

        Spacer(Modifier.height(24.dp))

        // ── Some disabled (band) ────────────────────
        Text(
            text = "Some disabled (band)",
            style = AppTypography.titleSmMedium,
            color = AppColors.text.secondary,
        )
        Spacer(Modifier.height(8.dp))
        WeekdayChips(
            chips = WeekDay.ordered.map { day ->
                WeekDayChipUi(
                    day = day,
                    selected = day == WeekDay.TUE,
                    enabled = day !in setOf(WeekDay.SAT, WeekDay.SUN),
                )
            },
            onToggle = {},
        )

        Spacer(Modifier.height(24.dp))

        // ── All selected ────────────────────────────
        Text(
            text = "All selected",
            style = AppTypography.titleSmMedium,
            color = AppColors.text.secondary,
        )
        Spacer(Modifier.height(8.dp))
        WeekdayChips(
            chips = WeekDay.ordered.map { day ->
                WeekDayChipUi(
                    day = day,
                    selected = true,
                    enabled = true,
                )
            },
            onToggle = {},
        )

        Spacer(Modifier.height(24.dp))

        // ── All unselected ──────────────────────────
        Text(
            text = "All unselected",
            style = AppTypography.titleSmMedium,
            color = AppColors.text.secondary,
        )
        Spacer(Modifier.height(8.dp))
        WeekdayChips(
            chips = WeekDay.ordered.map { day ->
                WeekDayChipUi(
                    day = day,
                    selected = false,
                    enabled = true,
                )
            },
            onToggle = {},
        )
    }
}