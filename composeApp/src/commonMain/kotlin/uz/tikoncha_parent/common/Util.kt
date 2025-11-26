package uz.tikoncha_parent.common


import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.math.roundToInt
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
object Util {

    val currentMillis: Long = Clock.System.now().toEpochMilliseconds()

    fun formatLocalDate(millis: Long): String {
        val zone = TimeZone.currentSystemDefault()
        val inputDate: LocalDate = Instant.fromEpochMilliseconds(millis)
            .toLocalDateTime(zone).date

        val today = Clock.System.now().toLocalDateTime(zone).date
        val yesterday = today.minus(1, DateTimeUnit.DAY)

        return when (inputDate) {
            today -> "Bugun"
            yesterday -> "Kecha"
            else -> formatDateDdMmYyyy(inputDate)
        }
    }

    private fun formatDateDdMmYyyy(date: LocalDate): String {
        return buildString {
            append(date.day.toString().padStart(2, '0'))
            append(".")
            append(date.month.number.toString().padStart(2, '0'))
            append(".")
            append(date.year)
        }
    }

    fun getCurrentDate(): LocalDate {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

    fun getCurrentTime(): LocalTime {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time
    }

    fun getMonthName(date: LocalDate): String {
        val uzbekMonths = listOf(
            "Yanvar", "Fevral", "Mart", "Aprel", "May", "Iyun",
            "Iyul", "Avgust", "Sentyabr", "Oktyabr", "Noyabr", "Dekabr"
        )
        return uzbekMonths[date.month.number - 1]
    }


    fun format6DigitCode(raw: String): String {
        val digits = raw.filter { it.isDigit() }.take(6)
        return if (digits.length <= 3) digits else "${digits.take(3)}-${digits.drop(3)}"
    }

    fun reformatDateTime_dd_MM_hh_mm(input: String?): String {
        if (input.isNullOrBlank()) return ""

        val localDateTime = try {
            val instant = Instant.parse(input)
            instant.toLocalDateTime(TimeZone.currentSystemDefault())
        } catch (_: Exception) {
            LocalDateTime.parse(input)
        }
        

        // Formatter yaratamiz
        val formatter = LocalDateTime.Format {
            day(padding = Padding.ZERO) // dd
            char('/')
            monthNumber(padding = Padding.ZERO) // MM
            char(' ')
            hour(padding = Padding.ZERO) // HH (24 soat formatda)
            char(':')
            minute(padding = Padding.ZERO) // mm
        }

        return localDateTime.format(formatter) // Masalan: 29/08 19:32
    }

    fun computeTimeProgress(
        createdAt: Long,
        dueAt: Long,
        now: Long = Clock.System.now().toEpochMilliseconds()
    ): Int {
        if (dueAt <= createdAt) {
            // yaroqsiz muddat: due kelmaguncha 100, keyin 0
            return if (now < dueAt) 100 else 0
        }
        val total = dueAt - createdAt
        val left = (dueAt - now).coerceIn(0L, total) // qolgan vaqt
        val pct = (left.toDouble() / total.toDouble()) * 100.0
        return pct.roundToInt().coerceIn(0, 100)     // 100 → 0
    }

    fun formatTimeHHmm(
        millis: Long,
        tz: TimeZone = TimeZone.currentSystemDefault()
    ): String {
        val ldt = Instant.fromEpochMilliseconds(millis).toLocalDateTime(tz)
        val hh = if (ldt.hour < 10) "0${ldt.hour}" else "${ldt.hour}"
        val mm = if (ldt.minute < 10) "0${ldt.minute}" else "${ldt.minute}"
        return "$hh:$mm"
    }

    fun formatDateDdMmYyyy(
        millis: Long,
        delimiter: Char = '.',
        tz: TimeZone = TimeZone.currentSystemDefault()
    ): String {
        val ldt = Instant.fromEpochMilliseconds(millis).toLocalDateTime(tz)
        val d = ldt.date
        val dd = d.day.toString().padStart(2, '0')
        val mm = d.month.number.toString().padStart(2, '0')
        val yyyy = d.year.toString().padStart(4, '0')
        return "$dd$delimiter$mm$delimiter$yyyy"
    }

    fun millisToLocalDate(
        millis: Long,
        tz: TimeZone = TimeZone.currentSystemDefault()
    ): LocalDate {
        return Instant.fromEpochMilliseconds(millis).toLocalDateTime(tz).date
    }

    fun millisToLocalTime(
        millis: Long,
        tz: TimeZone = TimeZone.currentSystemDefault()
    ): LocalTime {
        return Instant.fromEpochMilliseconds(millis).toLocalDateTime(tz).time
    }

    fun toMillis(date: LocalDate?, time: LocalTime?): Long {
        if (date == null || time == null) return 0L
        val ldt = LocalDateTime(date, time)
        return ldt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    fun Int.toCurrency(): String{
        val isNegative = this < 0
        val digits = kotlin.math.abs(this).toString()

        val sb = StringBuilder()
        var count = 0

        for (i in digits.length - 1 downTo 0){
            sb.append(digits[i])
            count++
            if (count == 3 && i != 0){
                sb.append(",")
                count = 0
            }
        }
        val formatted = sb.reverse().toString()
        return if (isNegative) "-$formatted" else formatted
    }
}