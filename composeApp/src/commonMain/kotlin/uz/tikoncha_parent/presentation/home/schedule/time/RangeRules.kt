package uz.tikoncha_parent.presentation.home.schedule.time

import androidx.compose.runtime.mutableStateListOf
import kotlin.math.max

object RangeRules {

    fun safe(start: Int, end: Int): MinuteRange? {
        val s = start.coerceIn(0, 24 * 60)
        val e = end.coerceIn(0, 24 * 60)

        return if (s < e) MinuteRange(s, e) else null
    }

    fun normalize(ranges: List<MinuteRange>): List<MinuteRange> {
        if (ranges.isEmpty()) return emptyList()
        val sorted = ranges.sortedBy { it.start }
        val acc = mutableStateListOf<MinuteRange>()
        var cur = sorted.first()

        for (i in 1 until sorted.size) {
            val next = sorted[i]
            if (next.start <= cur.end) {
                cur = MinuteRange(cur.start, max(cur.end, next.end))
            } else {
                acc += cur
                cur = next
            }
        }
        acc += cur
        return acc
    }

    fun add(ranges: List<MinuteRange>, newRange: MinuteRange): List<MinuteRange> =
        normalize(ranges + newRange)

    fun removeAt(ranges: List<MinuteRange>, index: Int): List<MinuteRange> =
        ranges.toMutableList().also { if (index in it.indices) it.removeAt(index) }

    fun replaceAt(ranges: List<MinuteRange>, index: Int, newRange: MinuteRange?): List<MinuteRange> {
        val list = ranges.toMutableList()
        if (index !in list.indices) return ranges
        if (newRange == null){
            list.removeAt(index)
            return list
        }
        list[index] = newRange
        return normalize(list)
    }
}