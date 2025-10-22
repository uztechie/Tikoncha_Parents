package uz.tikoncha_parent.presentation.home.schedule.timelist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.add_square
import tikoncha_parents.composeapp.generated.resources.faol_vaqt
import tikoncha_parents.composeapp.generated.resources.oraliq_qoshish
import tikoncha_parents.composeapp.generated.resources.qoshish
import tikoncha_parents.composeapp.generated.resources.saqlash
import tikoncha_parents.composeapp.generated.resources.vazifa_qo_shish
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.CustomOutlinedButton
import uz.tikoncha_parent.presentation.base.bottomShadow
import uz.tikoncha_parent.ui.ButtonCornerRadius
import uz.tikoncha_parent.ui.ButtonHeight
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.DialogButtonHeight
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.theme.extendedColor



class ScheduleTimeListScreen(): Screen {

    @Composable
    override fun Content() {

        val vieModel = koinViewModel<ScheduleTimeViewModel>()
        val state by vieModel.state.collectAsStateWithLifecycle()
        val event = vieModel::event
        ScheduleTimeListUi(
            state = state,
            event = event
        )


    }

}


@Composable
fun ScheduleTimeListUi(
    state: ScheduleTimeState,
    event: (ScheduleTimeEvent) -> Unit
)
{

    var showSetupDialog by remember {
        mutableStateOf(false)
    }


    ScheduleSetupTimeDialog(
        show = showSetupDialog,
        state = state,
        event = event,
        onDismiss = {showSetupDialog = false}
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {

        CustomHeader(
            modifier = Modifier
                .zIndex(1f),
            title = stringResource(Res.string.faol_vaqt),
            showBackButton = true,
            onBackClick = {}
        )



        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            contentPadding = PaddingValues(
                ContainerPadding
            )
        ) {
            items(state.timeList) {
                ScheduleTimeItem(
                    modifier = Modifier
                        .clickable(
                            interactionSource = null,
                            indication = null,
                            onClick = {
                                event(ScheduleTimeEvent.SetTimeData(
                                    it
                                ))
                                showSetupDialog = true
                            }
                        ),
                    item = it,
                    onRemove = {
                        event(ScheduleTimeEvent.RemoveTime(it))
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
//                    color = Color.Red
                )
                .bottomShadow(
                    shape = RoundedCornerShape(
                        topStart = ButtonCornerRadius,
                        topEnd = ButtonCornerRadius
                    ),
                    color = MaterialTheme.extendedColor.backgroundColor,
                    lowerOffset = - 5.dp,
                    radius = 10.dp

                )
                .background(MaterialTheme.extendedColor.backgroundColor)
                .padding(start = ContainerPadding, end = ContainerPadding, bottom = ContainerPadding)


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
                onClick = {}
            )

        }



    }
}





