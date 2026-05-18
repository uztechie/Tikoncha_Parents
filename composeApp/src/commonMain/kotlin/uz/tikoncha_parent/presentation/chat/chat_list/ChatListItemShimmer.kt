package uz.tikoncha_parent.presentation.chat.chat_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.tikoncha_parent.presentation.base.shimmer
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme



@Composable
fun ChatListShimmer(
    modifier: Modifier = Modifier,
    count: Int = 8
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        userScrollEnabled = false   // ← shimmer paytida scroll kerak emas
    ) {
        items((1..count).toList()) {
            ChatListItemShimmer()
        }
    }
}

@Composable
fun ChatListItemShimmer(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar (circle)
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .shimmer(
                    colors = listOf(
                        AppColors.bg.section,
                        AppColors.bg.secondary
                    )
                )
        )

        Spacer(modifier = Modifier.size(12.dp))

        // Title + subtitle
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.55f)
                        .height(20.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .shimmer(
                            colors = listOf(
                                AppColors.bg.section,
                                AppColors.bg.secondary
                            )
                        )
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.35f)
                        .height(20.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .shimmer(
                            colors = listOf(
                                AppColors.bg.section,
                                AppColors.bg.secondary
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(18.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmer(
                        colors = listOf(
                            AppColors.bg.section,
                            AppColors.bg.secondary
                        )
                    )
            )
        }
    }
}

@Preview
@Composable
private fun Pre() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ) {
        ChatListShimmer()
    }
}