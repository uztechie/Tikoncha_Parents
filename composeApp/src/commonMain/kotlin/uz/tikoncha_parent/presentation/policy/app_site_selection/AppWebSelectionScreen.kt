package uz.tikoncha_parent.presentation.policy.app_site_selection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.bu_ilova_kategoriya_orqali_tanlangan
import tikoncha_parents.composeapp.generated.resources.bu_sayt_allaqachon_ro_yxatda
import tikoncha_parents.composeapp.generated.resources.dialog_failed
import tikoncha_parents.composeapp.generated.resources.ilova_limiti_plus_tavsif
import tikoncha_parents.composeapp.generated.resources.ilova_qidirish
import tikoncha_parents.composeapp.generated.resources.ilovalar
import tikoncha_parents.composeapp.generated.resources.jadval
import tikoncha_parents.composeapp.generated.resources.kategoriya_bo_yicha_jadval_yaratish_uchun_plus_obunasini_faollashtiring
import tikoncha_parents.composeapp.generated.resources.kategoriyalar
import tikoncha_parents.composeapp.generated.resources.limit_tugadi
import tikoncha_parents.composeapp.generated.resources.ok
import tikoncha_parents.composeapp.generated.resources.plus_obnuna_kerak
import tikoncha_parents.composeapp.generated.resources.saqlash
import tikoncha_parents.composeapp.generated.resources.sayt_limiti_plus_tavsif
import tikoncha_parents.composeapp.generated.resources.sayt_qidirish
import tikoncha_parents.composeapp.generated.resources.saytlar
import tikoncha_parents.composeapp.generated.resources.search_normal
import tikoncha_parents.composeapp.generated.resources.xatolik
import uz.tikoncha_parent.presentation.base.CustomButtonNew
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.PillSegmentedButton
import uz.tikoncha_parent.presentation.base.PillSegmentedItem
import uz.tikoncha_parent.presentation.base.SubscriptionBottomDialog
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedEvent
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedModel
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedState
import uz.tikoncha_parent.presentation.profile.subscription.subscription_payment.SubscriptionPaymentScreen
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.NormalIconButtonSize
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.rememberScreenSystemBars

