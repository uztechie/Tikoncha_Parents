package org.example.project.presentation.completedTask

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.project.presentation.base.theme.*
import org.example.project.presentation.task.CustomLinearProgress
import org.example.project.presentation.task.ImportanceType
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import utils.getCurrentTimeMillis
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText

@Composable
fun CompletedTaskItem(
    task: CompletedTaskList,
    onEditIconClick: (task: CompletedTaskList) -> Unit,
    onDetailsIconClick: (task: CompletedTaskList) -> Unit,
    onDoneButtonClick: (task: CompletedTaskList) -> Unit
) {

    val importance = when(task.importance){
        ImportanceType.IMPORTANT -> "Muhim"
        ImportanceType.NONE -> ""
        ImportanceType.MEDIUM -> "O'rtacha"
        ImportanceType.MOST_IMPORTANT -> "O'ta muhim"
    }

    var titleColor = if (task.progress == 0 && !task.isCompleted){
        ProgressColor1
    }
    else{
        TextColor
    }
    var iconColor = if (task.progress == 0 && !task.isCompleted){
        ProgressColor1
    }
    else{
        PrimaryColor.copy(alpha = 0.7f)
    }


    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(CardCornerRadius)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BackgroundColor)
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
                        text = task.task,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = NormalTextSize,
                        color = titleColor
                    )

                    CustomText(
                        text = task.content,
                        fontSize = UltraSmallTextSize,
                        fontWeight = FontWeight.SemiBold,
                        color = HintTextColor
                    )

                }

                IconButton(
                    onClick = {
                        if (!task.isCompleted){
                            onEditIconClick(task)
                        }else{
                            onDetailsIconClick(task)
                        }
                    },
                    modifier = Modifier.size(NormalIconButtonSize)
                ) {

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
                        text = task.endDate,
                        color = TextColor,
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
                        text = task.endTime,
                        color = TextColor,
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
                        color = TextColor,
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
                       progress = (task.progress.toDouble() / 100).toFloat(),
                       modifier = Modifier
                           .weight(1f),
                       height = LinearProgressIndicatorHeight
                   )

                   CustomText(
                       text = "${task.progress}%",
                       modifier = Modifier
                           .padding(start = 10.dp),
                       color = TextColor,
                       fontSize = SmallTextSize,
                       fontWeight = FontWeight.SemiBold
                   )
               }

               SpaceMedium()

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
}

@Preview()
@Composable
private fun Pre() {
    CompletedTaskItem(
        task = CompletedTaskList(),
        onEditIconClick = {},
        onDoneButtonClick = {},
        onDetailsIconClick = {}
    )
}