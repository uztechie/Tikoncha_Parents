package uz.tikoncha_parent.presentation.home.schedule.type

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.home.schedule.timelist.ScheduleTimeEvent
import uz.tikoncha_parent.presentation.home.schedule.timelist.ScheduleTimeListScreen
import uz.tikoncha_parent.presentation.home.schedule.timelist.ScheduleTimeState
import uz.tikoncha_parent.presentation.home.schedule.timelist.ScheduleTimeViewModel
import uz.tikoncha_parent.presentation.home.schedule.timelist.ScheduleUsageLimitDialog
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.theme.extendedColor

class ScheduleTypeScreen : Screen {
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
fun ScheduleTypeUi(
    navigator: Navigator?,
    state: ScheduleTimeState,
    event: (ScheduleTimeEvent) -> Unit
) {


    val scheduleList = listOf(
        ScheduleItem(
            type = ScheduleType.TIME,
            icon = painterResource(Res.drawable.clock),
            title = stringResource(Res.string.vaqt),
            subtitle = stringResource(Res.string.ish_vaqti_dam_olish_kuni)
        ),
        ScheduleItem(
            type = ScheduleType.LOCATION,
            icon = painterResource(Res.drawable.permission_location),
            title = stringResource(Res.string.joylashuv),
            subtitle = stringResource(Res.string.ish_vaqti_dam_olish_kuni)
        ),
        ScheduleItem(
            type = ScheduleType.WIFI,
            icon = painterResource(Res.drawable.wi_fi),
            title = stringResource(Res.string.wi_fi),
            subtitle = stringResource(Res.string.ish_vaqti_dam_olish_kuni)
        ),
        ScheduleItem(
            type = ScheduleType.LAUNCH_COUNT,
            icon = painterResource(Res.drawable.icon_of),
            title = stringResource(Res.string.ishga_tushirishlar_soni),
            subtitle = stringResource(Res.string.ish_vaqti_dam_olish_kuni)
        ),
        ScheduleItem(
            type = ScheduleType.USAGE_LIMIT,
            icon = painterResource(Res.drawable.clock),
            title = stringResource(Res.string.foydalanish_chegarasi),
            subtitle = stringResource(Res.string.ish_vaqti_dam_olish_kuni)
        ),
    )

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
            title = stringResource(Res.string.jadvallar),
            showBackButton = true,
            onBackClick = { },
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            CustomText(
                text = stringResource(Res.string.bloklash_shartlari),
                fontSize = LargeTextSize,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(
                    horizontal = ContainerPadding
                )
            )

            SpaceMedium()

            CustomText(
                text = stringResource(Res.string.qachon_va_qayerda),
                fontSize = NormalTextSize,
                fontWeight = FontWeight.SemiBold,
                color = HintTextColor,
                modifier = Modifier.padding(
                    horizontal = ContainerPadding
                )
            )

            SpaceMedium()
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(
                    ContainerPadding
                ),
                content = {
                    items(items = scheduleList, key = { it.type }) { item ->
                        val isActiveStyle = state.enabledByType[item.type] == true

                        val isClickable = when(item.type) {
                            ScheduleType.TIME -> !isActiveStyle
                            ScheduleType.LOCATION -> true
                            ScheduleType.WIFI -> true
                            ScheduleType.LAUNCH_COUNT -> true
                            ScheduleType.USAGE_LIMIT -> !isActiveStyle
                        }
                        ScheduleTypeItem(
                            enabled = isActiveStyle,
                            painter = item.icon,
                            title = item.title,
                            subTitle = item.subtitle,
                            modifier = Modifier.clickable(
                                enabled = isClickable,
                                interactionSource = null,
                                indication = null,
                                onClick = {
                                    when(item.type){
                                        ScheduleType.TIME -> {
                                            navigator?.push(ScheduleTimeListScreen())
                                        }
                                        ScheduleType.LOCATION -> {}
                                        ScheduleType.WIFI -> {}
                                        ScheduleType.LAUNCH_COUNT -> {}
                                        ScheduleType.USAGE_LIMIT -> {
                                            showSetupDialog = true
                                        }
                                    }
                                }
                            ),
                        )
                    }
                }
            )
        }
    }
}