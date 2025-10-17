package uz.tikoncha_parent.presentation.home.schedule.time

import androidx.compose.ui.graphics.findFirstRoot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.LocalTime

class ActiveTimeController(initial: ActiveTimeState = ActiveTimeState()) {

    private val _state = MutableStateFlow(initial)
    val state = _state.asStateFlow()

    fun toggleDay(day: WeekDay) {
        val cur = state.value
        val days = cur.selectedDays.toMutableSet()
        if (day in days) days.remove(day) else days.add(day)
        _state.value = cur.copy(selectedDays = days)
    }

    fun setAllDay(enabled: Boolean){
        val cur = state.value
        _state.value = if (enabled){
            cur.copy(allDay = true, ranges = listOf(MinuteRange(0,24 * 60 -1)))
        } else {
            cur.copy(allDay = false, ranges = emptyList())
        }
    }

    fun addDefaultRange(){
        val cur = state.value
        val candidate = findFreeTwoHours(cur.ranges) ?: MinuteRange(12 * 60, 14 * 60)
        _state.value = cur.copy(ranges = RangeRules.add(cur.ranges, candidate))
    }

    fun deleteRange(index: Int){
        val cur = state.value
        _state.value = cur.copy(ranges = RangeRules.removeAt(cur.ranges, index))
    }

    fun updateRange(index: Int, newStart: Int, newEnd: Int){
        val cur = state.value
        val s = newStart.coerceIn(0, 24*60 - 2)
        val e = newEnd.coerceIn(s + 1, 24*60 - 1)

        val safe = RangeRules.safe(s, e)
        _state.value = cur.copy(ranges = RangeRules.replaceAt(cur.ranges, index, safe))
    }

    private fun findFreeTwoHours(ranges: List<MinuteRange>): MinuteRange? {
        val norm = RangeRules.normalize(ranges)
        var cursor = 0
        for (r in norm){
            if (r.start - cursor >= 120) return MinuteRange(cursor,cursor + 120)
            cursor = r.end
        }
        if (24 * 60 - cursor >= 120) return MinuteRange(cursor, cursor + 120)
        return null
    }
}