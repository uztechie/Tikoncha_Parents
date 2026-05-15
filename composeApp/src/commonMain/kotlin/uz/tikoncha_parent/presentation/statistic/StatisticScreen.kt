package uz.tikoncha_parent.presentation.statistic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.LoadingDialog
import uz.tikoncha_parent.presentation.base.SegmentedToggle
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.platform.openUrl
import uz.tikoncha_parent.presentation.add_child.AddChildScreen
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.ChildSelectionButton
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.PermissionWarningCard
import uz.tikoncha_parent.presentation.new_home.SelectionChildBottomSheet
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.rememberScreenSystemBars


class StatisticScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current ?: return

        val viewModel = navigator.koinNavigatorScreenModel<StatisticViewModel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        StatisticUi(
            navigator = navigator,
            state = state.value,
            event = event
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class, InternalVoyagerApi::class)
@Composable
fun StatisticUi(
    navigator: Navigator?,
    state: StatisticState,
    event: (StatisticEvent) -> Unit
) {

    val refreshScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }
    val appUsageErrorText = state.appUsageResponseState.errorText()
    var selectionTypeIndex by remember { mutableIntStateOf(0) }
    var showAppUsageErrorDialog by remember { mutableStateOf(false) }
    val appUsageLoading = state.appUsageResponseState is ResponseState.Loading
    var selectionType by remember { mutableStateOf(DateSelectionType.WEEK) }

    val systemBars = rememberScreenSystemBars(
        statusBarColor = AppColors.bg.secondary,
        navigationBarColor = AppColors.bg.secondary
    )

    LoadingDialog(appUsageLoading && !isRefreshing)


    LaunchedEffect(Unit) {
        event(StatisticEvent.GetChildren)
//        event(StatisticEvent.RefreshChild)
//        event(StatisticEvent.GetAppUsage)
    }


    LaunchedEffect(appUsageErrorText) {
        if (appUsageErrorText.isNotEmpty()) {
            showAppUsageErrorDialog = true
        }
    }

    if (showDialog) {
        SelectionChildBottomSheet(
            navigator = navigator,
            items = state.childrenList,
            selectedItem = state.selectedChild,
            onDismiss = { showDialog = false },
            title = stringResource(Res.string.farzandlaringiz),
            onItemSelected = {
                event(StatisticEvent.OnChildSelected(it))
                showDialog = false
            }
        )
    }

    CustomDialog(
        painter = painterResource(Res.drawable.dialog_failed),
        onDismiss = { showAppUsageErrorDialog = false },
        show = showAppUsageErrorDialog,
        title = stringResource(Res.string.xatolik),
        message = appUsageErrorText,
        onButtonClick = {
            showAppUsageErrorDialog = false
        }
    )

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            refreshScope.launch {
                isRefreshing = true
                event(StatisticEvent.RefreshChild)
                event(StatisticEvent.GetAppUsage)
                delay(500)
                isRefreshing = false
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(systemBars.modifier)
                .background(AppColors.bg.secondary)
        ) {
            CustomHeader(
                title = stringResource(Res.string.statistika),
                showBackButton = true,
                onBackClick = {
                    navigator?.pop()
                },
                trailingIcon = {
                    ChildSelectionButton(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .widthIn(120.dp, 160.dp),
                        text = state.selectedChild?.name ?: "",
                        imageUrl = state.selectedChild?.avatarUrl ?: "",
                        label = stringResource(Res.string.farzandingizni_tanlang),
                        onClick = {
                            if (state.childrenList.isEmpty()) {
                                navigator?.push(AddChildScreen())
                            } else {
                                showDialog = true
                            }
                        },
                    )
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = ContainerPadding,
                    )
                    .verticalScroll(rememberScrollState())
            ) {

                if (state.permissionIssueList.isNotEmpty()) {
                    Space(12.dp)
                    state.permissionIssueList.forEach {
                        PermissionWarningCard(
                            title = it.title,
                            body = it.body,
                            videoUrl = it.video_url,
                            onVideoClick = {
                                openUrl(it)
                            }
                        )
                        Space(12.dp)
                    }
                }
                else{
                    Space(12.dp)
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppColors.bg.surface, RoundedCornerShape(TextFieldCornerRadius))
                        .padding(ContainerPadding)
                ) {
                    SegmentedToggle(
                        containerColor = AppColors.bg.secondarySurface,
                        options = listOf(
                            stringResource(Res.string.haftalik) to null,
                            stringResource(Res.string.kunlik) to null
                        ),
                        selectedIndex = selectionTypeIndex,
                        modifier = Modifier
                            .fillMaxWidth(),
                        onOptionSelected = {
                            selectionTypeIndex = it
                            selectionType =
                                if (it == 0) DateSelectionType.WEEK else DateSelectionType.DAY
                        }
                    )
                    SpaceMedium()

//                    val averageTime = if (selectionType == DateSelectionType.DAY) {
//                        val formatTime = state.averageUsageTime
//                        buildList {
//
//                            if (state.isTodaySelected) {
//                                add(stringResource(Res.string.bugun))
//                            }
//                            if (formatTime.hour > 0) {
//                                add("${formatTime.hour} ${stringResource(Res.string.soat)}")
//                            }
//                            if (formatTime.minute > 0) {
//                                add("${formatTime.minute} ${stringResource(Res.string.daqiqa)}")
//                            }
//                        }.joinToString(" ")
//                    } else {
//
//                        val formatTime = state.averageUsageTime
//                        val usageTime = buildList {
//                            if (formatTime.hour > 0) {
//                                add("${formatTime.hour} ${stringResource(Res.string.soat)}")
//                            }
//                            if (formatTime.minute > 0) {
//                                add("${formatTime.minute} ${stringResource(Res.string.daqiqa)}")
//                            }
//                        }.joinToString(" ")
//
//                        "${stringResource(Res.string.bir_kunda_o_rtacha)} $usageTime"
//                    }
                    val sliderTimeText = if (selectionType == DateSelectionType.DAY) {
                        val f = state.averageUsageTime
                        buildList {
                            if (state.isTodaySelected) add(stringResource(Res.string.bugun))
                            if (f.hour > 0) add("${f.hour} ${stringResource(Res.string.soat)}")
                            if (f.minute > 0) add("${f.minute} ${stringResource(Res.string.daqiqa)}")
                        }.ifEmpty { listOf("0 ${stringResource(Res.string.daqiqa)}") }
                            .joinToString(" ")
                    } else {
                        val totalMinutes = state.weeklyChartData.values.sum().toInt()
                        val hours = totalMinutes / 60
                        val minutes = totalMinutes % 60

                        buildList {
                            if (hours > 0) add("$hours ${stringResource(Res.string.soat)}")
                            if (minutes > 0) add("$minutes ${stringResource(Res.string.daqiqa)}")
                        }.ifEmpty { listOf("0 ${stringResource(Res.string.daqiqa)}") }
                            .joinToString(" ")
                    }

                    DateSelectorSlider(
                        type = selectionType,
                        averageTimeText = sliderTimeText,
                        periodsDate = if (selectionType == DateSelectionType.WEEK) state.weeklyPeriods else state.dailyPeriods,
                        onDateSelected = {
                            event(StatisticEvent.GetUsageList(it, selectionType))
                        }
                    )

                    SpaceLarge()

                    val data = if (selectionType == DateSelectionType.WEEK) normalizeWeeklyKeys(state.weeklyChartData) else state.dailyChartData
                    UsageBarChart(
                        data = data,
                        isWeekly = selectionType == DateSelectionType.WEEK,
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                    )

                    if (selectionType == DateSelectionType.WEEK && state.selectedPeriod != null) {
                        val dailyMinutes = state.weeklyChartData.values
                        val totalMinutes = dailyMinutes.sum().toInt()
                        val activeDays = dailyMinutes.count { it > 0.0 }

                        val avgPerDay = if (activeDays > 0) totalMinutes / activeDays else 0
                        val hours = avgPerDay / 60
                        val minutes = avgPerDay % 60

                        val usageTime = buildList {
                            if (hours > 0) add("$hours ${stringResource(Res.string.soat)}")
                            if (minutes > 0) add("$minutes ${stringResource(Res.string.daqiqa)}")
                        }.ifEmpty { listOf("0 ${stringResource(Res.string.daqiqa)}") }
                            .joinToString(" ")

                        val weeklyAvgText = "${stringResource(Res.string.bir_kunda_o_rtacha)} $usageTime"

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = weeklyAvgText,
                                style = AppTypography.titleMdMedium,
                                color = AppColors.text.secondary,
                            )
                        }
                    }
                }
                SpaceMedium()

                CustomText(
                    text = stringResource(Res.string.eng_kop_foydalanilgan),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = NormalLargeTextSize
                )
                SpaceSmall()

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppColors.bg.surface, RoundedCornerShape(TextFieldCornerRadius))
                        .padding(ContainerPadding)
                ) {
                    state.appUsageUiList.forEach { item ->
                        AppUsageItem(appUsageUi = item)
                        SpaceUltraSmall()
                        DividerHorizontal()
                    }
                }
            }
        }
    }
}

private fun formatDate(date: LocalDate): String {
    val d = date.day.toString().padStart(2, '0')
    val m = date.month.number.toString().padStart(2, '0')
    return "$d.$m.${date.year}"
}

@Preview
@Composable
private fun Pre() {
    TikonchaParentTheme(
        ThemeMode.LIGHT
    ) {
        StatisticUi(
            navigator = null,
            state = StatisticState(),
            event = {}
        )
    }
}

