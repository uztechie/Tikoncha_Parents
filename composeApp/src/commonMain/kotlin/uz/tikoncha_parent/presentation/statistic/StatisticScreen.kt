@file:OptIn(ExperimentalMaterial3Api::class)

package uz.tikoncha_parent.presentation.statistic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.App
import uz.tikoncha_parent.platform.openUrl
import uz.tikoncha_parent.presentation.add_child.AddChildScreen
import uz.tikoncha_parent.presentation.base.*
import uz.tikoncha_parent.presentation.new_home.SelectionChildBottomSheet
import uz.tikoncha_parent.presentation.policy.common.SegmentedTabBar
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.*
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
        val state by viewModel.state.collectAsStateWithLifecycle()
        StatisticUi(navigator, state, viewModel::onEvent)
    }
}

@Composable
fun StatisticUi(
    navigator: Navigator?,
    state: StatisticState,
    event: (StatisticEvent) -> Unit,
) {
    val refreshScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    var showChildSheet by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }

    val appUsageErrorText = state.appUsageResponseState.errorText()
    val appUsageLoading = state.appUsageResponseState is ResponseState.Loading

    val systemBars = rememberScreenSystemBars(
        statusBarColor = AppColors.bg.secondary,
        navigationBarColor = AppColors.bg.secondary,
    )

    LoadingDialog(appUsageLoading && !isRefreshing)

    LaunchedEffect(Unit) { event(StatisticEvent.GetChildren) }

    LaunchedEffect(appUsageErrorText) {
        if (appUsageErrorText.isNotEmpty()) showErrorDialog = true
    }

    // Child selection sheet
    if (showChildSheet) {
        SelectionChildBottomSheet(
            navigator = navigator,
            items = state.childrenList,
            selectedItem = state.selectedChild,
            onDismiss = { showChildSheet = false },
            title = stringResource(Res.string.farzandlaringiz),
            onItemSelected = {
                event(StatisticEvent.OnChildSelected(it))
                showChildSheet = false
            }
        )
    }

    // Error dialog
    CustomDialog(
        painter = painterResource(Res.drawable.dialog_failed),
        onDismiss = { showErrorDialog = false },
        show = showErrorDialog,
        title = stringResource(Res.string.xatolik),
        message = appUsageErrorText,
        onButtonClick = { showErrorDialog = false }
    )

    // Bar click dialog
    UsageDetailsDialog(
        details = state.usageDetails,
        show = state.showUsageDetailsDialog,
        onDismiss = { event(StatisticEvent.DismissUsageDetailsDialog) }
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
                onBackClick = { navigator?.pop() },
                trailingIcon = {
                    ChildSelectionButton(
                        modifier = Modifier.widthIn(120.dp, 160.dp),
                        text = state.selectedChild?.name ?: "",
                        imageUrl = state.selectedChild?.avatarUrl ?: "",
                        label = stringResource(Res.string.farzandingizni_tanlang),
                        userInfo = state.selectedChild,
                        onClick = {
                            if (state.childrenList.isEmpty()) navigator?.push(AddChildScreen())
                            else showChildSheet = true
                        }
                    )
                }
            )

            // showBlur bo'lsa pastki blokga blur qo'llanadi
            val contentModifier = if (state.showBlur) Modifier.blur(10.dp) else Modifier

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Permission warnings
                if (state.permissionIssueList.isNotEmpty()) {
                    Space(12.dp)
                    state.permissionIssueList.forEach { issue ->
                        PermissionWarningCard(
                            title = issue.title,
                            body = issue.body,
                            videoUrl = issue.videoUrl,
                            onVideoClick = { openUrl(it) }
                        )
                        Space(12.dp)
                    }
                } else {
                    Space(12.dp)
                }

                /* ============ Chart card ============ */
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppColors.bg.surface, RoundedCornerShape(TextFieldCornerRadius))
                        .padding(ContainerPadding)
                        .then(contentModifier)
                ) {
                    // Toggle DAILY / WEEKLY
                    val selectedIdx = if (state.dateSelectionType == DateSelectionType.DAY) 0 else 1
                    PillSegmentedButton(
                        items = listOf(
                            PillSegmentedItem(
                                label = stringResource(Res.string.kunlik)
                            ),
                            PillSegmentedItem(
                                label = stringResource(Res.string.haftalik)
                            )
                        ),
                        selectedIndex = selectedIdx,
                        modifier = Modifier.fillMaxWidth(),
                        onSelected = { idx ->
                            val mode =
                                if (idx == 0) DateSelectionType.DAY else DateSelectionType.WEEK
                            event(StatisticEvent.ChangeMode(mode))
                        }
                    )

                    SpaceMedium()

                    // Pager
                    PagerBlock(
                        state = state,
                        onPageChange = { event(StatisticEvent.PageChanged(it)) }
                    )

                    SpaceLarge()

                    // Chart
                    UsageBarChart(
                        bars = state.bars,
                        mode = state.dateSelectionType,
                        chartSubtitle = state.selectedPage?.chartSubtitle,
                        onBarClick = { event(StatisticEvent.BarClicked(it)) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                SpaceMedium()

                /* ============ Top apps ============ */
                if (state.topApps.isNotEmpty()) {
                    Text(
                        text = stringResource(Res.string.eng_kop_foydalanilgan),
                        color = AppColors.text.primary,
                        style = AppTypography.titleMdSemiBold
                    )
                    SpaceSmall()

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                AppColors.bg.surface,
                                RoundedCornerShape(TextFieldCornerRadius)
                            )
                            .padding(horizontal = ContainerPadding)
                            .then(contentModifier)
                    ) {
                        state.topApps.forEachIndexed { i, app ->
                            TopAppItem(app = app)
                            if (i < state.topApps.lastIndex)
                                HorizontalDivider(
                                    thickness = 1.dp,
                                    color = AppColors.border.secondary
                                )
                        }
                    }
                }

                SpaceLarge()
            }
        }
    }
}

