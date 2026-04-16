package uz.tikoncha_parent.presentation.policy.app_site_selection

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.mapper.toAppSelectionUi
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.model.apps.AppCategory
import uz.tikoncha_parent.domain.use_case.policy.GetChildAppsUseCase
import uz.tikoncha_parent.presentation.profile.language.LanguagePrefs
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class AppWebViewModel(
    private val getChildAppsUseCase: GetChildAppsUseCase,
) : ScreenModel {

    private val _state = MutableStateFlow(AppWebState())
    val state = _state.asStateFlow()

    fun onEvent(event: AppWebEvent) {
        when (event) {
            is AppWebEvent.LoadApps -> loadChildApps(event.userId)

            is AppWebEvent.OnTabSelected ->
                _state.update { it.copy(tabIndex = event.tabIndex, searchQuery = "") }

            is AppWebEvent.OnSearchQueryChanged ->
                _state.update { it.copy(searchQuery = event.query) }

            is AppWebEvent.SetServerSites -> mergeServerSites(event.sites)

            is AppWebEvent.SetSiteInput ->
                _state.update { it.copy(siteInput = event.url, siteInputError = null) }

            AppWebEvent.ShowAddSiteDialog ->
                _state.update { it.copy(showAddSiteDialog = true, siteInput = "", siteInputError = null) }

            AppWebEvent.DismissAddSiteDialog ->
                _state.update { it.copy(showAddSiteDialog = false, siteInput = "", siteInputError = null) }

            AppWebEvent.ConfirmAddSite -> {
                if (_state.value.editingSite != null) confirmEditSite()
                else confirmAddSite()
            }

            is AppWebEvent.ShowSiteEditSheet ->
                _state.update {
                    it.copy(showSiteEditSheet = true, editingSite = event.site, siteInput = event.site.url)
                }

            is AppWebEvent.EditSite ->
                _state.update {
                    it.copy(
                        showSiteEditSheet = false,
                        editingSite = event.site,
                        siteInput = event.site.url,
                        showAddSiteDialog = true,
                        siteInputError = null,
                    )
                }

            AppWebEvent.DismissSiteEditSheet ->
                _state.update {
                    it.copy(showAddSiteDialog = false, siteInput = "", siteInputError = null, editingSite = null)
                }

            is AppWebEvent.RemoveSite ->
                _state.update { s ->
                    s.copy(
                        sites = s.sites.filter { it.url != event.url },
                        showSiteEditSheet = false,
                        editingSite = null,
                    )
                }
        }
    }

    // ── Ilova yuklash ────────────────────────

    private fun loadChildApps(userId: String) {
        screenModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(loadAppsResponseState = ResponseState.Loading) }

            when (val result = getChildAppsUseCase.invoke(userId)) {
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            loadAppsResponseState = ResponseState.Error(
                                message = result.message,
                                res = result.resId
                            )
                        )
                    }
                }
                is Resource.Success -> {
                    val allApps = result.data.map { it.toAppSelectionUi() }
                    val language = LanguagePrefs.loadOrDefault()

                    val grouped = allApps.groupBy { AppCategory.from(it.category) }
                    val hasCategoryData = grouped.keys.any { it != AppCategory.OTHER }

                    val uiGroups = grouped.map { (category, apps) ->
                        val localized = CategoryLocalizer.localize(category, language)
                        CategoryGroupUi(
                            id = category.id,
                            displayName = localized.name,
                            emoji = localized.emoji,
                            apps = apps.sortedBy { it.name.lowercase() },
                        )
                    }.sortedBy { it.id }

                    _state.update {
                        it.copy(
                            loadAppsResponseState = ResponseState.Success(),
                            apps = allApps,
                            categoryGroups = uiGroups,
                            hasCategoryData = hasCategoryData,
                        )
                    }
                }
            }
        }
    }

    // ── Site helpers ─────────────────────────

    private fun mergeServerSites(serverSites: List<String>) {
        val defaultUrls = DEFAULT_SITES.map { it.url }.toSet()
        val customs = serverSites
            .filter { it !in defaultUrls }
            .map { SiteUi(url = it, isDefault = false) }
        _state.update { it.copy(sites = DEFAULT_SITES + customs) }
    }

    private fun confirmAddSite() {
        val url = normalizeUrl(_state.value.siteInput.trim())
        if (!isValidUrl(url)) {
            _state.update { it.copy(siteInputError = SiteError.INVALID_URL) }
            return
        }
        if (_state.value.sites.any { it.url.equals(url, ignoreCase = true) }) {
            _state.update { it.copy(siteInputError = SiteError.ALREADY_EXISTS) }
            return
        }
        _state.update { s ->
            s.copy(
                sites = s.sites + SiteUi(url = url, isDefault = false),
                showAddSiteDialog = false,
                siteInput = "",
                siteInputError = null,
            )
        }
    }

    private fun confirmEditSite() {
        val old = _state.value.editingSite ?: return
        val newUrl = normalizeUrl(_state.value.siteInput.trim())
        if (!isValidUrl(newUrl)) {
            _state.update { it.copy(siteInputError = SiteError.INVALID_URL) }
            return
        }
        if (newUrl != old.url && _state.value.sites.any { it.url.equals(newUrl, ignoreCase = true) }) {
            _state.update { it.copy(siteInputError = SiteError.ALREADY_EXISTS) }
            return
        }
        _state.update { s ->
            s.copy(
                sites = s.sites.map { if (it.url == old.url) it.copy(url = newUrl) else it },
                showSiteEditSheet = false,
                showAddSiteDialog = false,
                editingSite = null,
                siteInput = "",
            )
        }
    }

    private fun normalizeUrl(url: String): String =
        url.removePrefix("https://").removePrefix("http://").removeSuffix("/")

    private fun isValidUrl(url: String): Boolean {
        if (url.isBlank()) return false
        return Regex("^([\\w-]+\\.)+[\\w-]{2,}(/.*)?$").matches(url)
    }
}