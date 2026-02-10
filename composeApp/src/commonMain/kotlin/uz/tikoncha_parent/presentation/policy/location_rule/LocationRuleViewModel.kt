package uz.tikoncha_parent.presentation.policy.location_rule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.presentation.profile.language.LanguagePrefs

class LocationRuleViewModel(

) : ScreenModel {

    private val TAG = "LocationRuleViewModel"

    private val _state = MutableStateFlow(LocationRuleState())
    val state = _state.asStateFlow()

    fun onEvent(event: LocationRuleEvent) {
        when (event) {
            is LocationRuleEvent.SetLocation -> {
                screenModelScope.launch {

                    val locationRuleUi = LocationRuleUi(
                        language = LanguagePrefs.loadOrDefault().languageCode,
                        is_dark = event.isDark,
                        lat = event.locationData?.lat,
                        lng = event.locationData?.lng,
                        editable = event.editable,
                        policy_name = event.policyName,
                        location_rule = event.locationRule,
                        parent = true
                    )

                    _state.update {
                        it.copy(
                            ruleUi = locationRuleUi,
                            rule = event.locationRule,
                            ruleJson = Json.encodeToString<LocationRuleUi>(locationRuleUi)
                        )
                    }
                }

            }

            is LocationRuleEvent.SetJsonString -> {
                val json = event.json?:return
                val locationRuleUi = Json.decodeFromString<LocationRuleUi>(json)
                _state.update {
                    it.copy(
                        rule = locationRuleUi.location_rule
                    )
                }
                Logger.d(TAG, "onEvent: SetJsonString rule=${_state.value.rule}")
                Logger.d(TAG, "onEvent: SetJsonString locationRuleUi=$locationRuleUi")
            }
        }
    }


}