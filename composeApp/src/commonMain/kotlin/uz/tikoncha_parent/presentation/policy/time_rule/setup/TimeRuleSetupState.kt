package uz.tikoncha_parent.presentation.policy.time_rule.setup

import kotlinx.datetime.LocalTime
import uz.tikoncha_parent.presentation.policy.common.WeekDayChipUi

data class TimeRuleSetupState(
    /** null bo'lsa create rejim, aks holda edit rejim */
    val editingId: Int? = null,

    val weekDays: List<WeekDayChipUi> = emptyList(),

    val startTime: LocalTime = DEFAULT_START_TIME,
    val endTime: LocalTime = DEFAULT_END_TIME,

    /** Kun davomida — belgilansa 1440 daqiqa (outside e'tiborga olinmaydi) */
    val allDay: Boolean = false,

    /** Tanlangan oraliqdan tashqarida cheklansin */
    val reverse: Boolean = false,

    /** Init chaqirilganmi — ikki marta ishlashning oldini olish uchun */
    val isInitialized: Boolean = false,
) {

    val isEditMode: Boolean get() = editingId != null

    val hasAnyDaySelected: Boolean get() = weekDays.any { it.selected }

    val canSave: Boolean get() = hasAnyDaySelected && (allDay || intervalMinutes > 0)

    /** start→end oralig'i minutlarda. Agar end < start bo'lsa, tungi o'tish sifatida hisoblanadi. */
    val intervalMinutes: Int
        get() {
            val s = startTime.hour * 60 + startTime.minute
            val e = endTime.hour * 60 + endTime.minute
            return if (e >= s) e - s else (MINUTES_PER_DAY - s) + e
        }

    /** Bir kundagi cheklov vaqti (daqiqa). */
    val perDayMinutes: Int
        get() = when {
            allDay -> MINUTES_PER_DAY
            reverse -> MINUTES_PER_DAY - intervalMinutes
            else -> intervalMinutes
        }

    /** Hafta davomida jami cheklov vaqti (daqiqa) — UI da "x soat, y daqiqa" ko'rsatiladi. */
    val totalWeekMinutes: Int
        get() = perDayMinutes * weekDays.count { it.selected }

    companion object {
        const val MINUTES_PER_DAY = 24 * 60
        val DEFAULT_START_TIME = LocalTime(8,0)
        val DEFAULT_END_TIME = LocalTime(15,0)
    }
}
