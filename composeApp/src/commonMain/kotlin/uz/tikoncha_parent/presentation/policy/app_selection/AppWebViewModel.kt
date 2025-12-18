package uz.tikoncha_parent.presentation.policy.app_selection

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.mapper.toAppSelectionUi
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.model.SubscriptionLimit
import uz.tikoncha_parent.domain.use_case.policy.GetChildAppsUseCase
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class AppWebViewModel(
    private val getChildAppsUseCase: GetChildAppsUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(AppWebState())
    val state = _state.asStateFlow()


    private var alreadySetOnce: Boolean = false
    private var alreadyLoadedOnce: Boolean = false

    fun onEvent(event: AppWebEvent) {
        when (event) {
            AppWebEvent.ClearData -> {
                Logger.d("AppWebViewModel", "Clear Data")

                alreadySetOnce = false
                alreadyLoadedOnce = false
                _state.update {
                    it.copy(
                        serverPkgs = emptySet(),
                        serverRequestedCount = 0,
                        serverPresentInstalledCount = 0,
                        serverMissingCount = 0,
                        apps = emptyList(),
                        selectedPkgs = emptySet(),
                        showLimitReachedDialog = false
                    )
                }
            }
            AppWebEvent.ClearAppList -> {
                Logger.d("AppWebViewModel", "ClearAppList")
                _state.update {
                    it.copy(
                        apps = emptyList(),
                    )
                }
            }
            AppWebEvent.DismissLimitDialog -> {
                _state.update { it.copy(showLimitReachedDialog = false) }
            }
            is AppWebEvent.OnAppWebSelected -> {
                _state.update { it.copy(appWebSelectionIndex = event.index) }
            }
            is AppWebEvent.SetServerPackages -> {
                if (alreadySetOnce){
                    return
                }

                val newServerPkgs = event.packages.toSet()
                val current = _state.value

                val updatedApps = if (current.apps.isNotEmpty()){
                    current.apps.map {app->
                        val shouldBeChecked = app.checked || newServerPkgs.contains(app.packageName)
                        app.copy(checked = shouldBeChecked)
                    }
                }else{
                    current.apps
                }

                val (requested, present, missing) = calculateServerStats(updatedApps, newServerPkgs)
                val newSelectedPkgs: Set<String> = buildSet {
                    addAll(current.selectedPkgs)
                    addAll(updatedApps.filter {it.checked}.map { it.packageName })
                }

                _state.update {
                    it.copy(
                        serverPkgs = newServerPkgs,
                        serverRequestedCount = requested,
                        serverPresentInstalledCount = present,
                        serverMissingCount = missing,
                        apps = updatedApps,
                        selectedPkgs = newSelectedPkgs
                    )
                }

                alreadySetOnce = true

            }

            is AppWebEvent.SetSelectedApps -> {
                val pkgs = event.apps
                    .filter { it.checked }
                    .map { it.packageName }
                    .toSet()

                _state.update { state ->
                    val updatedApps = if (state.apps.isNotEmpty()){
                        state.apps.map { app->
                            if (pkgs.contains(app.packageName)){
                                app.copy(
                                    checked = true
                                )
                            }
                            else{
                                app
                            }
                        }
                    }
                    else{
                        state.apps
                    }
                    state.copy(
                        selectedPkgs = pkgs,
                        apps = updatedApps
                    )
                }
            }

            is AppWebEvent.ToggleApp -> {
                val toggledApp = event.app
                val checked = event.checked

                _state.update {state ->

                    val appLimitCount = state.subscriptionLimit.appCount
                    val selectedAppCount = state.selectedPkgs.size

                    if (selectedAppCount >= appLimitCount && checked) {
                        return@update state.copy(
                            showLimitReachedDialog = true
                        )
                    }


                    val newApps = state.apps.map {app->
                        if (app.packageName == toggledApp.packageName){
                            app.copy(
                                checked = checked
                            )
                        }
                        else{
                            app
                        }
                    }

                    val newSelectedPkgs = if (checked){
                        state.selectedPkgs + toggledApp.packageName
                    }
                    else{
                        state.selectedPkgs - toggledApp.packageName
                    }
                    state.copy(
                        apps = newApps,
                        selectedPkgs = newSelectedPkgs
                    )

                }
            }

            AppWebEvent.GetAppsFromServer -> {
                if (_state.value.apps.isEmpty()){
                    if (alreadyLoadedOnce){
                        return
                    }
                    getChildApps()
                }
            }

            is AppWebEvent.SetChildId -> {
                _state.update {
                    it.copy(
                        childId = event.id
                    )
                }
            }

            is AppWebEvent.SetSubscriptionLimit -> {
                _state.update {
                    it.copy(
                        subscriptionLimit = event.limit
                    )
                }
            }

        }
    }


    private fun calculateServerStats(
        apps: List<AppSelectionUi>,
        serverPkgs: Set<String>
    ): Triple<Int, Int, Int>{
        val requested = serverPkgs.size

        val present = if (serverPkgs.isEmpty()) 0
        else apps.count{ serverPkgs.contains(it.packageName) }

        val missing = (requested - present).coerceAtLeast(0)
        return Triple(requested, present, missing)
    }


    private var appJob: Job? = null
    private fun getChildApps(){
        appJob?.cancel()
        appJob = screenModelScope.launch {
            Logger.d("AppWebViewModel", "getChildApps")
            _state.update {
                it.copy(
                    appsResponseState = ResponseState.Loading
                )
            }

            val result = getChildAppsUseCase.invoke(_state.value.childId)
            when(result){
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            appsResponseState = ResponseState.Error(message = result.message)
                        )
                    }
                }
                is Resource.Success -> {

                    val prevState = _state.value
                    val serverPkgs = prevState.serverPkgs
                    val previousSelected = prevState.selectedPkgs

                    val apps = result.data
                        .sortedBy { item -> item.order }
                        .map { item ->
                            val ui = item.toAppSelectionUi()
                            val pkg = ui.packageName
                            val shouldBeChecked = previousSelected.contains(pkg) || serverPkgs.contains(pkg)
                            ui.copy(checked = shouldBeChecked)
                        }

                    val (requested, present, missing) = calculateServerStats(apps, serverPkgs)

                    val newSelectedPkgs: Set<String> = buildSet {
                        addAll(previousSelected)
                        addAll(apps.filter { it.checked }.map { it.packageName })
                    }


                    _state.update {
                        it.copy(
                            appsResponseState = ResponseState.Success(),
                            apps = apps,
                            selectedPkgs = newSelectedPkgs,
                            serverRequestedCount = requested,
                            serverPresentInstalledCount = present,
                            serverMissingCount = missing
                        )
                    }
                    alreadyLoadedOnce = true
                }
            }
        }
    }







}