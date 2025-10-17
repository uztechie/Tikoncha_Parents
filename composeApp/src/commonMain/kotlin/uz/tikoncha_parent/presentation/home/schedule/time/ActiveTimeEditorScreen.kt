package uz.tikoncha_parent.presentation.home.schedule.time

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.qoshish
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.CustomOutlinedButton
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.SpaceMedium


class ActiveTimeEditorScreen(
    private val initial: ActiveTimeState = ActiveTimeState(),
    private val onSave: (ActiveTimeState) -> Unit = {}
) : Screen {
    @Composable
    override fun Content() {

        val controller = remember { ActiveTimeController(initial) }
        val state by controller.state.collectAsState()

        val vm: ScheduleViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
        val scheduleState by vm.state.collectAsState()


        ActiveTimeEditorUi(
            controller = controller,
            onChanges = onSave,
        )
    }
}

@Composable
fun ActiveTimeEditorUi(
    controller: ActiveTimeController = remember { ActiveTimeController() },
    onChanges: (ActiveTimeState) -> Unit = {},
    scheduleState: ScheduleState = ScheduleState(),
    onScheduleEvent: (ScheduleEvent) -> Unit = {}
) {
    val state by controller.state.collectAsState()

    LaunchedEffect(state) { onChanges(state) }

    Column(
        modifier = Modifier
            .padding(ContainerPadding)
            .fillMaxWidth()
    ) {
        CustomText(
            text = "Активное время"
        )
        SpaceMedium()

        WeekdayChips(
            selected = state.selectedDays,
            onToggle = { controller.toggleDay(it) }
        )
        SpaceMedium()

        Timeline(
            ranges = state.ranges,
            disabled = state.allDay,
            height = 20.dp
        )
        SpaceMedium()

        AllDayRow(
            checked = state.allDay,
            onCheckedChange = { controller.setAllDay(it) }
        )
        SpaceMedium()
        AnimatedVisibility(visible = !state.allDay) {
            Column {
                RangesList(
                    ranges = state.ranges,
                    onDelete = { index -> controller.deleteRange(index) },
                    onEdit = { index, newStart, newEnd ->
                        controller.updateRange(index, newStart, newEnd)
                    }
                )

                SpaceMedium()

                CustomOutlinedButton(
                    text = stringResource(Res.string.qoshish),
                    onClick = { controller.addDefaultRange() },
                    modifier = Modifier.align(Alignment.Start)
                )
            }
        }
    }
}


@Composable
@Preview
private fun PreviewActiveTimeEditor() {
    ActiveTimeEditorUi()
}