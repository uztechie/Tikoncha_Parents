package uz.tikoncha_parent.presentation.new_home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.farzandlaringiz
import tikoncha_parents.composeapp.generated.resources.*
import tikoncha_parents.composeapp.generated.resources.instagram_icon
import tikoncha_parents.composeapp.generated.resources.linkedin_icon
import tikoncha_parents.composeapp.generated.resources.notification
import tikoncha_parents.composeapp.generated.resources.profile
import tikoncha_parents.composeapp.generated.resources.whatsapp_icon
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.domain.model.HourMinute
import uz.tikoncha_parent.platform.HandleUpdateEffect
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.platform.openUrl
import uz.tikoncha_parent.presentation.add_child.AddChildScreen
import uz.tikoncha_parent.presentation.base.ChildSelectionButton
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.NoInternetDialog
import uz.tikoncha_parent.presentation.base.rememberInternetCheck
import uz.tikoncha_parent.presentation.base.simpleShadow
import uz.tikoncha_parent.presentation.base.singleClick
import uz.tikoncha_parent.presentation.chat.chat_list.ChatScreen
import uz.tikoncha_parent.presentation.in_app_update.InAppUpdateCard
import uz.tikoncha_parent.presentation.in_app_update.InAppUpdateDialog
import uz.tikoncha_parent.presentation.in_app_update.UpdateEvent
import uz.tikoncha_parent.presentation.in_app_update.UpdateUiState
import uz.tikoncha_parent.presentation.in_app_update.UpdateViewModel
import uz.tikoncha_parent.presentation.map.MapScreen
import uz.tikoncha_parent.presentation.new_home.logout.ParentRequestScreen
import uz.tikoncha_parent.presentation.notification.NotificationScreen
import uz.tikoncha_parent.presentation.policy.policy_list.PolicyListScreen
import uz.tikoncha_parent.presentation.profile.ProfileScreen
import uz.tikoncha_parent.presentation.statistic.StatisticEvent
import uz.tikoncha_parent.presentation.statistic.StatisticScreen
import uz.tikoncha_parent.presentation.statistic.StatisticState
import uz.tikoncha_parent.presentation.statistic.StatisticViewModel
import uz.tikoncha_parent.presentation.task.TaskScreen
import uz.tikoncha_parent.presentation.tracking.TrackingScreen
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.CardCornerPadding
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.HomeIconSize
import uz.tikoncha_parent.ui.HomeItemHeight
import uz.tikoncha_parent.ui.LargeCardCornerRadius
import uz.tikoncha_parent.ui.LargeTextSize
import uz.tikoncha_parent.ui.NormalIconSize
import uz.tikoncha_parent.ui.OtpErrorColor
import uz.tikoncha_parent.ui.SmallIconSize
import uz.tikoncha_parent.ui.SmallTextSize
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.SpaceUltraSmall
import uz.tikoncha_parent.ui.TextFieldCornerRadius
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor
import uz.tikoncha_parent.ui.theme.rememberScreenSystemBars


class NewHomeScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current ?: return


        val viewModel = navigator.koinNavigatorScreenModel<HomeViewModel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val statisticViewModel = navigator.koinNavigatorScreenModel<StatisticViewModel>()
        val statisticState = statisticViewModel.state.collectAsStateWithLifecycle()
        val statisticEvent = statisticViewModel::onEvent

        val updateViewModel = koinViewModel<UpdateViewModel>()
        val updateState by updateViewModel.state.collectAsStateWithLifecycle()
        val updateEvent = updateViewModel::onEvent

        LaunchedEffect(Unit) {
            event(HomeEvent.SyncSelectedChildFromSettings)
            event(HomeEvent.GetChildren)
            updateEvent(UpdateEvent.ScreenStarted)
        }




        HandleUpdateEffect(updateViewModel)

        LaunchedEffect(state.value.selectedChild) {
            Logger.d(
                "HomeViewModel", "homeScreen " +
                        "selectedChild=${state.value.selectedChild}"
            )
            state.value.selectedChild?.let { child ->
                statisticEvent(StatisticEvent.OnChildSelected(child))
            }
        }

        Logger.d("NewHomeScreen", "Content")

        NewHomeUi(
            navigator = navigator,
            state = state.value,
            event = event,
            statisticState = statisticState.value,
            statisticEvent = statisticEvent,
            appUpdateState = updateState,
            appUpdateEvent = updateEvent
        )
    }
}

