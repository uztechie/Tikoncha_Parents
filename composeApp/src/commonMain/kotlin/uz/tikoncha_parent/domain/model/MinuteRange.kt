package uz.tikoncha_parent.domain.model

data class MinuteRange(val start: Int, val end: Int) {
    init {
//        require(start in 0..(24 * 60) && end in 0..(24 * 60) && start < end)
    }
}

fun Int.hm(): String {
    val h = this / 60
    val m = this % 60
    val hh = if (h < 10) "0$h" else "$h"
    val mm = if (m < 10) "0$m" else "$m"
    return "$hh:$mm"
}
