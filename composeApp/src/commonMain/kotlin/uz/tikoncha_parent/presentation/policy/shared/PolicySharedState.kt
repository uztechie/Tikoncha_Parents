package uz.tikoncha_parent.presentation.policy.shared

import uz.tikoncha_parent.domain.model.LocationRule
import uz.tikoncha_parent.domain.model.Policy
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

    // ── Draft snapshot ───────────────────────
    val initialDraftSnapshot: PolicyDraftSnapshot? = null,
    val canUpdateInitialDraftSnapshot: Boolean = true,
) {
    val isEditable: Boolean
        get() = selectedPolicy == null || selectedPolicy.policyType == PolicyType.PARENT_CHILD

    val isEditMode: Boolean
        get() = selectedPolicy != null

    val selectedAppCount: Int get() = selectedPkgs.size
    val selectedCategoryCount: Int get() = selectedCategories.size
    val selectedSiteCount: Int get() = selectedSites.size

    val showAddRuleButton: Boolean get() = (limitList.isEmpty() || timeList.isEmpty() || locationRule == null) && canUpdate


    val canSave: Boolean
        get() {
            val hasRule = timeList.isNotEmpty() || limitList.isNotEmpty() || locationRule != null
            val hasResource =
                selectedPkgs.isNotEmpty() || selectedCategories.isNotEmpty() || selectedSites.isNotEmpty()
            return hasRule && hasResource && policyTitle.isNotBlank()
        }

    /** App tanlangan — individual YOKI kategoriyasi tanlangan */
    fun isAppSelected(pkg: String, category: String?): Boolean {
        if (selectedPkgs.any { it.equals(pkg, ignoreCase = true) }) return true
        if (category != null && selectedCategories.any {
                it.equals(
                    category,
                    ignoreCase = true
                )
            }) return true
        return false
    }

    fun isSiteSelected(url: String): Boolean =
        selectedSites.any { it.equals(url, ignoreCase = true) }

    /** Kategoriya checkbox holati */
    fun categoryState(categoryName: String, appPackages: List<String>): CategorySelectionState {
        // Kategoriya to'liq tanlangan
        if (selectedCategories.any { it.equals(categoryName, ignoreCase = true) }) {
            return CategorySelectionState.ALL
        }
        // Individual applar tekshiruv
        val selectedCount = appPackages.count { pkg ->
            selectedPkgs.any { it.equals(pkg, ignoreCase = true) }
        }
        return when {
            selectedCount == 0 -> CategorySelectionState.NONE
            selectedCount == appPackages.size -> CategorySelectionState.ALL
            else -> CategorySelectionState.PARTIAL
        }
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
}


enum class CategorySelectionState { NONE, PARTIAL, ALL }
