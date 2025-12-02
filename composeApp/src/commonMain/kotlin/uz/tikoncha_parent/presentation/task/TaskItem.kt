package uz.tikoncha_parent.presentation.task

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import uz.tikoncha_parent.common.Util.computeTimeProgress
import uz.tikoncha_parent.common.Util.currentMillis
import uz.tikoncha_parent.common.Util.formatDateDdMmYyyy
import uz.tikoncha_parent.common.Util.formatTimeHHmm
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun TaskItemUi(
    task: Task,
    onEditIconClick: (task: Task) -> Unit,
    onDetailsIconClick: (task: Task) -> Unit,
    onDoneButtonClick: (task: Task) -> Unit
) {

    val importance = when(task.importance){
        ImportanceType.IMPORTANT -> stringResource(Res.string.muhim)
        ImportanceType.NONE -> ""
        ImportanceType.MEDIUM -> stringResource(Res.string.o_rtacha)
        ImportanceType.MOST_IMPORTANT -> stringResource(Res.string.o_ta_muhim)
    }

    val shownTime = remember(task.id, task.dateTime) {
        formatTimeHHmm(task.dateTime)
    }

    val shownDate = remember(task.id, task.dateTime) {
        formatDateDdMmYyyy(task.dateTime)
    }

    var timeProgress by remember(task.id, task.createdAt, task.dateTime) {
        mutableStateOf(computeTimeProgress(task.createdAt, task.dateTime))
    }


    var titleColor = if (timeProgress == 0 && !task.isCompleted){
        ProgressColor1
    }
    else{
        MaterialTheme.extendedColor.onBackgroundColor
    }

    var iconColor = if (timeProgress == 0 && !task.isCompleted){
        ProgressColor1
    }
    else{
        PrimaryColor.copy(alpha = 0.7f)
    }

    LaunchedEffect(task.id, task.createdAt, task.dateTime) {
        while (true) {
            timeProgress = computeTimeProgress(task.createdAt, task.dateTime)
            delay(1_000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.extendedColor.cardColor,
                RoundedCornerShape(TextFieldCornerRadius)
            )
            .padding(horizontal = 20.dp, vertical = 15.dp)
    )
    {

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
                    color = MaterialTheme.extendedColor.hintColor
                )
            }

            if (task.isMine){
                IconButton(
                    onClick = {
                        if (!task.isCompleted){
                            onEditIconClick(task)
                        }else{
                            onDetailsIconClick(task)
                        }
                    },
                    modifier = Modifier.size(NormalIconButtonSize)
                )
                {

                    if (!task.isCompleted){
                        Icon(
                            painter = painterResource(Res.drawable.edit_pen),
                            contentDescription = "",
                            tint = PrimaryColor,
                            modifier = Modifier.padding(SmallIconButtonPadding)
                                .fillMaxSize()

                        )
                    }else{
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

        if (!task.isCompleted){
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                CustomLinearProgress(
                    progress = timeProgress / 100f,
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

            if (task.isMine){
                CustomButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DialogButtonHeight),
                    onClick = {
                        onDoneButtonClick(task)
                    },
                    enabled = true,
                    text = stringResource(Res.string.bajarildi)
                )
            }


        }else{

            SpaceLarge()

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
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        TaskItemUi(
            task = Task(
                title = "40 varoq kitob o'qish",
                description = "Bir hafta davomida har kuni 40 varoqdan",
                date = "04.11.2025",
                time = "08:00",
                importance = ImportanceType.IMPORTANT,
                isCompleted = true,
                dateTime = currentMillis,
                progress = 50,
                createdAt = currentMillis,
                targetUserId = "",
                authorId = "",
                isMine = true
            ),
            onEditIconClick = {},
            onDoneButtonClick = {},
            onDetailsIconClick = {}
        )
    }
}