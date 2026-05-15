package uz.tikoncha_parent.presentation.task.create_task

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.coin
import tikoncha_parents.composeapp.generated.resources.ozingiz_qoshing
import tikoncha_parents.composeapp.generated.resources.sizda_mavjud_tangachalar
import tikoncha_parents.composeapp.generated.resources.ta
import tikoncha_parents.composeapp.generated.resources.tangachalar
import tikoncha_parents.composeapp.generated.resources.vazifa_tangalari
import uz.tikoncha_parent.presentation.profile.coins.CoinNumberPicker
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun RewardCard(
    title: String = stringResource(Res.string.tangachalar),
    description: String = stringResource(Res.string.vazifa_tangalari),
    coinOptions: List<Int> = listOf(10, 25, 50, 100, 300, 500),
    state: CreateTaskState,
    onEvent: (CreateTaskEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val available = state.availableCoins
    val total = state.totalCoin
    val remaining = state.remainingCoins
    val chipsTotal = state.selectedChips.sum()
    val maxExtra = (available - chipsTotal).coerceAtLeast(0)
    val isCardEnabled = available > 0

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.modal.primary),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = AppTypography.titleLgSemiBold,
                color = AppColors.text.primary
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${stringResource(Res.string.sizda_mavjud_tangachalar)}:",
                    style = AppTypography.titleSmMedium,
                    color = if (isCardEnabled) AppColors.text.accentEmphasis
                    else AppColors.text.secondary
                )
                Space(8.dp)

                Text(
                    text = "$remaining ${stringResource(Res.string.ta)}",
                    style = AppTypography.titleSmMedium,
                    color = if (isCardEnabled) AppColors.text.accentEmphasis
                    else AppColors.text.secondary
                )
            }

            Text(
                text = "$description: $total ${stringResource(Res.string.ta)}",
                style = AppTypography.emphasizedSmMedium,
                color = AppColors.text.secondary
            )

            ChipsGrid(
                options = coinOptions,
                selectedChips = state.selectedChips,
                currentTotal = total,
                availableCoins = available,
                isEnabled = isCardEnabled,
                onToggle = { onEvent(CreateTaskEvent.OnChipToggle(it)) }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .shadow(
                        elevation = 25.dp,
                        spotColor = AppColors.border.primary,
                        ambientColor = Color.Black,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .background(AppColors.bg.surface, RoundedCornerShape(24.dp))
                    .padding(horizontal = 16.dp)
                    .alpha(if (isCardEnabled) 1f else 0.5f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(Res.string.ozingiz_qoshing),
                    color = AppColors.text.primary,
                    style = AppTypography.titleSmSemiBold,
                    modifier = Modifier.weight(1f)
                )

                CoinNumberPicker(
                    value = state.extraCoin,
                    max = maxExtra,
                    onValueChanged = {
                        onEvent(CreateTaskEvent.OnExtraCoinChange(it))
                    }
                )
            }
        }
    }
}

@Composable
private fun ChipsGrid(
    options: List<Int>,
    selectedChips: Set<Int>,
    currentTotal: Int,
    availableCoins: Int,
    isEnabled: Boolean,
    onToggle: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        options.chunked(3).forEach { rowOptions ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowOptions.forEach { amount ->
                    val isSelected = amount in selectedChips
                    val shape = RoundedCornerShape(20.dp)
                    val canSelect =
                        isEnabled && (isSelected || (currentTotal + amount <= availableCoins))
                    val borderColor =
                        if (isSelected) AppColors.border.accentEmphasis else Color.Transparent

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(107f / 96f)
                            .clip(shape)
                            .background(AppColors.bg.secondaryContainer, shape)
                            .border(2.dp, borderColor, shape)
                            .clickable(enabled = canSelect) { onToggle(amount) }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .alpha(if (canSelect) 1f else 0.4f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.coin),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                            Space(4.dp)

                            Text(
                                text = "$amount ${stringResource(Res.string.ta)}",
                                style = AppTypography.titleSmMedium,
                                color = AppColors.text.primary
                            )
                        }
                    }
                }

                repeat(3 - rowOptions.size) {
                    Spacer(Modifier.weight(1f))
                }

            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.LIGHT
    ) {
        RewardCard(
            state = CreateTaskState(),
            onEvent = {}
        )
    }
}