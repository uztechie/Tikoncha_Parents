package uz.tikoncha_parent.presentation.home

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
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
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.presentation.notification.NotificationScreen
import uz.tikoncha_parent.ui.theme.extendedColor
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText


class HomeScreen : Screen {

    @Composable
    override fun Content() {

        val viewModel = koinViewModel<HomeViewModel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val navigator = LocalNavigator.current

        HomeUi(
            navigator = navigator,
            state = state.value,
            event = event
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeUi(
    navigator: Navigator?,
    state: HomeState,
    event: (HomeEvent) -> Unit
) {
    val rootNavigator = navigator?.parent

    LaunchedEffect(true){
        event(HomeEvent.GetChildren)
    }

    val bottomRoundedShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = ShapeCornerRadius,
        bottomEnd = ShapeCornerRadius
    )



    val appUsageLoading = state.appUsageResponseState is ResponseState.Loading
    val appUsageErrorText = state.appUsageResponseState.errorText()
    val appUsageSuccess = state.appUsageResponseState is ResponseState.Success


    val createRuleLoading = state.createRuleResponseState is ResponseState.Loading
    val createRuleErrorText = state.createRuleResponseState.errorText()
    val createRuleSuccess = state.createRuleResponseState is ResponseState.Success

    val childrenLoading = state.childrenResponseState is ResponseState.Loading
    val childrenErrorText = state.childrenResponseState.errorText()


    LoadingDialog(appUsageLoading)
    LoadingDialog(createRuleLoading)

    var showAppUsageErrorDialog by remember {
        mutableStateOf(false)
    }
    var showCreateRuleDialog by remember {
        mutableStateOf(false)
    }


    LaunchedEffect(appUsageErrorText) {
        if (appUsageErrorText.isNotEmpty()) {
            showAppUsageErrorDialog = true
        }
    }

    LaunchedEffect(createRuleErrorText) {
        if (createRuleErrorText.isNotEmpty()) {
            showCreateRuleDialog = true
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

    CustomDialog(
        onDismiss = {showCreateRuleDialog = false},
        show = showCreateRuleDialog,
        title = stringResource(Res.string.xatolik),
        message = createRuleErrorText,
        onButtonClick = {
            showCreateRuleDialog = false
        }
    )




    var showDialog by remember {
        mutableStateOf(false)
    }

    var selectionTypeIndex by remember {
        mutableIntStateOf(0)
    }
    var selectionType by remember {
        mutableStateOf(DateSelectionType.WEEK)
    }

    CustomListDialog(
        title = stringResource(Res.string.farzandlaringiz),
        items = state.childrenList,
        show = showDialog,
        loading = childrenLoading,
        errorMessage = childrenErrorText,
        onItemSelected = {
            event(HomeEvent.OnChildSelected(it))
        },
        onDismiss = {
            showDialog = false
        }
    )

    var isRefreshing by remember { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            refreshScope.launch {
                isRefreshing = true
                event(HomeEvent.GetAppUsage)
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(HeaderHeight)
                    .shadow(
                        elevation = 4.dp,
                        shape = bottomRoundedShape,
                        ambientColor = MaterialTheme.extendedColor.shadowColor, // 🌈 Soya rangi shu yerda
                        spotColor = MaterialTheme.extendedColor.shadowColor     // Android 12+ uchun
                    )
                    .padding(bottom = 4.dp),
                shape = bottomRoundedShape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.extendedColor.backgroundColor
                ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(horizontal = ContainerPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    FilledTonalIconButton(
                        modifier = Modifier
                            .size(NormalIconButtonSize),
                        onClick = { },
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.extendedColor.buttonColor,
                            contentColor = MaterialTheme.extendedColor.onBackgroundColor
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.chart),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(NormalIconButtonPadding)
                        )
                    }

                    SpaceMedium()
                    CustomText(
                        text = stringResource(Res.string.bosh_sahifa),
                        fontSize = LargeTextSize,
                        maxLines = 1
                    )


                    Spacer(Modifier.weight(1f))

                    FilledTonalIconButton(
                        modifier = Modifier
                            .size(LargeIconButtonSize),
                        onClick = {
                            rootNavigator?.push(NotificationScreen())
                        },
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.extendedColor.buttonColor,
                            contentColor = MaterialTheme.extendedColor.onBackgroundColor
                        ),
                        shape = CircleShape
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.notification),
                            contentDescription = "",
                            tint = PrimaryColor,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(LargeIconButtonPadding)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = ContainerPadding,
                        end = ContainerPadding,
                        bottom = ContainerPadding,
                        top = NormalIconButtonPadding
                    )
                    .verticalScroll(rememberScrollState())
            ) {

                CustomText(
                    text = stringResource(Res.string.farzandlaringiz_telefon_ishlatish_statistikasi),
                    color = MaterialTheme.extendedColor.hintColor,
                    fontSize = NormalTextSize,
                    modifier = Modifier.fillMaxWidth()
                )

                SpaceUltraSmall()

                CustomSelectionButton(
                    label = stringResource(Res.string.farzandlaringiz),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(TextFieldHeight),
                    text = state.selectedChildren?.name?:"",
                    painter = painterResource(Res.drawable.profile),
                    onClick = {
                        showDialog = true
                    }
                )

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
                        event(HomeEvent.GetUsageList(it, selectionType))
                    },
                    onLastItemSelected = {
                        event(HomeEvent.TodaySelected(today = it && selectionType == DateSelectionType.DAY))
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
                            event(HomeEvent.OnLockClicked(it))
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
fun Pre() {

    HomeUi(
        navigator = null,
        state = HomeState(),
        event = {}
    )
}

