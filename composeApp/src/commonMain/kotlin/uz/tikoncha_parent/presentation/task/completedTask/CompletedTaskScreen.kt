package uz.tikoncha_parent.presentation.task.completedTask

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.presentation.task.TaskListEvent
import uz.tikoncha_parent.presentation.task.TaskListState
import uz.tikoncha_parent.presentation.task.TaskListViewModel
import uz.tikoncha_parent.presentation.task.TaskSegmentedToggle
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.rememberScreenSystemBars

class CompletedTaskScreen : Screen {
    @Composable
    override fun Content() {

        val viewModel = koinScreenModel<TaskListViewModel>()
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
    state: TaskListState,
    event: (TaskListEvent) -> Unit
) {
    val systemBars = rememberScreenSystemBars(
        statusBarColor = AppColors.bg.secondary,
        navigationBarColor = AppColors.bg.secondary
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(systemBars.modifier)
            .background(AppColors.bg.secondary)
    ) {
        CustomHeader(
            showBackButton = true,
            onBackClick = { navigator?.pop() },
            title = stringResource(Res.string.bajarilgan_vazifalar),
        )

        Column(
            modifier = Modifier.padding(horizontal = ContainerPadding)
        ) {
            Spacer(Modifier.height(10.dp))
            TaskSegmentedToggle(
                selectedIndex = state.taskIndex,
                modifier = Modifier.fillMaxWidth(),
                onOptionSelected = {
                    event(TaskListEvent.OnGenderSelected(it))
                },
                options = listOf(
                    stringResource(Res.string.ozim) to null,
                    stringResource(Res.string.farzandim) to null
                )
            )
            Spacer(Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.selectedCompletedTaskList) { task ->
                    CompletedTaskItem(task = task)
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
    ) {
        CompletedTaskUi(
            event = {},
            state = TaskListState(),
            navigator = LocalNavigator.current
        )
    }
}