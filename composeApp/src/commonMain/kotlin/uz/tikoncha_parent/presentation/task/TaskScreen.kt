package uz.tikoncha_parent.presentation.task

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.completedTask.CompletedTaskScreen
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.presentation.add_child.AddChildScreen
import uz.tikoncha_parent.presentation.base.ChildSelectionButton
import uz.tikoncha_parent.presentation.base.ConfirmationBottomSheet
import uz.tikoncha_parent.presentation.base.CustomButton
import uz.tikoncha_parent.presentation.base.bottomShadow
import uz.tikoncha_parent.presentation.base.singleClick
import uz.tikoncha_parent.presentation.new_home.SelectionChildBottonSheet
import uz.tikoncha_parent.presentation.task.add_task.AddNewTaskScreen
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.rememberScreenSystemBars


class TaskScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<TaskViewModel>()
        val state by viewModel.state.collectAsState()
        val event = viewModel::onEvent
        val navigator = LocalNavigator.current ?: return

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
    var showChildSelector by remember { mutableStateOf(false) }
    var taskToDelete by remember { mutableStateOf<Task?>(null) }
    val displayedList = if (state.taskIndex == 0) {
        state.parentTaskList
    } else {
        state.childrenTaskList
    }
    val isParentTab = state.taskIndex == 0

    if (showChildSelector) {
        SelectionChildBottonSheet(
            navigator = navigator,
            items = state.childrenList,
            selectedItem = state.selectedChild,
            onDismiss = { showChildSelector = false },
            title = stringResource(Res.string.farzandlaringiz),
            onItemSelected = {
                event(TaskEvent.OnChildSelected(it))
                showChildSelector = false
            }
        )
    }

    taskToDelete?.let { task ->
        ConfirmationBottomSheet(
            title = stringResource(Res.string.vazifa_ochirilsinmi),
            subtitle = stringResource(Res.string.vazifa_ochirish_tasdiq),
            confirmText = stringResource(Res.string.ochirish),
            cancelText = stringResource(Res.string.bekor_qilish),
            onDismiss = { taskToDelete = null },
            onConfirm = {
                taskToDelete = null
            }
        )
    }
    val systemBars = rememberScreenSystemBars(
        statusBarColor = AppColors.bg.secondary,
        navigationBarColor = AppColors.bg.secondary
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(systemBars.modifier)
            .background(AppColors.bg.secondary)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Header ────────────────────────────────────
            CustomHeader(
                showBackButton = true,
                onBackClick = { navigator?.pop() },
                title = stringResource(Res.string.vazifalar),
                trailingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.circle_clock),
                        contentDescription = null,
                        tint = AppColors.icon.accentPrimary,
                        modifier = Modifier
                            .size(24.dp)
                            .singleClick {
                                navigator?.push(CompletedTaskScreen())
                            }
                    )
                }
            )

            // ── Toggle + Child selector ───────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ContainerPadding)
            ) {
                Spacer(Modifier.height(8.dp))
                TaskSegmentedToggle(
                    selectedIndex = state.taskIndex,
                    modifier = Modifier.fillMaxWidth(),
                    options = listOf(
                        stringResource(Res.string.ozim) to null,
                        stringResource(Res.string.farzandim) to null
                    ),
                    onOptionSelected = { event(TaskEvent.OnTaskSelected(it)) }
                )
                Spacer(Modifier.height(12.dp))

                ChildSelectionButton(
                    text = state.selectedChild?.name.orEmpty(),
                    imageUrl = state.selectedChild?.avatarUrl.orEmpty(),
                    label = stringResource(Res.string.farzandlaringiz),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(TextFieldHeight),
                    onClick = {
                        if (state.childrenList.isEmpty()) navigator?.push(AddChildScreen())
                        else showChildSelector = true
                    }
                )
                Spacer(Modifier.height(12.dp))
            }

            // ── Empty state YOKI LazyColumn ───────────────
            if (displayedList.isEmpty()) {
                EmptyTaskState(
                    title = if (isParentTab) stringResource(Res.string.hali_vazifa_yoq) else stringResource(
                        Res.string.hozir_vazifalar_yo_q
                    ),
                    subtitle = if (isParentTab) stringResource(Res.string.ota_ona_vazifalari_desc) else stringResource(
                        Res.string.farzand_vazifalari_desc
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(bottom = ButtonHeight + ContainerPadding * 2)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(
                        start = ContainerPadding,
                        end = ContainerPadding,
                        bottom = ButtonHeight + ContainerPadding * 2
                    ),
                    verticalArrangement = Arrangement.spacedBy(if (isParentTab) 10.dp else 12.dp)
                ) {
                    items(items = displayedList, key = { it.id }) { task ->
                        TaskCardItem(
                            task = task,
                            onDetailsIconClick = {

                            },
                            onDoneButtonClick = {
                                if (isParentTab) event(TaskEvent.OnCompletedTask(task))
                            },
                            onEditIconClick = {
                                if (isParentTab) navigator?.push(AddNewTaskScreen(task))
                            },
                            onDeleteClick = {
                                taskToDelete = it
                            }
                        )
                    }
                }
            }
        }

        // ── Pastki tugma ──────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .bottomShadow(
                    shape = RoundedCornerShape(
                        topStart = ButtonCornerRadius,
                        topEnd = ButtonCornerRadius
                    ),
                    color = AppColors.bg.secondary
                )
                .background(
                    AppColors.bg.elevated,
                    RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .height(ButtonHeight)
                .align(Alignment.BottomCenter)
        ) {
            CustomButton(
                enabled = state.selectedChild != null,
                text = stringResource(Res.string.vazifa_qo_shish),
                onClick = { navigator?.push(AddNewTaskScreen()) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun EmptyTaskState(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.home_task), // ⚠ resource qo'shish kerak
            contentDescription = null,
            modifier = Modifier.size(90.dp)
        )
        Spacer(Modifier.height(26.dp))

        Text(
            text = title, // ⚠ string qo'shish kerak
            style = AppTypography.titleSmSemiBold,
            color = AppColors.text.primary,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(12.dp))

        Text(
            text = subtitle, // ⚠ string qo'shish kerak
            style = AppTypography.emphasizedMdMedium,
            color = AppColors.text.secondary,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.LIGHT
    ) {
        TaskUi(
            state = TaskState(),
            event = {},
            navigator = LocalNavigator.current
        )
    }
}