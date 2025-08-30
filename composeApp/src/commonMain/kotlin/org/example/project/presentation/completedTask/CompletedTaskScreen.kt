package org.example.project.presentation.completedTask

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import org.example.project.common.Util.getCurrentDate
import org.example.project.data.mapper.toTask
import org.example.project.presentation.base.CustomHeader
import org.example.project.presentation.base.SegmentedToggle
import org.example.project.presentation.task.ImportanceType
import org.example.project.presentation.task.TaskEvent
import org.example.project.presentation.task.TaskState
import org.example.project.presentation.task.TaskViewModel
import org.example.project.presentation.task.reformattedToday
import org.example.project.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomText

class CompletedTaskScreen : Screen {
    @Composable
    override fun Content() {

        val viewModel = koinViewModel <TaskViewModel>()
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

    val today = getCurrentDate()

    val showTaskRes = if (state.genderIndex == 0) {
        Res.string.shaxsiy_vazifalar
    } else {
        Res.string.sizdan_vazifalar
    }

    val showTask = stringResource(showTaskRes)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    )
    {

        CustomHeader(
            title = stringResource(Res.string.bajarilgan_vazifalar),
            showBackButton = true,
            onBackClick = { navigator?.pop() },
        )

        Column(
            modifier = Modifier.padding(ContainerPadding)
        ) {

            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.secondary,
                        RoundedCornerShape(TextFieldCornerRadius)
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

                CustomText(
                    text = reformattedToday(today),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            SpaceLarge()

            CustomText(
                text = stringResource(Res.string.farzandingiz_vazifalari),
                fontSize = SmallTextSize,
                color = MaterialTheme.colorScheme.secondary,
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
                fontWeight = FontWeight.Normal,
                fontSize = SmallTextSize
            )

            SpaceMedium()

            CustomText(
                text = showTask,
                fontSize = SmallTextSize,
                fontWeight = FontWeight.W600,
            )

            SpaceSmall()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.allTaskList) { task ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight() // aspectRatio olib tashlandi
                            .border(1.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(TextFieldCornerRadius)),
                        shape = RoundedCornerShape(TextFieldCornerRadius),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        CompletedTaskItem(
                            task = task.toTask(),
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