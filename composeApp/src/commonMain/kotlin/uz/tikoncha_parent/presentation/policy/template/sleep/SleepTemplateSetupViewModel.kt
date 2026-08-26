package uz.tikoncha_parent.presentation.policy.template.sleep

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.mapper.toTimeRuleDtoList
import uz.tikoncha_parent.data.remote.model.CreatePolicyRequest
import uz.tikoncha_parent.data.remote.model.UpdatePolicyRequest
import uz.tikoncha_parent.domain.model.PolicyResourceType
import uz.tikoncha_parent.domain.model.PolicyType
import uz.tikoncha_parent.domain.model.WeekDay
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.model.policy.PolicyAction
import uz.tikoncha_parent.domain.model.policy.PolicyTemplate
import uz.tikoncha_parent.domain.repository.PolicyRepository
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedState
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleUi
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class SleepTemplateSetupViewModel(
    private val policyRepository: PolicyRepository
) : ScreenModel {

    private val _state = MutableStateFlow(SleepTemplateSetupState())
    val state = _state.asStateFlow()

    private val _effect = Channel<SleepTemplateSetupEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: SleepTemplateSetupEvent) {
        when (event) {
            is SleepTemplateSetupEvent.Init -> initState(event)
            is SleepTemplateSetupEvent.SetStartTime ->
                _state.update { it.copy(startTime = event.time) }
            is SleepTemplateSetupEvent.SetEndTime ->
                _state.update { it.copy(endTime = event.time) }
            is SleepTemplateSetupEvent.SetTimeRange ->
                _state.update { it.copy(startTime = event.start, endTime = event.end) }
            is SleepTemplateSetupEvent.Save -> save(event.sharedState)
            is SleepTemplateSetupEvent.Delete -> delete(event.ruleId)
            SleepTemplateSetupEvent.ResetResponseState ->
                _state.update {
                    it.copy(
                        createState = ResponseState.Idle,
                        updateState = ResponseState.Idle,
                        deleteState = ResponseState.Idle,
                    )
                }
        }
    }

    private fun initState(event: SleepTemplateSetupEvent.Init) {
        if (_state.value.isInitialized) return

        val editing = event.sharedState.selectedPolicy

        if (editing != null) {
            val firstTimeRule = editing.timeRule.firstOrNull()
            _state.update {
                it.copy(
                    startTime = firstTimeRule?.startTime ?: it.startTime,
                    endTime = firstTimeRule?.endTime ?: it.endTime,
                    editingPolicy = editing,
                    isInitialized = true,
                )
            }
        } else {
            _state.update { it.copy(isInitialized = true) }
        }
    }

    private fun save(shared: PolicySharedState) {
        val s = _state.value

        if (s.intervalMinutes <= 0) {
            _effect.trySend(SleepTemplateSetupEffect.NoTimeIntervalSelectionToast)
            return
        }
        if (shared.selectedPkgs.isEmpty() &&
            shared.selectedCategories.isEmpty() &&
            shared.selectedSites.isEmpty()
        ) {
            _effect.trySend(SleepTemplateSetupEffect.NoAppWebSelectedToast)
            return
        }

        val timeRule = TimeRuleUi(
            id = 1,
            startTime = s.startTime,
            endTime = s.endTime,
            reverse = false,
            allDay = false,
            weekDays = WeekDay.entries.toSet(),
        )

        if (s.isEditMode) {
            requestUpdate(s, shared, timeRule)
        } else {
            requestCreate(s, shared, timeRule)
        }
    }

    private fun requestCreate(
        s: SleepTemplateSetupState,
        shared: PolicySharedState,
        timeRule: TimeRuleUi,
    ) {
        screenModelScope.launch {
            _state.update { it.copy(createState = ResponseState.Loading) }

            val request = CreatePolicyRequest(
                policy_name = "",
                scope_id = shared.selectedChild?.userId,
                rule_name = "",
                scope_type = PolicyType.PARENT_CHILD.name,
                policy_is_active = true,
                resource_type = PolicyResourceType.APP.name,
                action = PolicyAction.ALLOW.name,
                priority = 100,
                packages = shared.selectedPkgs.toList(),
                categories = shared.selectedCategories.toList(),
                features = shared.selectedFeatures.toList(),
                sites = shared.selectedSites.toList(),
                time_rule = listOf(timeRule).toTimeRuleDtoList(),
                limit_rule = null,
                location_rule = null,
                wifi = null,
                policy_template = PolicyTemplate.SLEEP.name,
            )

            when (val r = policyRepository.createPolicy(request)) {
                is Outcome.Failure -> _state.update {
                    it.copy(createState = ResponseState.Error(failure = r))
                }
                is Outcome.Success -> _state.update {
                    it.copy(createState = ResponseState.Success())
                }
            }
        }
    }

    private fun requestUpdate(
        s: SleepTemplateSetupState,
        shared: PolicySharedState,
        timeRule: TimeRuleUi,
    ) {
        val ruleId = s.editingPolicy?.ruleId.orEmpty()
        screenModelScope.launch {
            _state.update { it.copy(updateState = ResponseState.Loading) }

            val request = UpdatePolicyRequest(
                name = "",
                resource_type = PolicyResourceType.APP.name,
                action = PolicyAction.ALLOW.name,
                priority = 100,
                packages = shared.selectedPkgs.toList(),
                categories = shared.selectedCategories.toList(),
                sites = shared.selectedSites.toList(),
                features = shared.selectedFeatures.toList(),
                time_rule = listOf(timeRule).toTimeRuleDtoList(),
                limit_rule = null,
                location_rule = null,
                wifi = null,
                policy_template = PolicyTemplate.SLEEP.name,
            )

            when (val r = policyRepository.updatePolicyInServer(request, ruleId)) {
                is Outcome.Failure -> _state.update {
                    it.copy(updateState = ResponseState.Error(failure = r))
                }
                is Outcome.Success -> _state.update {
                    it.copy(updateState = ResponseState.Success())
                }
            }
        }
    }

    private fun delete(ruleId: String) {
        screenModelScope.launch {
            _state.update { it.copy(deleteState = ResponseState.Loading) }

            when (val r = policyRepository.deletePolicyInServer(ruleId)) {
                is Outcome.Failure -> _state.update {
                    it.copy(deleteState = ResponseState.Error(failure = r))
                }
                is Outcome.Success -> _state.update {
                    it.copy(deleteState = ResponseState.Success())
                }
            }
        }
    }
}