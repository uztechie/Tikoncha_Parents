package uz.tikoncha_parent.presentation.policy.template.sleep

sealed interface SleepTemplateSetupEffect {
    data object NoTitleToast : SleepTemplateSetupEffect
    data object NoDaySelectionToast : SleepTemplateSetupEffect
    data object NoTimeIntervalSelectionToast : SleepTemplateSetupEffect
    data object NoAppWebSelectedToast : SleepTemplateSetupEffect
}