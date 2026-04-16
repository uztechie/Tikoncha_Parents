package uz.tikoncha_parent.presentation.policy.app_site_selection

import uz.tikoncha_parent.domain.model.SubscriptionLimit
import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class AppWebState(
    val loadAppsResponseState: ResponseState<Nothing> = ResponseState.Idle,

    // ── Ilovalar ─────────────────────────────
    val apps: List<AppSelectionUi> = emptyList(),
    val categoryGroups: List<CategoryGroupUi> = emptyList(),
    val hasCategoryData: Boolean = false,

    // ── Saytlar ──────────────────────────────
    val sites: List<SiteUi> = DEFAULT_SITES,
    val siteInput: String = "",
    val siteInputError: SiteError? = null,
    val showAddSiteDialog: Boolean = false,
    val showSiteEditSheet: Boolean = false,
    val editingSite: SiteUi? = null,

    // ── Search ───────────────────────────────
    val searchQuery: String = "",

    // ── Tab ──────────────────────────────────
    val tabIndex: Int = 0,
) {
    val filteredApps: List<AppSelectionUi>
        get() = if (searchQuery.isBlank()) apps
        else apps.filter { it.name.contains(searchQuery, ignoreCase = true) }

    val filteredSites: List<SiteUi>
        get() = if (searchQuery.isBlank()) sites
        else sites.filter { it.url.contains(searchQuery, ignoreCase = true) }

    val filteredCategoryGroups: List<CategoryGroupUi>
        get() = if (searchQuery.isBlank()) categoryGroups
        else categoryGroups.map { group ->
            group.copy(apps = group.apps.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            })
        }.filter { it.apps.isNotEmpty() }
}
