package uz.tikoncha_parent.presentation.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.common.DateTimeUtil.formatDayMonthYearWithWeekday
import uz.tikoncha_parent.common.Util.currentMillis
import uz.tikoncha_parent.common.Util.formatTimeHHmm
import uz.tikoncha_parent.presentation.base.CustomButton
import uz.tikoncha_parent.presentation.task.model.ImportanceType
import uz.tikoncha_parent.presentation.task.model.Task
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun TaskCardItem(
    task: Task,
    isCompleting: Boolean = false,
    isDeleting: Boolean = false,
    onDeleteClick: (task: Task) -> Unit,
    onEditIconClick: (task: Task) -> Unit,
    onDoneButtonClick: (task: Task) -> Unit,
    onDetailsIconClick: (task: Task) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

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
    val shownTime = remember(task.id, task.dateTime) {
        formatTimeHHmm(task.dateTime)
    }

    // Card ustida biror background action ketayotgan bo'lsa
    val isBusy = isCompleting || isDeleting

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
                modifier = Modifier.fillMaxWidth()
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

                if (task.canUpdate) {
                    Box {
                        IconButton(
                            onClick = { menuExpanded = true },
                            modifier = Modifier.size(24.dp),
                            enabled = !isBusy        // ✅ delete/complete ketayotgan bo'lsa menu yopiq
                        ) {
                            if (isDeleting) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp,
                                    color = AppColors.text.accentDanger
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "",
                                    tint = MaterialTheme.extendedColor.hintColor
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false },
                            offset = DpOffset(x = (-8).dp, y = 0.dp),
                            shape = RoundedCornerShape(24.dp),
                            containerColor = AppColors.modal.primary,
                            shadowElevation = 8.dp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(Res.drawable.eye_check),
                                        contentDescription = null,
                                        tint = AppColors.icon.secondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                text = {
                                    Text(
                                        text = stringResource(Res.string.ko_rish),
                                        style = AppTypography.titleSmMedium,
                                        color = AppColors.text.primary
                                    )
                                },
                                onClick = {
                                    menuExpanded = false
                                    onDetailsIconClick(task)
                                }
                            )

                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(Res.drawable.message_edit),
                                        contentDescription = null,
                                        tint = AppColors.icon.secondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                text = {
                                    Text(
                                        text = stringResource(Res.string.tahrirlash),
                                        style = AppTypography.titleSmMedium,
                                        color = AppColors.text.primary
                                    )
                                },
                                onClick = {
                                    menuExpanded = false
                                    onEditIconClick(task)
                                }
                            )

                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(Res.drawable.message_delete),
                                        contentDescription = null,
                                        tint = AppColors.text.accentDanger,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                text = {
                                    Text(
                                        text = stringResource(Res.string.ochirish),
                                        style = AppTypography.titleSmMedium,
                                        color = AppColors.text.accentDanger
                                    )
                                },
                                onClick = {
                                    menuExpanded = false
                                    onDeleteClick(task)
                                }
                            )
                        }
                    }
                }
            }
            Space(8.dp)

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
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
                    modifier = Modifier.padding(start = 14.dp),
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
                    modifier = Modifier.padding(start = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
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

            if (!task.isCompleted) {
                SpaceMedium()
                if (task.canUpdate) {
                    CustomButton(
                        // ✅ Loading paytida ham, child bosmagan paytda ham disable
                        enabled = task.isChildDone && !isBusy,
                        text = stringResource(Res.string.tekshirildi),
                        onClick = {
                            onDoneButtonClick(task)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(DialogButtonHeight),
                        leadingIcon = {
                            when {
                                isCompleting -> {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(14.dp),
                                        strokeWidth = 2.dp,
                                        color = AppColors.icon.secondary
                                    )
                                }
                                !task.isChildDone -> {
                                    Icon(
                                        painter = painterResource(Res.drawable.lock),
                                        contentDescription = null,
                                        tint = AppColors.icon.secondary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    )
                    Space(8.dp)

                    if (!task.isChildDone) {
                        Text(
                            text = stringResource(Res.string.bola_hali_bajardim_bosmagan),
                            style = AppTypography.emphasizedXsMedium,
                            color = AppColors.text.secondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
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
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) { }
        TaskCardItem(
            task = Task(
                title = "Matimatika uy vazifasini bajarish",
                description = "12-betdagi 5-10 mashqlarni yech.",
                date = "04.11.2025",
                time = "08:00",
                importance = ImportanceType.IMPORTANT,
                isCompleted = false,
                dateTime = currentMillis,
                progress = 50,
                createdAt = currentMillis,
                targetUserId = "",
                authorId = "",
                canUpdate = true
            ),
            isCompleting = false,
            isDeleting = false,
            onEditIconClick = {},
            onDoneButtonClick = {},
            onDetailsIconClick = {},
            onDeleteClick = {}
        )
    }
}