@Composable
private fun PagerBlock(
    state: StatisticState,
    onPageChange: (Int) -> Unit,
) {
    if (state.pages.isEmpty()) {
        Spacer(Modifier.height(64.dp))
        return
    }

    val pagerState = rememberPagerState(
        initialPage = state.selectedPageIndex.coerceIn(0, state.pages.lastIndex),
        pageCount = { state.pages.size }
    )

    // state → pager
    LaunchedEffect(state.selectedPageIndex, state.pages.size) {
        val target = state.selectedPageIndex.coerceIn(0, state.pages.lastIndex)
        if (pagerState.currentPage != target) pagerState.scrollToPage(target)
    }

    // pager → state
    LaunchedEffect(pagerState.settledPage) {
        if (pagerState.settledPage != state.selectedPageIndex) onPageChange(pagerState.settledPage)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        PagerDots(
            total = state.pages.size,
            current = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            beyondViewportPageCount = 1,
        ) { idx ->
            val page = state.pages[idx]
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = pageTitleString(page.title),
                    style = AppTypography.titleMdMedium,
                    color = AppColors.text.secondary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = durationString(page.subtitle),
                    style = AppTypography.headlineMdSemiBold,
                    color = AppColors.text.primary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}


@Preview
@Composable
private fun StatisticScreenPreview_Daily_WithData() {
    TikonchaParentTheme(ThemeMode.LIGHT) {
        StatisticUi(
            navigator = null,
            state = previewStateDaily(),
            event = {},
        )
    }
}

@Preview
@Composable
private fun StatisticScreenPreview_Weekly_WithData() {
    TikonchaParentTheme(ThemeMode.LIGHT) {
        StatisticUi(
            navigator = null,
            state = previewStateWeekly(),
            event = {},
        )
    }
}

@Preview
@Composable
private fun StatisticScreenPreview_Empty() {
    TikonchaParentTheme(ThemeMode.LIGHT) {
        StatisticUi(
            navigator = null,
            state = previewStateEmpty(),
            event = {},
        )
    }
}

@Preview
@Composable
private fun StatisticScreenPreview_Dark() {
    TikonchaParentTheme(ThemeMode.DARK) {
        StatisticUi(
            navigator = null,
            state = previewStateDaily(),
            event = {},
        )
    }
}

@Preview
@Composable
private fun StatisticScreenPreview_Blurred() {
    TikonchaParentTheme(ThemeMode.LIGHT) {
        StatisticUi(
            navigator = null,
            state = previewStateDaily().copy(showBlur = true),
            event = {},
        )
    }
}