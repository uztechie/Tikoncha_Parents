package uz.tikoncha_parent.presentation.policy.common

import uz.tikoncha_parent.domain.model.WeekDay
import uz.tikoncha_parent.presentation.policy.limit_rule.LimitRuleUi
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleUi

// ═════════════════════════════════════════════════════════════
// Chips builder
// ═════════════════════════════════════════════════════════════

/**
 * Rule setup ekranlari uchun hafta kunlari chiplarini quradi.
 *
 * Qoida: bir tur rule ro'yxati (time YOKI limit) ichida har bir hafta kuni
 * faqat 1 marta ishlatilishi mumkin. Shu turdagi boshqa rulelardagi
 * tanlangan kunlar disabled bo'ladi.
 *
 * @param otherRuleDays shu turdagi BOSHQA rulelardagi tanlangan kunlar
 *                      (tahrirlanayotgan rule chetlab o'tilgan holda)
 * @param currentSelected shu rule hozir tanlagan kunlari
 */
fun buildWeekdayChips(
    otherRuleDays: Set<WeekDay>,
    currentSelected: Set<WeekDay> = emptySet(),
): List<WeekDayChipUi> = WeekDay.ordered.map { day ->
    WeekDayChipUi(
        day = day,
        selected = day in currentSelected,
        enabled = day !in otherRuleDays,
    )
}

// ═════════════════════════════════════════════════════════════
// Occupied days — bir turdagi rulelardan band kunlarni yig'adi
// ═════════════════════════════════════════════════════════════

/**
 * Time rulelardan band kunlarni yig'adi.
 * @param excludeId tahrirlanayotgan rule ID si (o'zini band deb hisoblamaslik uchun)
 */
fun occupiedTimeDays(
    rules: List<TimeRuleUi>,
    excludeId: Int? = null,
): Set<WeekDay> = rules.asSequence()
    .filter { excludeId == null || it.id != excludeId }
    .flatMap { it.weekDays.asSequence() }
    .toSet()

/**
 * Limit rulelardan band kunlarni yig'adi.
 * @param excludeId tahrirlanayotgan rule ID si (o'zini band deb hisoblamaslik uchun)
 */
fun occupiedLimitDays(
    rules: List<LimitRuleUi>,
    excludeId: Int? = null,
): Set<WeekDay> = rules.asSequence()
    .filter { excludeId == null || it.id != excludeId }
    .flatMap { it.weekDays.asSequence() }
    .toSet()

// ═════════════════════════════════════════════════════════════
// Chip list extensions — Setup VM lar ichida ishlatiladi
// ═════════════════════════════════════════════════════════════

/**
 * Faqat enabled bo'lgan kunni toggle qiladi.
 * Disabled chiplar (band kunlar) teginilmaydi.
 */
fun List<WeekDayChipUi>.toggleDay(day: WeekDay): List<WeekDayChipUi> =
    map { chip ->
        if (chip.day == day && chip.enabled) chip.copy(selected = !chip.selected)
        else chip
    }

/**
 * Chiplardan tanlangan kunlarni to'plam ko'rinishida qaytaradi.
 * Rule saqlashda ishlatiladi.
 */
fun List<WeekDayChipUi>.selectedDays(): Set<WeekDay> =
    asSequence()
        .filter { it.selected }
        .map { it.day }
        .toSet()