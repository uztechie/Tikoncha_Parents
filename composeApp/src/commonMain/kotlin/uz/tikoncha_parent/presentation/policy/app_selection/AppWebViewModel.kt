package uz.tikoncha_parent.presentation.policy.app_selection

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppWebViewModel : ViewModel() {

    private val _state = MutableStateFlow(AppWebState(isLoading = true))
    val state = _state.asStateFlow()

    init {
        _state.update {
            it.copy(
                categories = createAppList()
            )
        }
    }

    fun onEvent(event: AppWebEvent) {
        when (event) {
            is AppWebEvent.OnAppWebSelected -> {
                _state.update {
                    it.copy(
                        appWebSelectionIndex = event.genderIndex
                    )
                }
            }

            is AppWebEvent.ExpandCategory -> {
                expandCategory(event.category.id)
            }
            is AppWebEvent.ToggleApp -> {
                toggleApp(
                    appId = event.app.id,
                    checked = event.checked
                )
            }
            is AppWebEvent.ToggleCategory -> {
                toggleCategory(
                    categoryId = event.category.id,
                    checked = event.checked
                )
            }
        }
    }


    fun expandCategory(categoryId: String) {
        _state.update { state ->
            val newCats = state.categories.map { c ->
                if (c.id == categoryId) {
                    // agar expanded true bo‘lsa false qilamiz, bo‘lmasa true
                    c.copy(expanded = !c.expanded)
                } else c
            }
            state.copy(categories = newCats)
        }
    }

    fun collapseAll() {
        _state.update { state ->
            val newCats = state.categories.map { it.copy(expanded = false) }
            state.copy(categories = newCats)
        }
    }

    fun toggleCategory(categoryId: String, checked: Boolean) {
        _state.update { state ->
            val newCats = state.categories.map { c ->
                if (c.id == categoryId) {
                    val newApps = c.apps.map { it.copy(checked = checked) }
                    c.copy(checked = checked, apps = newApps)
                } else c
            }
            state.copy(categories = newCats)
        }
    }

    fun toggleApp(appId: String, checked: Boolean) {
        _state.update { state ->
            val newCats = state.categories.map { c ->
                if (c.apps.any { it.id == appId }) {
                    val newApps = c.apps.map { a ->
                        if (a.id == appId) a.copy(checked = checked) else a
                    }
                    val catChecked = newApps.all { it.checked }
                    c.copy(apps = newApps, checked = catChecked)
                } else c
            }
            state.copy(categories = newCats)
        }
    }

    // -------- App toggle (category id + app id bilan) --------
    fun toggleApp(categoryId: String, appId: String, checked: Boolean) {
        _state.update { state ->
            val newCats = state.categories.map { c ->
                if (c.id == categoryId) {
                    val newApps = c.apps.map { a ->
                        if (a.id == appId) a.copy(checked = checked) else a
                    }
                    val catChecked = newApps.all { it.checked }
                    c.copy(apps = newApps, checked = catChecked)
                } else c
            }
            state.copy(categories = newCats)
        }
    }


            private fun createAppList(): List<AppCategoryUi> {
                return listOf(
                    AppCategoryUi(
                        id = "social",
                        title = "Social",
                        expanded = false,
                        checked = false,
                        apps = listOf(
                            AppsUi(
                                id = "dsad",
                                title = "Instagram",
                                iconUrl = "https://vk.com/images/community_100.png",
                                checked = false
                            ),
                            AppsUi(
                                id = "asdasdasd",
                                title = "Telegram",
                                iconUrl = "https://vk.com/images/community_100.png",
                                checked = false
                            ),
                            AppsUi(
                                id = "asdas",
                                title = "WhatsApp",
                                iconUrl = "https://vk.com/images/community_100.png",
                                checked = false
                            )
                        )
                    ),
                    AppCategoryUi(
                        id = "productivity",
                        title = "Productivity",
                        expanded = false,
                        checked = false,
                        apps = listOf(
                            AppsUi(
                                id = "1sdsdaas",
                                title = "Clash",
                                iconUrl = "https://vk.com/images/community_100.png",
                                checked = false
                            ),
                            AppsUi(
                                id = "2asasdsdd",
                                title = "Imperius",
                                iconUrl = "https://vk.com/images/community_100.png",
                                checked = false
                            ),
                            AppsUi(
                                id = "3aaaaa",
                                title = "Legends",
                                iconUrl = "https://vk.com/images/community_100.png",
                                checked = false
                            )
                        )
                    )
                )
            }
        }