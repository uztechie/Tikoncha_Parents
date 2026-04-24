package uz.tikoncha_parent.presentation.policy.app_site_selection
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.bu_sayt_allaqachon_ro_yxatda
import tikoncha_parents.composeapp.generated.resources.dialog_failed
import tikoncha_parents.composeapp.generated.resources.ilova_limiti_plus_tavsif
import tikoncha_parents.composeapp.generated.resources.ilova_qidirish
import tikoncha_parents.composeapp.generated.resources.ilovalar
import tikoncha_parents.composeapp.generated.resources.jadval
import tikoncha_parents.composeapp.generated.resources.kategoriya_bo_yicha_jadval_yaratish_uchun_plus_obunasini_faollashtiring
import tikoncha_parents.composeapp.generated.resources.limit_tugadi
import tikoncha_parents.composeapp.generated.resources.noto_g_ri_url_format
import tikoncha_parents.composeapp.generated.resources.ok
import tikoncha_parents.composeapp.generated.resources.saqlash
import tikoncha_parents.composeapp.generated.resources.sayt_limiti_plus_tavsif
import tikoncha_parents.composeapp.generated.resources.sayt_qidirish
import tikoncha_parents.composeapp.generated.resources.saytlar
import tikoncha_parents.composeapp.generated.resources.search_normal
import tikoncha_parents.composeapp.generated.resources.xatolik
import uz.tikoncha_parent.presentation.base.CustomButtonNew
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.SubscriptionBottomDialog
import uz.tikoncha_parent.presentation.policy.common.SegmentedTabBar
import uz.tikoncha_parent.presentation.policy.common.SegmentedTabBarDefaults
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedEvent
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedModel
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedState
import uz.tikoncha_parent.presentation.profile.subscription.subscription_payment.SubscriptionPaymentScreen
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.NormalIconButtonSize
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.rememberScreenSystemBars


class AppWebSelectionScreen(val appSiteTabIndex: Int): Screen{
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current ?: return

        val viewModel = navigator.koinNavigatorScreenModel<AppWebViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val sharedViewModel = koinViewModel<PolicySharedModel>()
        val sharedState by sharedViewModel.state.collectAsStateWithLifecycle()
        val sharedEvent = sharedViewModel::onEvent

        LaunchedEffect(Unit){
            event(AppWebEvent.LoadApps(sharedState.selectedChild?.userId?:""))
            event(AppWebEvent.OnTabSelected(appSiteTabIndex))
        }


        AppWebSelectionUi(
            navigator = navigator,
            appState = state,
            appEvent = event,
            sharedState = sharedState,
            sharedEvent = sharedEvent
        )

    }

}

