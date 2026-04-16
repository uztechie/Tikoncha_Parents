package uz.tikoncha_parent.presentation.policy.app_site_selection

import uz.tikoncha_parent.domain.model.SubscriptionLimit


sealed interface AppWebEvent {

    data class LoadApps(val userId: String): AppWebEvent

    data class OnTabSelected(val tabIndex: Int) :
       AppWebEvent

    // ── Search ───────────────────────────────
    data class OnSearchQueryChanged(val query: String) :
       AppWebEvent

    // ── Saytlar CRUD ─────────────────────────
    data class SetServerSites(val sites: List<String>) :
       AppWebEvent
    data class SetSiteInput(val url: String) :
       AppWebEvent
    data object ShowAddSiteDialog :
       AppWebEvent
    data object DismissAddSiteDialog :
       AppWebEvent
    data object ConfirmAddSite :
       AppWebEvent

    data class ShowSiteEditSheet(val site: SiteUi) :
       AppWebEvent
    data class EditSite(val site: SiteUi) :
       AppWebEvent
    data object DismissSiteEditSheet :
       AppWebEvent
    data class RemoveSite(val url: String) :
       AppWebEvent


}