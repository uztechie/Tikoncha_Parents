package uz.tikoncha_parent.presentation.home.schedule.timelist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.add_square
import tikoncha_parents.composeapp.generated.resources.faol_vaqt
import tikoncha_parents.composeapp.generated.resources.qoshish
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.CustomOutlinedButton
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.DialogButtonHeight
import uz.tikoncha_parent.ui.PrimaryColor
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
            title = stringResource(Res.string.faol_vaqt),
            showBackButton = true,
            onBackClick = {}
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(ContainerPadding)
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(state.timeList) {
                    ScheduleTimeItem(
                        item = it,
                        onRemove = {
                            event(ScheduleTimeEvent.RemoveTime(it))
                        }
                    )
                }
            }

            CustomOutlinedButton(
                text = stringResource(Res.string.qoshish),
                onClick = {
                    showSetupDialog = true
                },
                modifier = Modifier
                    .height(DialogButtonHeight)
                    .align(Alignment.BottomEnd),
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.add_square),
                        contentDescription = "",
                        tint = PrimaryColor
                    )
                }
            )



        }
    }
}