@Composable
fun AppWebSelectionUi(
    navigator: Navigator? = null,
    appState: AppWebState,
    appEvent: (AppWebEvent) -> Unit,
    sharedState: PolicySharedState,
    sharedEvent: (PolicySharedEvent) -> Unit,
) {
    var isSearchMode by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    var expandedCategories by remember { mutableStateOf(emptySet<String>()) }

    LaunchedEffect(isSearchMode) {
        if (isSearchMode) focusRequester.requestFocus()
    }

    val appsLoading = appState.loadAppsResponseState is ResponseState.Loading
    val appsError = appState.loadAppsResponseState.errorText()
    var showErrorAppsDialog by remember { mutableStateOf(false) }
    LaunchedEffect(appsError){
        showErrorAppsDialog = appsError.isNotEmpty()
    }

    // ── Search da kategoriyalar ochiq, search yo'qsa user boshqaradi ─
    val effectiveExpanded = if (appState.searchQuery.isNotBlank()) {
        appState.categoryGroups.map { it.id }.toSet()
    } else {
        expandedCategories
    }

    // ── Flat list uchun sort (kategoriya yo'q holatda) ─
    val sortedApps = remember(appState.apps) {
        val selected = sharedState.selectedPkgs
        appState.apps.sortedWith(
            compareByDescending<AppSelectionUi> { it.packageName in selected }
                .thenByDescending { it.usageMinutes }
                .thenBy { it.name }
        )
    }

    val filteredApps = remember(sortedApps, appState.searchQuery) {
        if (appState.searchQuery.isBlank()) sortedApps
        else sortedApps.filter { it.name.contains(appState.searchQuery, ignoreCase = true) }
    }

    // ── Sites sort ───────────────────────────
    val sortedSites = remember(appState.sites) {
        val selected = sharedState.selectedSites
        appState.sites.sortedWith(
            compareByDescending<SiteUi> { it.url in selected }
                .thenByDescending { it.isDefault }
                .thenBy { it.url }
        )
    }

    val filteredSites = remember(sortedSites, appState.searchQuery) {
        if (appState.searchQuery.isBlank()) sortedSites
        else sortedSites.filter { it.url.contains(appState.searchQuery, ignoreCase = true) }
    }

    // ── Kategoriyali ro'yxatda search filter ─
    val visibleGroups = remember(appState.categoryGroups, appState.searchQuery) {
        if (appState.searchQuery.isBlank()) appState.categoryGroups
        else appState.categoryGroups.filter { group ->
            group.apps.any { it.name.contains(appState.searchQuery, ignoreCase = true) }
        }
    }

    CustomDialog(
        painter = painterResource(Res.drawable.dialog_failed),
        show = showErrorAppsDialog,
        title = stringResource(Res.string.xatolik),
        message = appsError,
        buttonText = stringResource(Res.string.ok),
        showCloseButton = false,
        onDismiss = { showErrorAppsDialog = false },
        onButtonClick = { showErrorAppsDialog = false },
    )


    SubscriptionBottomDialog(
        show = sharedState.showAppLimitDialog,
        title = stringResource(Res.string.limit_tugadi),
        message = stringResource(
            Res.string.ilova_limiti_plus_tavsif,
            sharedState.subscriptionLimitEntity?.appCount ?: 0,
        ),
        onConfirm = {
            sharedEvent(PolicySharedEvent.DismissAppLimitDialog)
            navigator?.push(SubscriptionPaymentScreen())
        },
        onDismiss = {
            sharedEvent(PolicySharedEvent.DismissAppLimitDialog)
        }
    )
    SubscriptionBottomDialog(
        show = sharedState.showSiteLimitDialog,
        title = stringResource(Res.string.limit_tugadi),
        message = stringResource(
            Res.string.sayt_limiti_plus_tavsif,
            sharedState.subscriptionLimitEntity?.appCount ?: 0,
        ),
        onConfirm = {
            sharedEvent(PolicySharedEvent.DismissSiteLimitDialog)
            navigator?.push(SubscriptionPaymentScreen())
        },
        onDismiss = {
            sharedEvent(PolicySharedEvent.DismissSiteLimitDialog)
        }
    )

    SubscriptionBottomDialog(
        show = sharedState.showCategoryLimitDialog,
        title = stringResource(Res.string.limit_tugadi),
        message = stringResource(Res.string.kategoriya_bo_yicha_jadval_yaratish_uchun_plus_obunasini_faollashtiring),
        onConfirm = {
            sharedEvent(PolicySharedEvent.DismissCategoryLimitDialog)
            navigator?.push(SubscriptionPaymentScreen())
        },
        onDismiss = {
            sharedEvent(PolicySharedEvent.DismissCategoryLimitDialog)
        }
    )
    val errorText = when (appState.siteInputError) {
        SiteError.INVALID_URL -> stringResource(Res.string.noto_g_ri_url_format)
        SiteError.ALREADY_EXISTS -> stringResource(Res.string.bu_sayt_allaqachon_ro_yxatda)
        null -> null
    }

    AddSiteDialog(
        show = appState.showAddSiteDialog,
        inputUrl = appState.siteInput,
        inputError = errorText,
        onInputChange = { appEvent(AppWebEvent.SetSiteInput(it)) },
        onConfirm = {
            val oldSite = appState.editingSite
            val newUrl = appState.siteInput.trim()
                .removePrefix("https://").removePrefix("http://").removeSuffix("/")

            appEvent(AppWebEvent.ConfirmAddSite)

            // Edit rejimda — selectedSites dagi eski URL ni yangi ga almashtirish
            if (oldSite != null && !oldSite.url.equals(newUrl, ignoreCase = true)) {
                val updated = sharedState.selectedSites.map {
                    if (it.equals(oldSite.url, ignoreCase = true)) newUrl else it
                }.toSet()
                sharedEvent(PolicySharedEvent.SetSelectedSites(updated))
            }
        },
        onDismiss = { appEvent(AppWebEvent.DismissAddSiteDialog) },
    )

    SiteEditBottomSheet(
        show = appState.showSiteEditSheet,
        site = appState.editingSite,
        onEdit = {
            appState.editingSite?.let { site ->
                appEvent(AppWebEvent.EditSite(site))
            }
        },
        onDelete = {
            appState.editingSite?.let { site ->
                appEvent(AppWebEvent.RemoveSite(site.url))
                sharedEvent(PolicySharedEvent.SetSelectedSites(
                    sharedState.selectedSites - site.url
                ))
            }
        },
        onDismiss = { appEvent(AppWebEvent.DismissSiteEditSheet) },
    )

    val systemBars = rememberScreenSystemBars(
        statusBarColor = AppColors.bg.secondary,
        navigationBarColor = AppColors.bg.secondary
    )

    // ── UI ───────────────────────────────────
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(systemBars.modifier)
            .background(AppColors.bg.secondary),
    ) {
        // Header
        if (isSearchMode) {
            SearchHeader(
                query = appState.searchQuery,
                onQueryChange = { appEvent(AppWebEvent.OnSearchQueryChanged(it)) },
                focusRequester = focusRequester,
                placeholder = if (appState.tabIndex == 0)
                    stringResource(Res.string.ilova_qidirish)
                else stringResource(Res.string.sayt_qidirish),
                onClose = {
                    isSearchMode = false
                    appEvent(AppWebEvent.OnSearchQueryChanged(""))
                },
            )
        } else {
            CustomHeader(
                showBackButton = true,
                onBackClick = { navigator?.pop() },
                title = stringResource(Res.string.jadval),
                trailingIcon = {
                    IconButton(
                        modifier = Modifier.size(NormalIconButtonSize),
                        onClick = { isSearchMode = true },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = AppColors.icon.secondary,
                        ),
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.search_normal),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                },
            )
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(),
        ) {
            // Tab
            SegmentedTabBar(
                items = listOf(
                    stringResource(Res.string.ilovalar),
                    stringResource(Res.string.saytlar),
                ),
                selectedIndex = appState.tabIndex,
                borderWidth = 2.dp,
                textStyle = AppTypography.bodyLgMedium,
                onSelect = { appEvent(AppWebEvent.OnTabSelected(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .padding(horizontal = 16.dp),
                colors = SegmentedTabBarDefaults.colors(
                    textColor = AppColors.text.primary,
                    selectedBorderColor = AppColors.border.accentEmphasis,
                    unselectedBorderColor = AppColors.border.disabled
                )
            )

            // Content
            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 16.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    when (appState.tabIndex) {
                        0 -> {
                            if (appsLoading) {
                                // ── Shimmer ──────────
                                items(count = 12) {
                                    ShimmerAppRowItem(
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp)
                                    )
                                }

                            } else if (!appState.hasCategoryData) {
                                // ── Kategoriya yo'q — flat list ──
                                items(
                                    items = filteredApps,
                                    key = { it.packageName },
                                ) { app ->
                                    AppRowItem(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(AppColors.bg.surface)
                                            .padding(horizontal = 16.dp),
                                        app = app,
                                        isSelected = sharedState.isAppSelected(app.packageName, null),
                                        enabled = sharedState.canUpdate,
                                        onToggle = {
                                            sharedEvent(PolicySharedEvent.ToggleApp(
                                                packageName = app.packageName,
                                                category = null,
                                            ))
                                        },
                                    )
                                }

                            } else {
                                // ── Kategoriyali ro'yxat ─────
                                visibleGroups.forEach { group ->
                                    item(key = "cat_${group.id}") {
                                        val groupApps = if (appState.searchQuery.isBlank()) {
                                            group
                                        } else {
                                            group.copy(
                                                apps = group.apps.filter {
                                                    it.name.contains(appState.searchQuery, ignoreCase = true)
                                                }
                                            )
                                        }

                                        AppCategoryHeaderItem(
                                            group = groupApps,
                                            sharedState = sharedState,
                                            expanded = effectiveExpanded.contains(group.id),
                                            enabled = sharedState.canUpdate,
                                            onToggleExpand = {
                                                expandedCategories = if (expandedCategories.contains(group.id))
                                                    expandedCategories - group.id
                                                else
                                                    expandedCategories + group.id
                                            },
                                            onToggleCategory = {
                                                // ← DOIM asl group.apps (search filtrsiz)
                                                sharedEvent(PolicySharedEvent.ToggleCategory(
                                                    categoryName = group.id,
                                                    appPackages = group.apps.map { it.packageName },
                                                ))
                                            },
                                            onToggleApp = { app ->
                                                // ← group.id ishlatish (app.category emas)
                                                sharedEvent(PolicySharedEvent.ToggleApp(
                                                    packageName = app.packageName,
                                                    category = group.id,
                                                    categoryAppPackages = group.apps.map { it.packageName },
                                                ))
                                            },
                                        )
                                    }
                                }
                            }
                        }

                        1 -> {
                            // ── Saytlar tab ──────────
                            items(
                                items = filteredSites,
                                key = { it.url },
                            ) { site ->
                                SiteRowItem(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp),
                                    site = site,
                                    isSelected = sharedState.isSiteSelected(site.url),
                                    enabled = sharedState.canUpdate,
                                    onToggle = {
                                        sharedEvent(PolicySharedEvent.ToggleSite(site.url))
                                    },
                                    onLongClick = {
                                        if (!site.isDefault && sharedState.canUpdate) {
                                            appEvent(AppWebEvent.ShowSiteEditSheet(site))
                                        }
                                    },
                                )
                            }
                        }
                    }
                }

                // FAB
                if (appState.tabIndex == 1 && sharedState.canUpdate) {
                    FloatingActionButton(
                        onClick = { appEvent(AppWebEvent.ShowAddSiteDialog) },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                            .size(48.dp),
                        shape = CircleShape,
                        containerColor = AppColors.bg.primary,
                        contentColor = AppColors.icon.inverse,
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    }
                }
            }

            // Saqlash
            if (sharedState.canUpdate) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppColors.bg.elevated, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ){
                    CustomButtonNew(
                        enabled = sharedState.canSaveAppWebSelection,
                        text = stringResource(Res.string.saqlash),
                        onClick = { navigator?.pop() },
                        modifier = Modifier
                            .fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        AppWebSelectionUi(
            appState = AppWebState(tabIndex = 0, loadAppsResponseState = ResponseState.Loading),
            appEvent = {},
            sharedState = PolicySharedState(),
            sharedEvent = {},
        )
    }
}