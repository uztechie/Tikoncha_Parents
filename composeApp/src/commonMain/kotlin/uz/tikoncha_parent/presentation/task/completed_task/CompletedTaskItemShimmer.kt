package uz.tikoncha_parent.presentation.task.completed_task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.presentation.base.shimmer
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun CompletedTaskItemShimmer() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(AppColors.modal.primary)
    ) {
        // Chap tomondagi accent chiziq placeholder
        Box(
            modifier = Modifier
                .offset(x = 2.dp, y = 20.dp)
                .width(4.dp)
                .height(36.dp)
                .shimmer(shape = RoundedCornerShape(2.dp))
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Title + "Bajarilgan" chip row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(18.dp)
                            .shimmer(shape = RoundedCornerShape(6.dp))
                    )
                    Space(8.dp)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(12.dp)
                            .shimmer(shape = RoundedCornerShape(4.dp))
                    )
                    Spacer(Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(12.dp)
                            .shimmer(shape = RoundedCornerShape(4.dp))
                    )
                }

                // "Bajarilgan" chip placeholder
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(22.dp)
                        .shimmer(shape = RoundedCornerShape(50))
                )
            }

            Space(12.dp)

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                itemVerticalAlignment = Alignment.CenterVertically,
            ) {
                // Calendar
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .shimmer(shape = RoundedCornerShape(4.dp))
                    )
                    Spacer(Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .width(110.dp)
                            .height(14.dp)
                            .shimmer(shape = RoundedCornerShape(4.dp))
                    )
                }

                // Time
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .shimmer(shape = RoundedCornerShape(4.dp))
                    )
                    Spacer(Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(14.dp)
                            .shimmer(shape = RoundedCornerShape(4.dp))
                    )
                }

                // Importance
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .shimmer(shape = CircleShape)
                    )
                    Spacer(Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .width(70.dp)
                            .height(14.dp)
                            .shimmer(shape = RoundedCornerShape(4.dp))
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CompletedTaskItemShimmerPreview() {
    TikonchaParentTheme(ThemeMode.DARK) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CompletedTaskItemShimmer()
            CompletedTaskItemShimmer()
        }
    }
}