class AppWebSelectionScreen(
    val appSiteTabIndex: Int,
    val singleTabMode: Boolean = false,
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current ?: return

        val viewModel = navigator.koinNavigatorScreenModel<AppWebViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val sharedViewModel = koinViewModel<PolicySharedModel>()
        val sharedState by sharedViewModel.state.collectAsStateWithLifecycle()
        val sharedEvent = sharedViewModel::onEvent

        // ════════════════════════════════════════════════════════════
        // Screen ochilganda: applarni yuklash + sortlash uchun
        // hozirgi selections'ni viewModel'ga uzatish (bir marta)
        // ════════════════════════════════════════════════════════════
        LaunchedEffect(Unit) {
            event(
                AppWebEvent.LoadApps(
                    userId = sharedState.selectedChild?.userId ?: "",
                    selectedPkgs = sharedState.selectedPkgs,
                    selectedCategories = sharedState.selectedCategories,
                )
            )
            event(AppWebEvent.SetServerSites(sharedState.selectedSites.toList()))
            event(AppWebEvent.OnTabSelected(appSiteTabIndex))
        }

        AppWebSelectionUi(
            navigator = navigator,
            appState = state,
            appEvent = event,
            sharedState = sharedState,
            sharedEvent = sharedEvent,
            singleTabMode = singleTabMode,
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
    singleTabMode: Boolean,
) {
    var isSearchMode by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    var expandedCategories by remember { mutableStateOf(emptySet<String>()) }
    var expandedApps by remember { mutableStateOf(emptySet<String>()) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var toastJob by remember { mutableStateOf<Job?>(null) }

    LaunchedEffect(isSearchMode) {
        if (isSearchMode) focusRequester.requestFocus()
    }

    val appsLoading = appState.loadAppsResponseState is ResponseState.Loading
    val appsError = appState.loadAppsResponseState.errorText()
    var showErrorAppsDialog by remember { mutableStateOf(false) }
    LaunchedEffect(appsError) {
        showErrorAppsDialog = appsError.isNotEmpty()
    }

    // ─── Apps filter (search) — sort viewModel'da bo'lib bo'lgan ──────
    val filteredApps = remember(appState.apps, appState.searchQuery) {
        if (appState.searchQuery.isBlank()) appState.apps
        else appState.apps.filter { AppFeatures.matchesSearch(it, appState.searchQuery) }
    }

    // ─── Search vaqtida feature topilsa parent appni auto-expand ──────
    val effectiveExpandedApps = remember(filteredApps, appState.searchQuery, expandedApps) {
        if (appState.searchQuery.isNotBlank()) {
            filteredApps
                .filter { AppFeatures.visibleFeaturesFor(it, appState.searchQuery).isNotEmpty() }
                .map { it.packageName }
                .toSet()
        } else {
            expandedApps
        }
    }

    // ─── Categories: search vaqtida hammasini ochish ──────────────────
    val effectiveExpandedCategories = remember(appState.categoryGroups, appState.searchQuery, expandedCategories) {
        if (appState.searchQuery.isNotBlank()) {
            appState.categoryGroups.map { it.id }.toSet()
        } else {
            expandedCategories
        }
    }

    // ─── Categories filter (search) ───────────────────────────────────
    val visibleCategoryGroups = remember(appState.categoryGroups, appState.searchQuery) {
        if (appState.searchQuery.isBlank()) appState.categoryGroups
        else appState.categoryGroups.mapNotNull { group ->
            val filtered = group.apps.filter {
                it.name.contains(appState.searchQuery, ignoreCase = true)
            }
            if (filtered.isEmpty()) null else group.copy(apps = filtered)
        }
    }

    // ─── Sites: bir marta sort (selected first + default first) ───────
    val sortedSites = remember(appState.sites) {
        if (appState.sites.isEmpty()) return@remember emptyList()
        val snap = sharedState.selectedSites
        appState.sites.sortedWith(
            compareByDescending<SiteUi> { it.url in snap }
                .thenByDescending { it.isDefault }
        )
    }

    val filteredSites = remember(sortedSites, appState.searchQuery) {
        if (appState.searchQuery.isBlank()) sortedSites
        else sortedSites.filter { it.url.contains(appState.searchQuery, ignoreCase = true) }
    }
    val title = stringResource(Res.string.bu_ilova_kategoriya_orqali_tanlangan)

    fun showCoveredToast() {
        if (toastJob?.isActive == true) return
        toastJob = scope.launch {
            snackbarHostState.showSnackbar(
                message = title,
                duration = SnackbarDuration.Short,
            )
        }
    }

    // ─────────── Dialoglar ───────────

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
        onDismiss = { sharedEvent(PolicySharedEvent.DismissAppLimitDialog) },
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
        onDismiss = { sharedEvent(PolicySharedEvent.DismissSiteLimitDialog) },
    )

    SubscriptionBottomDialog(
        show = sharedState.showCategoryLimitDialog,
        title = stringResource(Res.string.plus_obnuna_kerak),
        message = stringResource(Res.string.kategoriya_bo_yicha_jadval_yaratish_uchun_plus_obunasini_faollashtiring),
        onConfirm = {
            sharedEvent(PolicySharedEvent.DismissCategoryLimitDialog)
            navigator?.push(SubscriptionPaymentScreen())
        },
        onDismiss = { sharedEvent(PolicySharedEvent.DismissCategoryLimitDialog) },
    )

    SubscriptionBottomDialog(
        show = sharedState.showFeatureLimitDialog,
        title = stringResource(Res.string.plus_obnuna_kerak),
        message = stringResource(Res.string.kategoriya_bo_yicha_jadval_yaratish_uchun_plus_obunasini_faollashtiring),
        onConfirm = {
            sharedEvent(PolicySharedEvent.DismissFeatureLimitDialog)
            navigator?.push(SubscriptionPaymentScreen())
        },
        onDismiss = { sharedEvent(PolicySharedEvent.DismissFeatureLimitDialog) },
    )

    val errorText = when (appState.siteInputError) {
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
        onEdit = { appState.editingSite?.let { appEvent(AppWebEvent.EditSite(it)) } },
        onDelete = {
            appState.editingSite?.let { site ->
                appEvent(AppWebEvent.RemoveSite(site.url))
                sharedEvent(
                    PolicySharedEvent.SetSelectedSites(
                        sharedState.selectedSites - site.url
                    )
                )
            }
        },
        onDismiss = { appEvent(AppWebEvent.DismissSiteEditSheet) },
    )

    val systemBars = rememberScreenSystemBars(
        statusBarColor = AppColors.bg.secondary,
        navigationBarColor = AppColors.bg.elevated,
    )

    // ─────────── UI ───────────
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(systemBars.modifier)
                .background(AppColors.bg.secondary),
        ) {
            // ── Header ──
            if (isSearchMode) {
                SearchHeader(
                    query = appState.searchQuery,
                    onQueryChange = { appEvent(AppWebEvent.OnSearchQueryChanged(it)) },
                    focusRequester = focusRequester,
                    placeholder = when (appState.tabIndex) {
                        2 -> stringResource(Res.string.sayt_qidirish)
                        else -> stringResource(Res.string.ilova_qidirish)
                    },
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

            // ── 3 ta tab ──
            if (!singleTabMode) {
                PillSegmentedButton(
                    items = listOf(
                        PillSegmentedItem(stringResource(Res.string.ilovalar)),
                        PillSegmentedItem(stringResource(Res.string.kategoriyalar)),
                        PillSegmentedItem(stringResource(Res.string.saytlar)),
                    ),
                    selectedIndex = appState.tabIndex,
                    onSelected = { appEvent(AppWebEvent.OnTabSelected(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 8.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    when (appState.tabIndex) {

                        // ════════ TAB 0: ILOVALAR ════════
                        0 -> {
                            if (appsLoading) {
                                items(count = 12) {
                                    ShimmerAppRowItem(
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                    )
                                }
                            } else {
                                items(
                                    items = filteredApps,
                                    key = { it.packageName },
                                ) { app ->
                                    val visibleFeatures =
                                        AppFeatures.visibleFeaturesFor(app, appState.searchQuery)
                                    val covered = sharedState.isAppCoveredByCategory(app.category)
                                    val isExpanded = effectiveExpandedApps.contains(app.packageName)

                                    AppRowWithFeatures(
                                        modifier = Modifier.fillMaxWidth(),
                                        app = app,
                                        features = visibleFeatures,
                                        isAppSelected = sharedState.isAppSelected(app.packageName, null),
                                        selectedFeatureKeys = sharedState.selectedFeatures,
                                        coveredByCategory = covered,
                                        enabled = sharedState.canUpdate,
                                        expanded = isExpanded,
                                        onToggleExpand = {
                                            expandedApps = if (expandedApps.contains(app.packageName))
                                                expandedApps - app.packageName
                                            else
                                                expandedApps + app.packageName
                                        },
                                        onAppToggle = {
                                            if (covered) {
                                                showCoveredToast()
                                            } else {
                                                sharedEvent(
                                                    PolicySharedEvent.ToggleApp(
                                                        packageName = app.packageName,
                                                        category = null,
                                                    )
                                                )
                                            }
                                        },
                                        onFeatureToggle = { feature ->
                                            if (covered) {
                                                showCoveredToast()
                                            } else {
                                                sharedEvent(PolicySharedEvent.ToggleFeature(feature.key))
                                            }
                                        },
                                    )
                                }
                            }
                        }

                        // ════════ TAB 1: KATEGORIYALAR ════════
                        1 -> {
                            if (appsLoading) {
                                items(count = 8) {
                                    ShimmerAppRowItem(
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                    )
                                }
                            } else {
                                visibleCategoryGroups.forEach { group ->
                                    item(key = "cat_${group.id}") {
                                        CategoryCardItem(
                                            group = group,
                                            isSelected = sharedState.isCategorySelected(group.id),
                                            expanded = effectiveExpandedCategories.contains(group.id),
                                            enabled = sharedState.canUpdate,
                                            onToggleExpand = {
                                                expandedCategories = if (expandedCategories.contains(group.id))
                                                    expandedCategories - group.id
                                                else
                                                    expandedCategories + group.id
                                            },
                                            onToggleSelect = {
                                                sharedEvent(
                                                    PolicySharedEvent.ToggleCategory(
                                                        categoryName = group.id,
                                                        appPackages = group.apps.map { it.packageName },
                                                    )
                                                )
                                            },
                                        )
                                    }
                                }
                            }
                        }

                        // ════════ TAB 2: SAYTLAR ════════
                        2 -> {
                            items(
                                items = filteredSites,
                                key = { it.url },
                            ) { site ->
                                SiteRowItem(
                                    modifier = Modifier.padding(horizontal = 16.dp),
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

                // ── FAB faqat Saytlar tabida ──
                if (appState.tabIndex == 2 && sharedState.canUpdate) {
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

            // ── Saqlash tugmasi ──
            if (sharedState.canUpdate) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            AppColors.bg.elevated,
                            RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                        )
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                ) {
                    CustomButtonNew(
                        enabled = sharedState.canSaveAppWebSelection,
                        text = stringResource(Res.string.saqlash),
                        onClick = { navigator?.pop() },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }

        // ── Snackbar (toast) pastda ──
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp),
        )
    }
}