package uz.tikoncha_parent.presentation.policy.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.mapper.toTimeRuleUiList
import uz.tikoncha_parent.domain.model.PolicyType
import uz.tikoncha_parent.domain.model.SubscriptionLimit
import uz.tikoncha_parent.domain.model.apps.AppCategory
import uz.tikoncha_parent.domain.use_case.payment.SubscriptionLimitUseCase
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.presentation.policy.app_site_selection.AppFeatures
import uz.tikoncha_parent.presentation.policy.limit_rule.LimitRuleUi
import uz.tikoncha_parent.presentation.policy.policy_setup.PolicyDraftSnapshot
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleUi
import kotlin.plus
import kotlin.text.category

class PolicySharedModel(
    private val subscriptionLimitUseCase: SubscriptionLimitUseCase
): ViewModel() {



    private val _state = MutableStateFlow(PolicySharedState())
    val state = _state.asStateFlow()


    fun onEvent(event: PolicySharedEvent) {
        when (event) {

            // ── Qoidalar ─────────────────────
            is PolicySharedEvent.SetTimeRule ->
                _state.update { it.copy(timeList = event.list) }

            is PolicySharedEvent.SetLimitRule ->
                _state.update { it.copy(limitList = event.list) }

            is PolicySharedEvent.SetLocationRule ->
                _state.update { it.copy(locationRule = event.locationRule) }

            // ── Ilovalar ─────────────────────
            is PolicySharedEvent.ToggleApp -> toggleApp(event.packageName, event.category, event.categoryAppPackages)

            is PolicySharedEvent.SetSelectedPkgs ->
                _state.update { it.copy(selectedPkgs = event.pkgs) }

            // ── Kategoriyalar ────────────────
            is PolicySharedEvent.ToggleCategory ->
                toggleCategory(event.categoryName, event.appPackages)

            is PolicySharedEvent.SetSelectedCategories ->
                _state.update { it.copy(selectedCategories = event.categories) }

            // ── Saytlar ──────────────────────
            is PolicySharedEvent.ToggleSite -> toggleSite(event.url)

            is PolicySharedEvent.SetSelectedSites ->
                _state.update { it.copy(selectedSites = event.sites) }

            is PolicySharedEvent.ToggleFeature -> toggleFeature(event.key)
            is PolicySharedEvent.SetSelectedFeatures ->
                _state.update { it.copy(selectedFeatures = event.features) }

            PolicySharedEvent.DismissFeatureLimitDialog ->
                _state.update { it.copy(showFeatureLimitDialog = false) }

            // ── Tab ──────────────────────────
            is PolicySharedEvent.SetAppWebTabIndex ->
                _state.update { it.copy(appWebTabIndex = event.index) }

            // ── Limit dialog ─────────────────
            PolicySharedEvent.DismissAppLimitDialog ->
                _state.update { it.copy(showAppLimitDialog = false) }
            PolicySharedEvent.DismissSiteLimitDialog ->
                _state.update { it.copy(showSiteLimitDialog = false) }
            PolicySharedEvent.DismissCategoryLimitDialog ->
                _state.update { it.copy(showCategoryLimitDialog = false) }

            // ── Policy info ──────────────────
            is PolicySharedEvent.SetPolicy -> {
                val policy = event.policyItemUi
                _state.update {
                    it.copy(
                        timeList = policy.timeRule,
                        limitList = policy.limitRule,
                        locationRule = policy.locationRule,
                        selectedPkgs = policy.packages.toSet(),
                        selectedCategories = policy.categories.toSet(),
                        selectedSites = policy.sites.toSet(),
                        selectedFeatures = policy.features.toSet(),         // YANGI — PolicyItemUi ga features field qo'shing
                        policyTitle = policy.policyName,
                        policyAction = policy.action,
                        selectedPolicy = policy,
                        canUpdate = policy.policyType == PolicyType.PARENT_CHILD,
                    )
                }
                _state.update { s -> s.copy(initialDraftSnapshot = s.toSnapshot()) }
            }

            is PolicySharedEvent.SetPolicyTitle ->
                _state.update { it.copy(policyTitle = event.title) }

            is PolicySharedEvent.SetPolicyAction ->
                _state.update { it.copy(policyAction = event.action) }

            is PolicySharedEvent.SetSubscriptionLimit ->
                _state.update { it.copy(subscriptionLimitEntity = event.limit) }

            // ── Snapshot ─────────────────────
            PolicySharedEvent.LockInitialSnapshot -> {
                _state.update { s ->
                    if (s.canUpdateInitialDraftSnapshot) {
                        s.copy(
                            initialDraftSnapshot = s.toSnapshot(),
                            canUpdateInitialDraftSnapshot = false,
                        )
                    } else s
                }
            }

            // ── Clear ────────────────────────
            PolicySharedEvent.ClearData ->
                _state.value = PolicySharedState()

            is PolicySharedEvent.SetSelectedChild -> {
                _state.update {
                    it.copy(
                        selectedChild = event.child
                    )
                }
            }

            is PolicySharedEvent.UpsertTimeRule -> upsertTimeRule(event.rule)
            is PolicySharedEvent.RemoveTimeRule -> removeTimeRule(event.id)

            is PolicySharedEvent.UpsertLimitRule -> upsertLimitRule(event.rule)
            is PolicySharedEvent.RemoveLimitRule -> removeLimitRule(event.id)
        }
    }

    // ═════════════════════════════════════════
    // Toggle helpers
    // ═════════════════════════════════════════

    private fun toggleApp(packageName: String, category: String?, categoryAppPackages: List<String>) {
        val s = _state.value

        val catKey = if (category != null) {
            s.selectedCategories.firstOrNull { it.equals(category, ignoreCase = true) }
        } else null

        if (catKey != null) {
            val otherApps = categoryAppPackages.filter {
                !it.equals(packageName, ignoreCase = true)
            }.toSet()
            _state.update { state ->
                state.copy(
                    selectedCategories = state.selectedCategories - catKey,
                    selectedPkgs = state.selectedPkgs + otherApps,
                )
            }
            return
        }

        val existing = s.selectedPkgs.firstOrNull { it.equals(packageName, ignoreCase = true) }
        if (existing != null) {
            // Olib tashlash — feature larga tegmaydi (mustaqil qoladi)
            _state.update { it.copy(selectedPkgs = it.selectedPkgs - existing) }
        } else {
            if (!s.canAddApp) {
                _state.update { it.copy(showAppLimitDialog = true) }
                return
            }

            // Auto-add features (faqat plus user va limitga sig'sa)
            val featuresToAutoAdd: List<String> = if (s.canSelectFeature) {
                val maxApps = s.subscriptionLimitEntity?.appCount ?: Int.MAX_VALUE
                val slotsLeft = (maxApps - (s.selectedAppCount + 1)).coerceAtLeast(0)
                AppFeatures.featuresFor(packageName)
                    .filter { feat ->
                        s.selectedFeatures.none { it.equals(feat.key, ignoreCase = true) }
                    }
                    .take(slotsLeft)
                    .map { it.key }
            } else emptyList()

            _state.update {
                it.copy(
                    selectedPkgs = it.selectedPkgs + packageName,
                    selectedFeatures = it.selectedFeatures + featuresToAutoAdd,
                )
            }
        }
    }

    private fun toggleCategory(categoryName: String, appPackages: List<String>) {
        val s = _state.value
        val isOther = categoryName == AppCategory.OTHER.id
        val featureKeys = appPackages.flatMap { pkg ->                // YANGI
            AppFeatures.featuresFor(pkg).map { it.key }
        }
        val catState = s.categoryState(categoryName, appPackages, featureKeys)

        when (catState) {
            CategorySelectionState.NONE,
            CategorySelectionState.PARTIAL -> {
                // Select qilmoqchi — obuna tekshiruvi
                if (!s.canSelectCategory) {
                    _state.update { it.copy(showCategoryLimitDialog = true) }
                    return
                }

                // app_count limit tekshiruvi
                if (!s.canAddApp) {
                    _state.update { it.copy(showAppLimitDialog = true) }
                    return
                }

                if (isOther) {
                    val appsToAdd = appPackages.filter { pkg ->
                        s.selectedPkgs.none { it.equals(pkg, ignoreCase = true) }
                    }.toSet()
                    _state.update { state ->
                        state.copy(selectedPkgs = state.selectedPkgs + appsToAdd)
                    }
                } else {
                    val appsToRemove = appPackages.flatMap { pkg ->
                        s.selectedPkgs.filter { it.equals(pkg, ignoreCase = true) }
                    }.toSet()
                    _state.update { state ->
                        state.copy(
                            selectedCategories = state.selectedCategories + categoryName,
                            selectedPkgs = state.selectedPkgs - appsToRemove,
                        )
                    }
                }
            }

            CategorySelectionState.ALL -> {
                if (isOther) {
                    val appsToRemove = appPackages.flatMap { pkg ->
                        s.selectedPkgs.filter { it.equals(pkg, ignoreCase = true) }
                    }.toSet()
                    _state.update { it.copy(selectedPkgs = it.selectedPkgs - appsToRemove) }
                } else {
                    val catKey = s.selectedCategories.firstOrNull {
                        it.equals(categoryName, ignoreCase = true)
                    }
                    if (catKey != null) {
                        _state.update { it.copy(selectedCategories = it.selectedCategories - catKey) }
                    } else {
                        val appsToRemove = appPackages.flatMap { pkg ->
                            s.selectedPkgs.filter { it.equals(pkg, ignoreCase = true) }
                        }.toSet()
                        _state.update { it.copy(selectedPkgs = it.selectedPkgs - appsToRemove) }
                    }
                }
            }
        }
    }

    private fun toggleSite(url: String) {
        val s = _state.value
        val normalize = url.lowercase().trim()
        val existing = s.selectedSites.firstOrNull { it.equals(normalize, ignoreCase = true) }

        if (existing != null) {
            _state.update { it.copy(selectedSites = it.selectedSites - existing) }
        } else {
            if (!s.canAddSite) {
                _state.update { it.copy(showSiteLimitDialog = true) }
                return
            }
            _state.update { it.copy(selectedSites = it.selectedSites + url) }
        }
    }

    private fun toggleFeature(key: String) {
        val s = _state.value
        val existing = s.selectedFeatures.firstOrNull { it.equals(key, ignoreCase = true) }

        if (existing != null) {
            // Olib tashlash — limit yo'q
            _state.update { it.copy(selectedFeatures = it.selectedFeatures - existing) }
        } else {
            // Plus obuna shart
            if (!s.canSelectFeature) {
                _state.update { it.copy(showFeatureLimitDialog = true) }
                return
            }
            // Feature ham app_count ga kiradi
            if (!s.canAddApp) {
                _state.update { it.copy(showAppLimitDialog = true) }
                return
            }
            _state.update { it.copy(selectedFeatures = it.selectedFeatures + key) }
        }
    }

    // ═════════════════════════════════════════
// Rule upsert/remove helpers
// ═════════════════════════════════════════

    private fun upsertTimeRule(rule: TimeRuleUi) {
        _state.update { s ->
            // Edit rejim: id > 0 va ro'yxatda mavjud
            if (rule.id != 0) {
                val idx = s.timeList.indexOfFirst { it.id == rule.id }
                if (idx >= 0) {
                    val newList = s.timeList.toMutableList().apply { this[idx] = rule }
                    return@update s.copy(timeList = newList)
                }
            }
            // Create rejim yoki id topilmadi: yangi id beriladi
            val newId = (s.timeList.maxOfOrNull { it.id } ?: 0) + 1
            s.copy(timeList = s.timeList + rule.copy(id = newId))
        }
    }

    private fun removeTimeRule(id: Int) {
        _state.update { s ->
            s.copy(timeList = s.timeList.filterNot { it.id == id })
        }
    }

    private fun upsertLimitRule(rule: LimitRuleUi) {
        _state.update { s ->
            if (rule.id != 0) {
                val idx = s.limitList.indexOfFirst { it.id == rule.id }
                if (idx >= 0) {
                    val newList = s.limitList.toMutableList().apply { this[idx] = rule }
                    return@update s.copy(limitList = newList)
                }
            }
            val newId = (s.limitList.maxOfOrNull { it.id } ?: 0) + 1
            s.copy(limitList = s.limitList + rule.copy(id = newId))
        }
    }

    private fun removeLimitRule(id: Int) {
        _state.update { s ->
            s.copy(limitList = s.limitList.filterNot { it.id == id })
        }
    }
}