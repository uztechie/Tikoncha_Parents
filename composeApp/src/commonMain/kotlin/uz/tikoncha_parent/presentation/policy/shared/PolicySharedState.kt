package uz.tikoncha_parent.presentation.policy.shared

import uz.tikoncha_parent.domain.model.LocationRule
import uz.tikoncha_parent.domain.model.PolicyType
import uz.tikoncha_parent.domain.model.SubscriptionLimit
import uz.tikoncha_parent.domain.model.SubscriptionType
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.domain.model.policy.PolicyAction
import uz.tikoncha_parent.presentation.policy.policy_list.PolicyItemUi
import uz.tikoncha_parent.presentation.policy.limit_rule.LimitRuleUi
import uz.tikoncha_parent.presentation.policy.policy_setup.PolicyDraftSnapshot
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleUi

data class PolicySharedState(

    val selectedChild: UserInfo? = null,
    // ── Qoidalar ─────────────────────────────
    val timeList: List<TimeRuleUi> = emptyList(),
    val limitList: List<LimitRuleUi> = emptyList(),
    val locationRule: LocationRule? = null,

    // ── Tanlangan resurslar ──────────────────
    val selectedPkgs: Set<String> = emptySet(),
    val selectedCategories: Set<String> = emptySet(),
    val selectedSites: Set<String> = emptySet(),
    val selectedFeatures: Set<String> = emptySet(),               // YANGI

    // ── Tab ──────────────────────────────────
    val appWebTabIndex: Int = 0,

    // ── Policy asosiy ────────────────────────
    val policyTitle: String = "",
    val policyAction: PolicyAction = PolicyAction.DENY,
    val selectedPolicy: PolicyItemUi? = null,

    // ── Ruxsat va limitlar ───────────────────
    val canUpdate: Boolean = true,
    val subscriptionLimitEntity: SubscriptionLimit? = null,
    val showAppLimitDialog: Boolean = false,
    val showSiteLimitDialog: Boolean = false,
    val showCategoryLimitDialog: Boolean = false,
    val showFeatureLimitDialog: Boolean = false,                  // YANGI

    // ── Draft snapshot ───────────────────────
    val initialDraftSnapshot: PolicyDraftSnapshot? = null,
    val canUpdateInitialDraftSnapshot: Boolean = true,
) {
    val isEditable: Boolean
        get() = selectedPolicy == null || selectedPolicy.policyType == PolicyType.PARENT_CHILD

    val isEditMode: Boolean
        get() = selectedPolicy != null

    val selectedAppCount: Int
        get() = selectedPkgs.size + selectedFeatures.size         // O'ZGARDI: feature ham app countga kiradi
    val selectedCategoryCount: Int get() = selectedCategories.size
    val selectedSiteCount: Int get() = selectedSites.size
    val selectedFeatureCount: Int get() = selectedFeatures.size   // YANGI

    val showAddRuleButton: Boolean
        get() = (limitList.isEmpty() || timeList.isEmpty() || locationRule == null) && canUpdate

    val canSavePolicy: Boolean
        get() {
            val hasRule = timeList.isNotEmpty() || limitList.isNotEmpty() || locationRule != null
            val hasResource =
                selectedPkgs.isNotEmpty() ||
                        selectedCategories.isNotEmpty() ||
                        selectedSites.isNotEmpty() ||
                        selectedFeatures.isNotEmpty()
            return hasRule && hasResource && policyTitle.isNotBlank()
        }

    fun isAppSelected(pkg: String, category: String?): Boolean {
        if (selectedPkgs.any { it.equals(pkg, ignoreCase = true) }) return true
        if (category != null && selectedCategories.any {
                it.equals(category, ignoreCase = true)
            }) return true
        return false
    }

    fun isSiteSelected(url: String): Boolean =
        selectedSites.any { it.equals(url, ignoreCase = true) }

    fun isFeatureSelected(key: String): Boolean =                 // YANGI
        selectedFeatures.any { it.equals(key, ignoreCase = true) }

    /** Kategoriya checkbox holati — feature ham PARTIAL ga sabab bo'ladi */
    fun categoryState(
        categoryName: String,
        appPackages: List<String>,
        appFeatureKeys: List<String> = emptyList(),               // YANGI param
    ): CategorySelectionState {
        if (selectedCategories.any { it.equals(categoryName, ignoreCase = true) }) {
            return CategorySelectionState.ALL
        }
        val hasSelectedApp = appPackages.any { pkg ->
            selectedPkgs.any { it.equals(pkg, ignoreCase = true) }
        }
        val hasSelectedFeature = appFeatureKeys.any { key ->
            selectedFeatures.any { it.equals(key, ignoreCase = true) }
        }
        return if (!hasSelectedApp && !hasSelectedFeature) CategorySelectionState.NONE
        else CategorySelectionState.PARTIAL
    }

    fun toSnapshot(): PolicyDraftSnapshot = PolicyDraftSnapshot(
        title = policyTitle,
        action = policyAction,
        timeList = timeList,
        limitList = limitList,
        locationRule = locationRule,
        packages = selectedPkgs.toList(),
        categories = selectedCategories.toList(),
        sites = selectedSites.toList(),
        features = selectedFeatures.toList(),                     // YANGI — PolicyDraftSnapshot ga ham qo'shing
    )

    val hasChanges: Boolean
        get() {
            val initial = initialDraftSnapshot ?: return false
            return initial != toSnapshot()
        }

    val canAddApp: Boolean
        get() = subscriptionLimitEntity?.appCount?.let { selectedAppCount < it } ?: true

    val canAddSite: Boolean
        get() = subscriptionLimitEntity?.appCount?.let { selectedSiteCount < it } ?: true

    val canSelectCategory: Boolean
        get() = subscriptionLimitEntity?.subscriptionType != SubscriptionType.FREE

    val canSelectFeature: Boolean                                 // YANGI
        get() = subscriptionLimitEntity?.subscriptionType != SubscriptionType.FREE

    val canSaveAppWebSelection: Boolean
        get() = selectedPkgs.isNotEmpty() ||
                selectedSites.isNotEmpty() ||
                selectedCategories.isNotEmpty() ||
                selectedFeatures.isNotEmpty()
}

enum class CategorySelectionState { NONE, PARTIAL, ALL }