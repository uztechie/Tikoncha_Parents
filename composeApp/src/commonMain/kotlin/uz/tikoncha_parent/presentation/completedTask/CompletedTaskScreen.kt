package uz.tikoncha_parent.presentation.completedTask

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import uz.tikoncha_parent.common.Util.getCurrentDate
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.SegmentedToggle
import uz.tikoncha_parent.presentation.task.TaskEvent
import uz.tikoncha_parent.presentation.task.TaskState
import uz.tikoncha_parent.presentation.task.TaskViewModel
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

class CompletedTaskScreen : Screen {
    @Composable
    override fun Content() {

        val viewModel = koinScreenModel<TaskViewModel>()
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
            .background(MaterialTheme.extendedColor.backgroundColor)
    )
    {

        CustomHeader(
            title = stringResource(Res.string.bajarilgan_vazifalar),
            showBackButton = true,
            onBackClick = { navigator?.pop() },
        )

        Column(
            modifier = Modifier.padding(horizontal = ContainerPadding)
        ) {

//            Row(
//                modifier = Modifier
//                    .wrapContentSize()
//                    .border(
//                        1.dp,
//                        MaterialTheme.colorScheme.secondary,
//                        RoundedCornerShape(TextFieldCornerRadius)
//                    )
//                    .padding(vertical = 10.dp, horizontal = 12.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center
//            ) {
//
//                Image(
//                    painter = painterResource(Res.drawable.calendar_search),
//                    contentDescription = "",
//                )
//
//                SpaceSmall()
//
//                CustomText(
//                    text = reformattedToday(today),
//                    fontSize = 13.sp,
//                    fontWeight = FontWeight.Bold,
//                )
//            }
//
//            SpaceLarge()

            SpaceMedium()
            CustomText(
                text = stringResource(Res.string.farzandingiz_vazifalari),
                fontSize = SmallTextSize,
                color = MaterialTheme.extendedColor.hintColor,
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

            val bottomRoundedShape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 0.dp,
                bottomStart = ShapeCornerRadius,
                bottomEnd = ShapeCornerRadius
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.selectedCompletedTaskList) { task ->
                    CompletedTaskItem(
                        task = task,
                        onDetailsIconClick = {},
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        CompletedTaskUi(
            state = TaskState(),
            event = {},
            navigator = LocalNavigator.current
        )
    }
}