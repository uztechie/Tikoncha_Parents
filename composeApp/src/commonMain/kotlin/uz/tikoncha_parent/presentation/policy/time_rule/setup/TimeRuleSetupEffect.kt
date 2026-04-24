package uz.tikoncha_parent.presentation.policy.time_rule.setup

import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleUi

sealed interface TimeRuleSetupEffect {
    /** Save muvaffaqiyatli, shared ga yozib, ekranni yopish kerak. */
    data class Saved(val rule: TimeRuleUi) : TimeRuleSetupEffect
    data object NoDaySelectionToast : TimeRuleSetupEffect
    data object NoTimeIntervalSelectionToast : TimeRuleSetupEffect
}