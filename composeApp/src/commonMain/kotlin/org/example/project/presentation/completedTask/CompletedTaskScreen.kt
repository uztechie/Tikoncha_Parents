package org.example.project.presentation.completedTask

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.project.presentation.base.CustomHeader
import org.example.project.presentation.base.SegmentedToggle
import org.example.project.presentation.base.theme.BackgroundColor
import org.example.project.presentation.base.theme.ButtonHeight
import org.example.project.presentation.base.theme.CardCornerRadius
import org.example.project.presentation.base.theme.ContainerCornerRadius
import org.example.project.presentation.base.theme.ContainerPadding
import org.example.project.presentation.base.theme.HintTextColor
import org.example.project.presentation.base.theme.LargeIconButtonPadding
import org.example.project.presentation.base.theme.LargeIconButtonSize
import org.example.project.presentation.base.theme.PrimaryColor
import org.example.project.presentation.base.theme.SmallTextSize
import org.example.project.presentation.base.theme.SpaceLarge
import org.example.project.presentation.base.theme.SpaceMedium
import org.example.project.presentation.base.theme.SpaceSmall
import org.example.project.presentation.base.theme.TextColor
import org.example.project.presentation.base.theme.TonalButtonContainerColor
import org.example.project.presentation.base.theme.WheelPickerSelectionColor
import org.example.project.presentation.task.ImportanceType
import org.example.project.presentation.task.TaskEvent
import org.example.project.presentation.task.TaskItem
import org.example.project.presentation.task.TaskState
import org.example.project.presentation.task.TaskViewModel
import org.example.project.presentation.task.reformattedToday
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.bajarilgan_vazifalar
import tikoncha_parents.composeapp.generated.resources.calendar_search
import tikoncha_parents.composeapp.generated.resources.farzandingiz_vazifalari
import tikoncha_parents.composeapp.generated.resources.shaxsiy_vazifalar
import tikoncha_parents.composeapp.generated.resources.sizdan_vazifalar
import tikoncha_parents.composeapp.generated.resources.task_square2

class CompletedTaskScreen : Screen {
    @Composable
    override fun Content() {

        val viewModel = remember { TaskViewModel() }
        val state by viewModel.state.collectAsState()
        val event = viewModel::onEvent
        val navigator = LocalNavigator.current

        CompletedTaskUi(
            state = state,
            event = event,
            navigator = navigator
        )
    }
}

@Composable
fun CompletedTaskUi(
    navigator: Navigator?,
    state: TaskState,
    event: (TaskEvent) -> Unit
) {

    val testTasks = listOf(
        CompletedTaskList(task = "Kitob o‘qish", content = "Har kuni 10 bet", progress = 80),
        CompletedTaskList(task = "Masala yechish", content = "Har kuni 10 donadan", progress = 100, isCompleted = true, importance = ImportanceType.MOST_IMPORTANT),
        CompletedTaskList(task = "KMP UI", content = "UI tugatish", progress = 100, isCompleted = true)
    )
    val updatedState = state.copy(completedTasksEndList = testTasks)


    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    val showTaskRes = if (state.genderIndex == 0) {
        Res.string.shaxsiy_vazifalar
    } else {
        Res.string.sizdan_vazifalar
    }

    val showTask = stringResource(showTaskRes)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    )
    {

        CustomHeader(
            title = stringResource(Res.string.bajarilgan_vazifalar),
            showBackButton = true,
            onBackClick = { navigator?.pop() },
            trailingIcon = {

                SpaceMedium()

                FilledTonalIconButton(
                    modifier = Modifier.size(LargeIconButtonSize),
                    onClick = { },
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = TonalButtonContainerColor,
                        contentColor = TextColor
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.task_square2),
                        contentDescription = "",
                        tint = PrimaryColor,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(LargeIconButtonPadding)
                    )
                }
            }
        )

        Column(
            modifier = Modifier.padding(ContainerPadding)
        ) {

            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .border(
                        1.dp,
                        WheelPickerSelectionColor,
                        RoundedCornerShape(ContainerCornerRadius)
                    )
                    .padding(vertical = 10.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Image(
                    painter = painterResource(Res.drawable.calendar_search),
                    contentDescription = "",
                )

                SpaceSmall()

                Text(
                    text = reformattedToday(today),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            SpaceLarge()

            Text(
                text = stringResource(Res.string.farzandingiz_vazifalari),
                fontSize = SmallTextSize,
                color = HintTextColor,
                fontWeight = FontWeight.W500
            )

            SpaceMedium()

            SegmentedToggle(
                options = listOf(
                    stringResource(Res.string.shaxsiy_vazifalar) to null,
                    stringResource(Res.string.sizdan_vazifalar) to null,
                ),
                selectedIndex = state.genderIndex,
                onOptionSelected = {
                    event(TaskEvent.OnGenderSelected(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonHeight),
            )

            SpaceMedium()

            Text(
                text = showTask,
                fontSize = SmallTextSize,
                fontWeight = FontWeight.W600,
                color = TextColor
            )

            SpaceSmall()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(updatedState.completedTasksEndList) { task ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight() // aspectRatio olib tashlandi
                            .border(1.dp, WheelPickerSelectionColor, RoundedCornerShape(CardCornerRadius)),
                        shape = RoundedCornerShape(CardCornerRadius),
                        colors = CardDefaults.cardColors(
                            containerColor = WheelPickerSelectionColor
                        )
                    ) {
                        CompletedTaskItem(
                            task = task,
                            onDetailsIconClick = {},
                            onEditIconClick = {},
                            onDoneButtonClick = {}
                        )
                    }
                }
            }

        }
    }
}

@Preview
fun Preview() {
    CompletedTaskScreen()
}