package uz.tikoncha_parent.presentation.task.completedTask

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.common.Util.formatTimeHHmm
import uz.tikoncha_parent.presentation.task.model.ImportanceType
import uz.tikoncha_parent.presentation.task.model.Task
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.common.DateTimeUtil.formatDayMonthWithWeekday
import uz.tikoncha_parent.common.DateTimeUtil.formatDayMonthYearWithWeekday
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun CompletedTaskItem(
    task: Task,
) {
    val importance = when (task.importance) {
        ImportanceType.NONE -> ""
        ImportanceType.IMPORTANT -> stringResource(Res.string.muhim)
        ImportanceType.MEDIUM -> stringResource(Res.string.o_rtacha)
        ImportanceType.MOST_IMPORTANT -> stringResource(Res.string.juda_muhim)
    }

    // Muhimlik darajasiga qarab rang
    val importanceColor = when (task.importance) {
        ImportanceType.MOST_IMPORTANT -> AppColors.text.accentDanger
        ImportanceType.IMPORTANT -> AppColors.text.accentWarning
        ImportanceType.MEDIUM -> AppColors.text.accentSuccess
        ImportanceType.NONE -> Color.Transparent
    }

    val shownDate = formatDayMonthYearWithWeekday(task.dateTime)
    val shownTime = remember(task.id, task.dateTime) { formatTimeHHmm(task.dateTime) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(AppColors.modal.primary)
    ) {
        // Chap tomondagi rangli accent chiziq
        Box(
            modifier = Modifier
                .offset(x = 2.dp, y = 20.dp)
                .width(4.dp)
                .height(36.dp)
                .drawBehind {
                    val path = Path().apply {
                        val w = size.width
                        val h = size.height
                        val r = w  // egilish radiusi = enning to'liqligi

                        moveTo(0f, 0f)
                        // yuqoridagi yumaloq qism
                        quadraticTo(w, 0f, w, r)
                        // o'ng tomondagi to'g'ri chiziq
                        lineTo(w, h - r)
                        // pastdagi yumaloq qism
                        quadraticTo(w, h, 0f, h)
                        close()
                    }
                    drawPath(path = path, color = importanceColor)
                }
        )

        // Asosiy kontent
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = task.title,
                        style = AppTypography.titleMdSemiBold,
                        color = AppColors.text.primary
                    )
                    Space(8.dp)

                    Text(
                        text = task.description,
                        style = AppTypography.emphasizedXsMedium,
                        color = AppColors.text.primary
                    )
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(AppColors.bg.accentWarningContainer)
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.checked),
                        contentDescription = null,
                        tint = AppColors.text.accentSuccess,
                        modifier = Modifier.size(10.dp)
                    )
                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = stringResource(Res.string.bajarilgan),
                        style = AppTypography.bodySmSemiBold,
                        color = AppColors.text.accentSuccess,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Space(8.dp)

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                itemVerticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.calendar),
                        tint = AppColors.icon.secondary,
                        contentDescription = "",
                        modifier = Modifier.size(20.dp)
                    )

                    Text(
                        modifier = Modifier.padding(start = 3.dp),
                        text = shownDate,
                        color = AppColors.text.primary,
                        style = AppTypography.bodyMdMedium
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.time_square),
                        tint = AppColors.icon.secondary,
                        contentDescription = "",
                        modifier = Modifier.size(20.dp)
                    )

                    Text(
                        modifier = Modifier.padding(start = 3.dp),
                        text = shownTime,
                        color = AppColors.text.primary,
                        style = AppTypography.bodyMdMedium
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(importanceColor)
                    )

                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = importance,
                        color = importanceColor,
                        style = AppTypography.bodyMdSemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Pre() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ) {
        CompletedTaskItem(
            task = Task(
                id = "1",
                title = "Title",
                description = "Description",
                date = "12.12.2023",
                time = "12:00",
                dateTime = 123123123123,
                importance = ImportanceType.IMPORTANT,
                isCompleted = true,
                isMine = true,
                authorId = "1",
                targetUserId = "1",
                createdAt = 123123123123,
                coin = 123
            )
        )
    }
}