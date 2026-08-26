package uz.tikoncha_parent.presentation.policy.policy_setup

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.mapper.toLimitRuleDtoList
import uz.tikoncha_parent.data.mapper.toLocationRuleDto
import uz.tikoncha_parent.data.mapper.toTimeRuleDtoList
import uz.tikoncha_parent.data.remote.model.CreatePolicyRequest
import uz.tikoncha_parent.data.remote.model.UpdatePolicyRequest
import uz.tikoncha_parent.domain.model.PolicyResourceType
import uz.tikoncha_parent.domain.model.PolicyType
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.repository.PolicyRepository
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedState
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class PolicySetupViewModel(
    private val policyRepository: PolicyRepository
): ScreenModel {


    private val TAG = "PolicySetupViewModel"
    private val _state = MutableStateFlow(PolicySetupState())
    val state = _state.asStateFlow()

    private val _effect = Channel<PolicySetupEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()


    fun onEvent(event: PolicySetupEvent) {
        when (event) {
            is PolicySetupEvent.SavePolicy -> {

                val s = event.sharedState
                if (s.policyTitle.isEmpty()){
                    _effect.trySend(PolicySetupEffect.NoTitleToast)
                    return
                }
                if (s.timeList.isEmpty() && s.limitList.isEmpty() && s.locationRule == null){
                    _effect.trySend(PolicySetupEffect.NoRuleSelectedToast)
                    return
                }
                if (s.selectedCategories.isEmpty() &&
                    s.selectedPkgs.isEmpty() &&
                    s.selectedSites.isEmpty() &&
                    s.selectedFeatures.isEmpty()) {                              // YANGI
                    _effect.trySend(PolicySetupEffect.NoAppWebSelectedToast)
                    return
                }

                if (event.sharedState.isEditMode) requestUpdate(event.sharedState)
                else requestCreate(event.sharedState)
            }
            is PolicySetupEvent.DeletePolicy -> requestDelete(event.ruleId)
            PolicySetupEvent.ResetResponseState -> {
                _state.update {
                    PolicySetupState()
                }
            }
        }
    }


    private fun requestCreate(shared: PolicySharedState) {
        screenModelScope.launch {
            _state.update { it.copy(createState = ResponseState.Loading) }
            val request = CreatePolicyRequest(
                policy_name = shared.policyTitle,
                scope_id = shared.selectedChild?.userId,
                rule_name = shared.policyTitle,
                scope_type = PolicyType.PARENT_CHILD.name,
                policy_is_active = true,
                resource_type = PolicyResourceType.APP.name,
                action = shared.policyAction.name,
                priority = 100,
                packages = shared.selectedPkgs.toList(),
                categories = shared.selectedCategories.toList(),
                sites = shared.selectedSites.toList(),
                features = shared.selectedFeatures.toList(),
                time_rule = shared.timeList.toTimeRuleDtoList(),
                limit_rule = shared.limitList.toLimitRuleDtoList(),
                location_rule = shared.locationRule?.toLocationRuleDto(),
                wifi = null,
            )
            when (val res = policyRepository.createPolicy(request)) {
                is Outcome.Failure -> _state.update { it.copy(createState = ResponseState.Error(failure = res)) }
                is Outcome.Success -> _state.update { it.copy(createState = ResponseState.Success()) }
            }
        }
    }


    private fun requestUpdate(shared: PolicySharedState) {
        screenModelScope.launch {
            _state.update { it.copy(updateState = ResponseState.Loading) }
            val ruleId = shared.selectedPolicy?.ruleId.orEmpty()
            val request = UpdatePolicyRequest(
                name = shared.policyTitle,
                resource_type = PolicyResourceType.APP.name,
                action = shared.policyAction.name,
                priority = 100,
                packages = shared.selectedPkgs.toList(),
                categories = shared.selectedCategories.toList(),
                sites = shared.selectedSites.toList(),
                features = shared.selectedFeatures.toList(),
                time_rule = shared.timeList.toTimeRuleDtoList(),
                limit_rule = shared.limitList.toLimitRuleDtoList(),
                location_rule = shared.locationRule?.toLocationRuleDto(),
                wifi = null,
            )

            when (val res = policyRepository.updatePolicyInServer(request, ruleId)) {
                is Outcome.Failure -> _state.update { it.copy(updateState = ResponseState.Error(failure = res)) }
                is Outcome.Success -> _state.update { it.copy(updateState = ResponseState.Success()) }
            }
        }
    }

    private fun requestDelete(ruleId: String) {
        screenModelScope.launch {
            _state.update { it.copy(deleteState = ResponseState.Loading) }

            when (val res = policyRepository.deletePolicyInServer(ruleId)) {
                is Outcome.Failure -> _state.update { it.copy(deleteState = ResponseState.Error(failure = res)) }
                is Outcome.Success -> _state.update { it.copy(deleteState = ResponseState.Success()) }
            }
        }
    }
}