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

    val timeProgress = remember(task.createdAt, task.dateTime, task.isCompleted) {
        if (task.isCompleted) 100 else computeTimeProgress(task.createdAt, task.dateTime)
    }

    val shownTime = remember(task.id, task.dateTime) {
        formatTimeHHmm(task.dateTime)
    }

    val shownDate = remember(task.id, task.dateTime) {
        formatDateDdMmYyyy(task.dateTime)
    }

    var titleColor = if (timeProgress == 0 && !task.isCompleted) {
        ProgressColor1
    } else {
        TextColor
    }
    var iconColor = if (timeProgress == 0 && !task.isCompleted) {
        ProgressColor1
    } else {
        PrimaryColor.copy(alpha = 0.7f)
    }


    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(TextFieldCornerRadius)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
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
                        color = titleColor
                    )

                    CustomText(
                        text = task.description,
                        fontSize = UltraSmallTextSize,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.secondary
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
                            tint = PrimaryColor,
                            modifier = Modifier.padding(SmallIconButtonPadding)
                                .fillMaxSize()

                        )
                    } else {
                        Icon(
                            painter = painterResource(Res.drawable.primary_arrow_right),
                            contentDescription = "",
                            tint = PrimaryColor,
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
                        tint = iconColor,
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
                        tint = iconColor,
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
                        tint = iconColor,
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

            SpaceUltraSmall()

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                CustomLinearProgress(
                    progress = (timeProgress / 100).toFloat(),
                    modifier = Modifier
                        .weight(1f),
                    height = LinearProgressIndicatorHeight
                )

                CustomText(
                    text = "${timeProgress}%",
                    modifier = Modifier
                        .padding(start = 10.dp),
                    fontSize = SmallTextSize,
                    fontWeight = FontWeight.SemiBold
                )
            }

            SpaceMedium()

            CustomText(
                text = stringResource(Res.string.bajarilgan),
                color = PrimaryColor,
                fontSize = NormalTextSize,
                fontWeight = FontWeight.SemiBold
            )

        }
    }
}

@Preview()
@Composable
private fun Pre() {
    CompletedTaskItem(
        task = null,
        onDetailsIconClick = {}
    )
}