@Composable
fun NewHomeUi(
    navigator: Navigator?,
    state: HomeState,
    event: (HomeEvent) -> Unit,
    statisticState: StatisticState,
    statisticEvent: (StatisticEvent) -> Unit = {},
    appUpdateState: UpdateUiState = UpdateUiState(),
    appUpdateEvent: (UpdateEvent) -> Unit = {},
) {
    LaunchedEffect(Unit) {
        event(HomeEvent.RefreshParentRequest)
    }

    val count = state.parentRequestCount
    val taskCount = state.activeTaskCount
    val tableCount = state.parentPolicyCount
    val refreshScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }
    var showChildDialog by remember { mutableStateOf(false) }

    val systemBars = rememberScreenSystemBars(
        statusBarColor = AppColors.bg.page,
        navigationBarColor = AppColors.bg.page
    )

    val internetCheck = rememberInternetCheck(refreshScope)
    NoInternetDialog(internetCheck)

    if (showDialog) {
        SelectionChildBottonSheet(
            navigator = navigator,
            items = state.childrenList,
            selectedItem = state.selectedChild,
            onDismiss = { showDialog = false },
            title = stringResource(Res.string.farzandlaringiz),
            onItemSelected = {
                event(HomeEvent.OnChildSelected(it))
                showDialog = false
            }
        )
    }

    CustomDialog(
        show = showChildDialog,
        title = stringResource(Res.string.diqqat),
        buttonText = stringResource(Res.string.farzand_qo_shish),
        message = stringResource(Res.string.farzand_malumotlari_keyin_korinadi),
        onDismiss = { showChildDialog = false },
        onButtonClick = {
            navigator?.push(AddChildScreen())
            showChildDialog = false
        }
    )

    InAppUpdateDialog(
        show = appUpdateState.showUpdateDialog,
        state = appUpdateState,
        onDismiss = { appUpdateEvent(UpdateEvent.DismissUpdateDialog) },
        onConfirm = { type -> appUpdateEvent(UpdateEvent.StartUpdateClicked(type)) }
    )

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            internetCheck.check {
                isRefreshing = true
                event(HomeEvent.GetChildren)
                statisticEvent(StatisticEvent.RefreshChild)
                statisticEvent(StatisticEvent.GetAppUsage)
                delay(500)
                isRefreshing = false
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(systemBars.modifier)
                .background(AppColors.bg.page)
        ) {
            Row(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                ChildSelectionButton(
                    text = state.selectedChild?.name ?: "",
                    imageUrl = state.selectedChild?.avatarUrl ?: "",
                    label = stringResource(Res.string.farzand_qo_shish),
                    trailingIcon = state.childrenList.isNotEmpty(),
                    modifier = Modifier
                        .height(40.dp)
                        .widthIn(120.dp, 160.dp),
                    onClick = {
                        if (state.childrenList.isEmpty()) {
                            navigator?.push(AddChildScreen())
                        } else {
                            showDialog = true
                        }
                    },
                )
                Spacer(Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(44.dp)
                        .background(AppColors.bg.surfaceTertiary)
                        .singleClick {
                            openUrl("https://t.me/tikoncha_support")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.support_icon),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(MaterialTheme.extendedColor.primaryAlphaColor),
                        modifier = Modifier
                            .size(NormalIconSize)
                    )
                }
                SpaceUltraSmall()
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(44.dp)
                        .background(AppColors.bg.surfaceTertiary)
                        .singleClick {
                            navigator?.push(NotificationScreen())
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.notification),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(MaterialTheme.extendedColor.primaryAlphaColor),
                        modifier = Modifier
                            .size(NormalIconSize)
                    )
                }
                SpaceUltraSmall()
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(44.dp)
                        .background(AppColors.bg.surfaceTertiary)
                        .singleClick {
                            navigator?.push(ProfileScreen())
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.profile),
                        contentDescription = "",
                        modifier = Modifier
                            .size(NormalIconSize),
                        colorFilter = ColorFilter.tint(MaterialTheme.extendedColor.primaryAlphaColor)
                    )
                }
            }



            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {

                item {
                    if (count > 0) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .simpleShadow(RoundedCornerShape(CardCornerRadius))
                                .background(
                                    MaterialTheme.extendedColor.cardColor,
                                    RoundedCornerShape(CardCornerRadius)
                                )
                                .clip(RoundedCornerShape(CardCornerRadius))
                                .clickable {
                                    navigator?.push(ParentRequestScreen())
                                }
                                .padding(horizontal = CardCornerPadding, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CustomText(
                                text = stringResource(Res.string.sorovlar),
                                fontSize = LargeTextSize,
                                modifier = Modifier.weight(1f)
                            )
                            if (count > 0) {
                                Box(
                                    modifier = Modifier
                                        .background(OtpErrorColor, CircleShape)
                                        .size(24.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CustomText(
                                        text = if (count > 99) "99" else count.toString(),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.W600),
                                        maxLines = 1,
                                        fontSize = SmallTextSize
                                    )
                                }
                            }
                        }
                        Space(12.dp)
                    }
                }

                item {
                    InAppUpdateCard(
                        modifier = Modifier
                            .padding(bottom = 16.dp),
                        state = appUpdateState,
                        event = appUpdateEvent
                    )
                }

                item {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(LargeCardCornerRadius))
                            .fillMaxWidth()
                            .height(HomeItemHeight)
                            .background(
                                AppColors.section.tertiary,
                                RoundedCornerShape(LargeCardCornerRadius)
                            )
                            .singleClick {
                                if (state.childrenList.isEmpty()) {
                                    internetCheck.check {
                                        showChildDialog = true
                                    }
                                } else {
                                    navigator?.push(StatisticScreen())
                                }
                            }
                            .padding(CardCornerPadding),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {

                            val statUsageTime = buildString {
                                append(
                                    when {
                                        statisticState.todayUsage.hour == 0 && statisticState.todayUsage.minute == 0 -> {
                                            "0 ${stringResource(Res.string.daq)}"
                                        }

                                        statisticState.todayUsage.hour > 0 && statisticState.todayUsage.minute == 0 -> {
                                            "${statisticState.todayUsage.hour} ${stringResource(Res.string.soat)}"
                                        }

                                        statisticState.todayUsage.hour > 0 && statisticState.todayUsage.minute > 0 -> {
                                            "${statisticState.todayUsage.hour} ${stringResource(Res.string.s)}" +
                                                    ", ${statisticState.todayUsage.minute} ${
                                                        stringResource(
                                                            Res.string.d
                                                        )
                                                    }"
                                        }

                                        statisticState.todayUsage.hour == 0 && statisticState.todayUsage.minute > 0 -> {
                                            "${statisticState.todayUsage.minute} ${
                                                stringResource(
                                                    Res.string.daq
                                                )
                                            }"
                                        }

                                        else -> {
                                            "0 ${stringResource(Res.string.daq)}"
                                        }
                                    }
                                )
                            }

                            Text(
                                text = statUsageTime,
                                color = AppColors.text.primary,
                                style = AppTypography.displaySmRegular
                            )
                            Text(
                                text = stringResource(Res.string.bugun_sarfladi),
                                color = AppColors.text.secondary,
                                style = AppTypography.titleSmMedium,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight(0.5f)
                                    .background(
                                        MaterialTheme.extendedColor.backgroundColor,
                                        RoundedCornerShape(LargeCardCornerRadius)
                                    )
                                    .padding(6.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                Image(
                                    painter = painterResource(Res.drawable.linkedin_icon),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(SmallIconSize),
                                    alignment = Alignment.BottomCenter,
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight(0.65f)
                                    .background(
                                        MaterialTheme.extendedColor.backgroundColor,
                                        RoundedCornerShape(TextFieldCornerRadius)
                                    )
                                    .padding(6.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                Image(
                                    painter = painterResource(Res.drawable.whatsapp_icon),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(SmallIconSize),
                                    alignment = Alignment.BottomCenter,
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight(0.8f)
                                    .background(
                                        MaterialTheme.extendedColor.backgroundColor,
                                        RoundedCornerShape(TextFieldCornerRadius)
                                    )
                                    .padding(6.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                Image(
                                    painter = painterResource(Res.drawable.instagram_icon),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(SmallIconSize),
                                    alignment = Alignment.BottomCenter,
                                )
                            }
                        }
                    }
                    Space(12.dp)
                }

                item {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(LargeCardCornerRadius))
                            .fillMaxWidth()
                            .height(HomeItemHeight)
                            .background(
                                AppColors.section.tertiary,
                                RoundedCornerShape(LargeCardCornerRadius)
                            )
                            .singleClick {
                                if (state.childrenList.isEmpty()) {
                                    internetCheck.check {
                                        showChildDialog = true
                                    }
                                } else {
                                    navigator?.push(TaskScreen())
                                }

                            }
                            .padding(horizontal = CardCornerPadding, vertical = ContainerPadding),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text(
                                text = stringResource(Res.string.topshiriqlar),
                                color = AppColors.text.primary,
                                style = AppTypography.displaySmRegular
                            )
                            Text(
                                text = stringResource(Res.string.faol_vazifa, taskCount),
                                color = AppColors.text.secondary,
                                style = AppTypography.titleSmMedium,
                            )
                        }

                        Image(
                            painter = painterResource(Res.drawable.home_task),
                            contentDescription = "",
                            modifier = Modifier.size(HomeIconSize)
                        )
                    }
                    Space(12.dp)
                }

                item {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(LargeCardCornerRadius))
                            .fillMaxWidth()
                            .height(HomeItemHeight)
                            .background(
                                AppColors.section.tertiary,
                                RoundedCornerShape(LargeCardCornerRadius)
                            )
                            .singleClick {
                                if (state.childrenList.isEmpty()) {
                                    internetCheck.check {
                                        showChildDialog = true
                                    }
                                } else {
                                    navigator?.push(PolicyListScreen())
                                }
                            }
                            .padding(horizontal = CardCornerPadding, vertical = ContainerPadding),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text(
                                text = stringResource(Res.string.cheklovlar),
                                color = AppColors.text.primary,
                                style = AppTypography.displaySmRegular
                            )
                            Text(
                                text = stringResource(Res.string.ilova_cheklangan, tableCount),
                                color = AppColors.text.secondary,
                                style = AppTypography.titleSmMedium,
                            )
                        }

                        Image(
                            painter = painterResource(Res.drawable.home_table),
                            contentDescription = "",
                            modifier = Modifier.size(HomeIconSize)
                        )
                    }
                    Space(12.dp)
                }

                item {
                    NewHomeItem(
                        onSettingSelected = { selectionItem ->
                            when (selectionItem) {
                                HomeSelectionItem.XARITA -> {
                                    navigator?.push(TrackingScreen())
                                }

                                HomeSelectionItem.SIHBAT -> {
                                    navigator?.push(ChatScreen())
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Pre() {
    TikonchaParentTheme(
        ThemeMode.LIGHT
    ) {
        NewHomeUi(
            navigator = null,
            state = HomeState(),
            event = {},
            statisticState = StatisticState(
                todayUsage = HourMinute(1, 22)
            )
        )
    }
}