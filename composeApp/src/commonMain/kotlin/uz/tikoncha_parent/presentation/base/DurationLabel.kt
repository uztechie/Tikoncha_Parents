package uz.tikoncha_parent.presentation.base

data class DurationLabel(
    val hours: Int,
    val minutes: Int
) {
    val isEmpty: Boolean get() = hours == 0 && minutes == 0

    companion object {
        fun fromMillis(ms: Long): DurationLabel {
            if (ms <= 0L) return DurationLabel(0, 0)
            val totalMin = (ms / 60_000L).toInt()
            return DurationLabel(hours = totalMin / 60, minutes = totalMin % 60)
        }
    }
}