package uz.tikoncha_parent.presentation.policy.policy_setup

import uz.tikoncha_parent.domain.model.LocationRule
import uz.tikoncha_parent.presentation.policy.limit_rule.LimitRuleUi
import uz.tikoncha_parent.presentation.policy.location_rule.LocationRuleUi
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleUi

data class PolicyDraftSnapshot(
    val title: String,
    val timeList: List<TimeRuleUi>,
    val limitList: List<LimitRuleUi>,
    val locationRule: LocationRule?,
    val packages: List<String>,
)