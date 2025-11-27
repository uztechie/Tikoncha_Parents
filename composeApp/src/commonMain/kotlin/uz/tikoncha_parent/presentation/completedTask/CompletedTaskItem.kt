package uz.tikoncha_parent.presentation.completedTask

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.common.Util.computeTimeProgress
import uz.tikoncha_parent.common.Util.formatDateDdMmYyyy
import uz.tikoncha_parent.common.Util.formatTimeHHmm
import uz.tikoncha_parent.presentation.task.CustomLinearProgress
import uz.tikoncha_parent.presentation.task.ImportanceType
import uz.tikoncha_parent.presentation.task.Task
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*

import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.tripleShadow
import uz.tikoncha_parent.presentation.task.TaskUi
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun CompletedTaskItem(
    task: Task?,
    onDetailsIconClick: (task: Task) -> Unit,
) {

    if (task == null) {
        return
    }
    val importance = when (task.importance) {
        ImportanceType.IMPORTANT -> "Muhim"
        ImportanceType.NONE -> ""
        ImportanceType.MEDIUM -> "O'rtacha"
        ImportanceType.MOST_IMPORTANT -> "O'ta muhim"
    }

    val shownTime = remember(task.id, task.dateTime) {
        formatTimeHHmm(task.dateTime)
    }

    val shownDate = remember(task.id, task.dateTime) {
        formatDateDdMmYyyy(task.dateTime)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .tripleShadow(
                shape = RoundedCornerShape(TextFieldCornerRadius)
            )
            .clip(RoundedCornerShape(TextFieldCornerRadius))
            .background(
                MaterialTheme.extendedColor.cardColor,
                RoundedCornerShape(TextFieldCornerRadius)
            )
            .padding(horizontal = 20.dp, vertical = 15.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {

                CustomText(
                    text = task.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = NormalTextSize,
                )

                CustomText(
                    text = task.description,
                    fontSize = UltraSmallTextSize,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.extendedColor.hintColor
                )

            }

            IconButton(
                onClick = {
                    onDetailsIconClick(task)
                },
                modifier = Modifier.size(NormalIconButtonSize)
            ) {

                if (!task.isCompleted) {
                    Icon(
                        painter = painterResource(Res.drawable.edit_pen),
                        contentDescription = "",
                        tint = MaterialTheme.extendedColor.primaryColor,
                        modifier = Modifier.padding(SmallIconButtonPadding)
                            .fillMaxSize()

                    )
                } else {
                    Icon(
                        painter = painterResource(Res.drawable.primary_arrow_right),
                        contentDescription = "",
                        tint = MaterialTheme.extendedColor.primaryColor,
                        modifier = Modifier.padding(SmallIconButtonPadding)
                            .fillMaxSize()
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.calendar),
                    tint = MaterialTheme.extendedColor.primaryColor,
                    contentDescription = "",
                    modifier = Modifier.size(TextFieldIconSize)
                )

                CustomText(
                    modifier = Modifier
                        .padding(start = 3.dp),
                    text = shownDate,
                    fontSize = SmallTextSize,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.alarm),
                    tint = MaterialTheme.extendedColor.primaryColor,
                    contentDescription = "",
                    modifier = Modifier.size(TextFieldIconSize)
                )

                CustomText(
                    modifier = Modifier
                        .padding(start = 3.dp),
                    text = shownTime,
                    fontSize = SmallTextSize,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.zap),
                    tint = MaterialTheme.extendedColor.primaryColor,
                    contentDescription = "",
                    modifier = Modifier.size(TextFieldIconSize)
                )

                CustomText(
                    modifier = Modifier
                        .padding(start = 3.dp),
                    text = importance,
                    fontSize = SmallTextSize,
                    fontWeight = FontWeight.SemiBold
                )
            }

        }
        SpaceMedium()

        CustomText(
            text = stringResource(Res.string.bajarilgan),
            color = MaterialTheme.extendedColor.primaryColor,
            fontSize = NormalTextSize,
            fontWeight = FontWeight.SemiBold
        )

    }
}

@Preview()
@Composable
private fun Pre() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
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
            ),
            onDetailsIconClick = {}
        )
    }
}