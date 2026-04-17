package uz.tikoncha_parent.presentation.policy.limit_rule.setup

import uz.tikoncha_parent.domain.model.DayHour
import uz.tikoncha_parent.domain.model.HourMinute
import uz.tikoncha_parent.presentation.policy.common.WeekDayChipUi

data class LimitRuleSetupState(
    /** null bo'lsa create rejim, aks holda edit rejim */
    val editingId: Int? = null,

    val weekDays: List<WeekDayChipUi> = emptyList(),

    /** Limit davomiyligi. Daily uchun hour+minute ikkalasi, Hourly uchun faqat minute. */
    val duration: HourMinute = HourMinute(0, 0),

    val limitType: DayHour = DayHour.DAY,

    val isInitialized: Boolean = false,
) {

    val isEditMode: Boolean get() = editingId != null

    val hasAnyDaySelected: Boolean get() = weekDays.any { it.selected }

    val hasAnyDuration: Boolean
        get() = when (limitType) {
            DayHour.DAY -> duration.hour > 0 || duration.minute > 0
            DayHour.HOUR -> duration.minute > 0
        }

    val canSave: Boolean get() = hasAnyDaySelected && hasAnyDuration

    /** UI da pickerga berish uchun: Hourly tipda faqat minute ishlaydi */
    val effectiveHour: Int get() = if (limitType == DayHour.HOUR) 0 else duration.hour
    val effectiveMinute: Int get() = duration.minute
}