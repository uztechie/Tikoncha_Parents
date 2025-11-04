package uz.tikoncha_parent.presentation.home.schedule.timelist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.CustomOutlinedButton
import uz.tikoncha_parent.presentation.base.bottomShadow
import uz.tikoncha_parent.presentation.home.schedule.type.ScheduleType
import uz.tikoncha_parent.presentation.home.schedule.type.ScheduleTypeUi
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.theme.extendedColor


class ScheduleUsageLimitListScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        val viewModel = koinViewModel<ScheduleTimeViewModel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::event

        ScheduleTypeUi(
            navigator = navigator,
            state = state.value,
            event = event
        )
    }
}
@Composable
fun ScheduleUsageLimitListUi(
    navController: NavController,
    state: ScheduleTimeState,
    event: (ScheduleTimeEvent) -> Unit
)
{

    val isTimeEnabled = state.enabledByType[ScheduleType.TIME] == true

    var showSetupDialog by remember {
        mutableStateOf(false)
    }

    ScheduleUsageLimitDialog(
        show = showSetupDialog,
        state = state,
        event = event,
        onDismiss = { showSetupDialog = false }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {

        CustomHeader(
            modifier = Modifier
                .zIndex(1f),
            title = stringResource(Res.string.foydalanish_chegarasi),
            showBackButton = true,
            onBackClick = { navController.popBackStack() }
        )



        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            contentPadding = PaddingValues(ContainerPadding)
        ) {
            items(state.usageTimeList) {
                ScheduleUsageLimitItem(
                    modifier = Modifier
                        .clickable(
                            enabled = !isTimeEnabled,
                            interactionSource = null,
                            indication = null,
                            onClick = {
//                                event(ScheduleTimeEvent.SetUsageLimitData(
//                                    it
//                                ))
                                showSetupDialog = true
                            }
                        ),
                    item = it,
                    onRemove = {
//                        event(ScheduleTimeEvent.RemoveUsageLimit(it))
                    }
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .bottomShadow(
                    shape = RoundedCornerShape(
                        topStart = ButtonCornerRadius,
                        topEnd = ButtonCornerRadius
                    ),
                    color = MaterialTheme.extendedColor.backgroundColor
                )
                .bottomShadow(
                    shape = RoundedCornerShape(
                        topStart = ButtonCornerRadius,
                        topEnd = ButtonCornerRadius
                    ),
                    color = MaterialTheme.extendedColor.backgroundColor,
                    lowerOffset = -5.dp,
                    radius = 10.dp

                )
                .background(MaterialTheme.extendedColor.backgroundColor)
                .padding(
                    start = ContainerPadding,
                    end = ContainerPadding,
                    bottom = ContainerPadding
                )


        )
        {
            CustomOutlinedButton(
                enabled = !state.timeList.any { it.allDay && it.weekDays.size == 7 },
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    showSetupDialog = true
                },
                text = stringResource(Res.string.oraliq_qoshish),
                endingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.add_square),
                        contentDescription = "",
                    )
                }

            )
            SpaceSmall()

            CustomButton(
                text = stringResource(Res.string.saqlash),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonHeight),
                onClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewScheduleTimeListScreen() {
    ScheduleUsageLimitListUi(
        navController = rememberNavController(),
        state = ScheduleTimeState(),
        event = {}
    )
}