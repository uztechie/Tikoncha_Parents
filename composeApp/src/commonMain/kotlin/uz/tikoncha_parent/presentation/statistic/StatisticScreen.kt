package uz.tikoncha_parent.presentation.statistic

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.CustomSelectionButton
import uz.tikoncha_parent.presentation.base.LoadingDialog
import uz.tikoncha_parent.presentation.base.SegmentedToggle
import uz.tikoncha_parent.presentation.common.CustomListDialog
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.topShadow
import uz.tikoncha_parent.presentation.notification.NotificationScreen
import uz.tikoncha_parent.presentation.policy.PolicyListScreen
import uz.tikoncha_parent.ui.theme.extendedColor
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.theme.ThemeController.mode
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme


class StatisticScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current?:return

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

    val bottomRoundedShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = ShapeCornerRadius,
        bottomEnd = ShapeCornerRadius
    )



    val appUsageLoading = state.appUsageResponseState is ResponseState.Loading
    val appUsageErrorText = state.appUsageResponseState.errorText()
    val appUsageSuccess = state.appUsageResponseState is ResponseState.Success






    LoadingDialog(appUsageLoading)

    var showAppUsageErrorDialog by remember {
        mutableStateOf(false)
    }
    var showCreateRuleDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit){
        event(StatisticEvent.RefreshChild)
        event(StatisticEvent.GetAppUsage)
    }


    LaunchedEffect(appUsageErrorText) {
        if (appUsageErrorText.isNotEmpty()) {
            showAppUsageErrorDialog = true
        }
    }


    CustomDialog(
        onDismiss = {showAppUsageErrorDialog = false},
        show = showAppUsageErrorDialog,
        title = stringResource(Res.string.xatolik),
        message = appUsageErrorText,
        onButtonClick = {
            showAppUsageErrorDialog = false
        }
    )







    var selectionTypeIndex by remember {
        mutableIntStateOf(0)
    }
    var selectionType by remember {
        mutableStateOf(DateSelectionType.WEEK)
    }


    var isRefreshing by remember { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()

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
                .background(MaterialTheme.extendedColor.backgroundColor)
        )
        {
            CustomHeader(
                title = stringResource(Res.string.statistika),
                showBackButton = true,
                onBackClick = {
                    navigator?.pop()
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

                SpaceMedium()
                SegmentedToggle(
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

                DateSelectorSlider(
                    type = selectionType,
                    periodsDate = if (selectionType == DateSelectionType.WEEK) state.weeklyPeriods else state.dailyPeriods,
                    onDateSelected = {
                        event(StatisticEvent.GetUsageList(it, selectionType))
                    },
                    onLastItemSelected = {
                        event(StatisticEvent.TodaySelected(today = it && selectionType == DateSelectionType.DAY))
                    }
                )

                SpaceUltraSmall()

                val averageTime = if (selectionType == DateSelectionType.DAY) {
                    val formatTime = state.averageUsageTime
                    buildList<String> {

                        if (state.isTodaySelected) {
                            add(stringResource(Res.string.bugun))
                        }
                        if (formatTime.hour > 0) {
                            add("${formatTime.hour} ${stringResource(Res.string.soat)}")
                        }
                        if (formatTime.minute > 0) {
                            add("${formatTime.minute} ${stringResource(Res.string.daqiqa)}")
                        }
                    }.joinToString(" ")
                } else {

                    val formatTime = state.averageUsageTime
                    val usageTime = buildList<String> {
                        if (formatTime.hour > 0) {
                            add("${formatTime.hour} ${stringResource(Res.string.soat)}")
                        }
                        if (formatTime.minute > 0) {
                            add("${formatTime.minute} ${stringResource(Res.string.daqiqa)}")
                        }
                    }.joinToString(" ")

                    "${stringResource(Res.string.bir_kunda_o_rtacha)} $usageTime"
                }

                CustomText(
                    text = averageTime,
                    color = MaterialTheme.extendedColor.hintColor,
                    fontSize = UltraSmallTextSize,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                SpaceLarge()


                UsageBarChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    data = state.dailyChartData
                )

                SpaceMedium()

                CustomText(
                    text = stringResource(Res.string.eng_kop_foydalanilgan),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = NormalLargeTextSize
                )

                SpaceSmall()

                state.appUsageUiList.forEach { item ->
                    AppUsageItem(
                        appUsageUi = item,
                        onLockClick = {

                        }
                    )
                    SpaceUltraSmall()
                    DividerHorizontal()
                }
            }
        }
    }

}

@Preview
@Composable
private fun Pre() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        StatisticUi(
            navigator = null,
            state = StatisticState(),
            event = {}
        )
    }
}

