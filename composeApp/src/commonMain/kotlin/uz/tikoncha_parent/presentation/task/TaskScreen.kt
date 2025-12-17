package uz.tikoncha_parent.presentation.task

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import uz.tikoncha_parent.common.Util
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.common.CustomListDialog
import uz.tikoncha_parent.presentation.completedTask.CompletedTaskScreen
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.ChildSelectionButton
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor


class TaskScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinViewModel<TaskViewModel>()
        val state by viewModel.state.collectAsState()
        val event = viewModel::onEvent
        val navigator = LocalNavigator.current?:return

        TaskUi(
            navigator = navigator,
            state = state,
            event = event
        )
    }
}

@Composable
fun TaskUi(
    navigator: Navigator?,
    state: TaskState,
    event: (TaskEvent) -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }

    val taskLoading = state.taskResponseState is ResponseState.Loading
    val taskErrorText = state.taskResponseState.errorText()
    val taskSuccess = state.taskResponseState is ResponseState.Success

    CustomListDialog(
        title = stringResource(Res.string.farzandlaringiz),
        items = state.childrenList,
        show = showDialog,
        loading = taskLoading,
        errorMessage = taskErrorText,
        onItemSelected = {
            event(TaskEvent.OnChildSelected(it))
        },
        onDismiss = {
            showDialog = false
        }
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
    )
    {

        CustomHeader(
            showBackButton = true,
            onBackClick = {
                navigator?.pop()
            },
            title = stringResource(Res.string.vazifalar),
            trailingIcon = {

                SpaceMedium()

                FilledTonalIconButton(
                    modifier = Modifier.size(LargeIconButtonSize),
                    onClick = {
                        navigator?.push(CompletedTaskScreen())
                    },
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = MaterialTheme.extendedColor.cardColor,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    shape = RoundedCornerShape(12.dp)
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
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = ContainerPadding)
        ) {

//            Row(
//                modifier = Modifier
//                    .wrapContentSize()
//                    .border(
//                        1.dp,
//                        BorderColor,
//                        RoundedCornerShape(TextFieldCornerRadius)
//                    )
//                    .padding(vertical = 8.dp, horizontal = 12.dp),
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
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Bold,
//                )
//            }
//
            SpaceSmall()

            CustomText(
                text = stringResource(Res.string.farzandingiz_vazifalari),
                fontSize = NormalTextSize,
                color = MaterialTheme.extendedColor.hintColor,
                fontWeight = FontWeight.W500
            )

            SpaceMedium()
            ChildSelectionButton(
                text = state.selectedChild?.name ?: "",
                label = stringResource(Res.string.farzandlaringiz),
                onClick = {showDialog = true},
                imageUrl = state.selectedChild?.avatarUrl?:"",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(TextFieldHeight)
            )

            SpaceMedium()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CustomText(
                    text = stringResource(Res.string.sizdan_vazifalar),
                    fontWeight = FontWeight.W600,
                    fontSize = NormalTextSize
                )

                if (state.parentTaskList.isNotEmpty()) {
                    SpaceSmall()
                    CustomText(
                        text = if (!state.showMineAll) stringResource(Res.string.barchasini_ko_rish) else stringResource(
                            Res.string.qisqartirish
                        ),
                        color = PrimaryColor,
                        fontWeight = FontWeight.W600,
                        fontSize = NormalTextSize,
                        modifier = Modifier
                            .clickable {
                                event(TaskEvent.ShowMineAll)
                            }
                    )
                }
            }

            SpaceSmall()

            if (state.parentTaskList.isEmpty()) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f)
                        .background(
                            MaterialTheme.extendedColor.cardColor,
                            RoundedCornerShape(TextFieldCornerRadius)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CustomText(
                        text = stringResource(Res.string.xozir_vazifalar_yo_q),
                        fontSize = SmallTextSize,
                        color = MaterialTheme.extendedColor.hintColor,
                        fontWeight = FontWeight.W500
                    )
                }

            } else {

                if (state.showMineAll) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        state.parentTaskList.forEach { task ->
                            TaskItemUi(
                                task = task,
                                onDoneButtonClick = {
                                    event(TaskEvent.OnCompletedTask(task))
                                },
                                onEditIconClick = {
                                    println("onEditIconClick = $task")
                                    navigator?.push(AddNewTaskScreen(task))
                                },
                                onDetailsIconClick = {}
                            )
                        }
                    }
                } else {
                    TaskItemUi(
                        task = state.parentTaskList.first(),
                        onDoneButtonClick = { task ->
                            event(TaskEvent.OnCompletedTask(task))
                        },
                        onEditIconClick = {
                            navigator?.push(AddNewTaskScreen(state.parentTaskList.first()))
                        },
                        onDetailsIconClick = { }
                    )
                }
            }


            SpaceMedium()

            TextButton(
                onClick = {
                    navigator?.push(AddNewTaskScreen())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(TextFieldCornerRadius))
                    .border(1.dp, PrimaryColor, RoundedCornerShape(TextFieldCornerRadius))
                    .height(ButtonHeight)
            ) {
                Row {

                    CustomText(
                        text = stringResource(Res.string.vazifa_qo_shish),
                        fontSize = 16.sp,
                        color = PrimaryColor,
                        fontWeight = FontWeight.W500
                    )

                    SpaceMedium()

                    Icon(
                        painter = painterResource(Res.drawable.add_square),
                        contentDescription = "",
                        tint = PrimaryColor
                    )
                }
            }

            SpaceMedium()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CustomText(
                    text = stringResource(Res.string.farzandingiz_vazifalari),
                    fontSize = NormalTextSize,
                    fontWeight = FontWeight.W600,
                )

                if (state.childrenTaskList.isNotEmpty()) {
                    SpaceSmall()
                    CustomText(
                        text = if (!state.showChildrenAll) stringResource(Res.string.barchasini_ko_rish) else stringResource(
                            Res.string.qisqartirish
                        ),
                        color = PrimaryColor,
                        fontWeight = FontWeight.W600,
                        fontSize = NormalTextSize,
                        maxLines = 1,
                        modifier = Modifier
                            .clickable {
                                event(TaskEvent.ShowChildrenAll)
                            }
                    )
                }
            }

            SpaceSmall()

            if (state.childrenTaskList.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f)
                        .background(
                            MaterialTheme.extendedColor.cardColor,
                            RoundedCornerShape(TextFieldCornerRadius)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CustomText(
                        text = stringResource(Res.string.xozir_vazifalar_yo_q),
                        fontSize = SmallTextSize,
                        color = MaterialTheme.extendedColor.hintColor,
                        fontWeight = FontWeight.W500
                    )
                }

            } else {

                if (state.showChildrenAll) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        state.childrenTaskList.forEach { task ->
                            TaskItemUi(
                                task = task,
                                onDoneButtonClick = {},
                                onEditIconClick = {},
                                onDetailsIconClick = {}
                            )
                        }
                    }
                } else {
                    TaskItemUi(
                        task = state.childrenTaskList.first(),
                        onEditIconClick = { },
                        onDoneButtonClick = { },
                        onDetailsIconClick = { }
                    )
                }

            }
            Spacer(modifier = Modifier.weight(1f))
            SpaceLarge()
        }
    }
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        TaskUi(
            state = TaskState(),
            event = {},
            navigator = LocalNavigator.current
        )
    }
}