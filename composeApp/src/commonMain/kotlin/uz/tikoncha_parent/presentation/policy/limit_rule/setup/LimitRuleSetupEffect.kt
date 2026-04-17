package uz.tikoncha_parent.presentation.policy.limit_rule.setup

import uz.tikoncha_parent.presentation.policy.limit_rule.LimitRuleUi

sealed interface LimitRuleSetupEffect {
    data class Saved(val rule: LimitRuleUi) : LimitRuleSetupEffect
}