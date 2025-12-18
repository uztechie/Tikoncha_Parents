package uz.tikoncha_parent.common

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import uz.tikoncha_parent.presentation.domain.model.LanguageType
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
object DateTimeUtil {

    private fun Int.two(): String = if (this < 10) "0$this" else toString()

    fun toMillisUtc(s: String?): Long {
        if (s.isNullOrBlank()) return 0L
        val input = s.trim()

        // Capture date, time, and optional fractional seconds (any length)
        val re = Regex(
            pattern = """^(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2}):(\d{2})(?:\.(\d{1,}))?""",
            option = RegexOption.IGNORE_CASE
        )

        val m = re.find(input) ?: return 0L

        val year   = m.groupValues[1].toInt()
        val month  = m.groupValues[2].toInt()
        val day    = m.groupValues[3].toInt()
        val hour   = m.groupValues[4].toInt()
        val minute = m.groupValues[5].toInt()
        val second = m.groupValues[6].toInt()

        val frac = m.groupValues.getOrNull(7).orEmpty()
        // milliseconds = first 3 digits (truncate) or pad with zeros if shorter
        val ms = when {
            frac.isEmpty()      -> 0
            frac.length >= 3    -> frac.substring(0, 3).toInt()
            else                -> (frac + "000").substring(0, 3).toInt()
        }

        val ldt = LocalDateTime(
            year, month, day,
            hour, minute, second,
            ms * 1_000_000 // nanos
        )

        // Interpret the naive timestamp as UTC (no offset in the string)
        return ldt.toInstant(TimeZone.UTC).toEpochMilliseconds()
    }

    private fun formatTimeHHmm(t: LocalTime): String {
        val h = t.hour
        val m = t.minute
        return "${h.two()}${":"}${m.two()   }"
    }

    fun formatTime(millis: Long): String {
        val t = Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.currentSystemDefault()).time
        return "${t.hour.two()}:${t.minute.two()}"
    }

    private fun formatDate_ddMMyyyy(d: LocalDate): String {
        val day = d.day
        val mon = d.month.ordinal
        val yr = d.year
        return "${day.two()}.${mon.two()}.$yr"
    }

    fun formatDateForChatUserStatus(
        localDate: LocalDate?,
        bugun: String,
        kecha: String,
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ): String {
        if (localDate == null) return ""

        val today = Clock.System.todayIn(timeZone)
        val yesterday = today.minus(1, DateTimeUnit.DAY)

        return when (localDate) {
            today -> bugun
            yesterday -> kecha
            else -> {
                val d = localDate.day.toString().padStart(2, '0')
                val m = localDate.month.ordinal.toString().padStart(2, '0')
                val y = localDate.year.toString()
                "$d.$m.$y"
            }
        }
    }

    fun formatDayMonthLocalized(
        localDate: LocalDate?,
        lang: LanguageType,     // UZ yoki RU
        bugun: String,
        kecha: String,
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ): String {
        if (localDate == null) return ""

        val today = Clock.System.todayIn(timeZone)
        val yesterday = today.minus(1, DateTimeUnit.DAY)

        return when (localDate) {
            today -> bugun
            yesterday -> kecha
            else -> formatDayMonthLocal(localDate, lang)
        }
    }

    fun formatDayMonthLocal(date: LocalDate, lang: LanguageType): String {
        val uzMonths = listOf(
            "Yanvar", "Fevral", "Mart", "Aprel", "May", "Iyun",
            "Iyul", "Avgust", "Sentyabr", "Oktyabr", "Noyabr", "Dekabr"
        )

        val ruMonths = listOf(
            "Января", "Февраля", "Марта", "Апреля", "Мая", "Июня",
            "Июля", "Августа", "Сентября", "Октября", "Ноября", "Декабря"
        )

        val months = when (lang) {
            LanguageType.RU -> ruMonths
            LanguageType.UZ -> uzMonths
        }

        val day = date.day
        val monthName = months[date.month.ordinal]

        return "$day $monthName"
    }

    fun formatDateTimeForChatUserStatus(
        millis: Long,
        bugun: String,
        kecha: String,
        zone: TimeZone = TimeZone.currentSystemDefault()
    ): String {
        if (millis == 0L) return ""

        val instant = Instant.fromEpochMilliseconds(millis)
        val localDT = instant.toLocalDateTime(zone)
        val date = localDT.date

        val nowDate = Clock.System.now().toLocalDateTime(zone).date
        val yesterday = nowDate.minus(DatePeriod(days = 1))

        return when (date) {
            nowDate -> "$bugun ${formatTimeHHmm(localDT.time)}"
            yesterday -> "$kecha ${formatTimeHHmm(localDT.time)}"
            else -> formatDate_ddMMyyyy(date)
        }
    }

    fun formatDateTimeForChat(
        longDate: Long,
        zone: TimeZone = TimeZone.currentSystemDefault()
    ): String {
        if (longDate == 0L) return ""
        val instant = Instant.fromEpochMilliseconds(longDate)

        val date = instant.toLocalDateTime(zone).date
        val today = Clock.System.now().toLocalDateTime(zone).date

        return if (date == today) formatTime(longDate) else formatDate_ddMMyyyy(date)
    }


    fun formatDateTimeMonthlyForMap(
        longDate: Long,
        lang: LanguageType,
        zone: TimeZone = TimeZone.currentSystemDefault()
    ): String {
        if (longDate == 0L) return ""

        val instant = Instant.fromEpochMilliseconds(longDate)

        val date = instant.toLocalDateTime(zone).date
        val today = Clock.System.now().toLocalDateTime(zone).date
        val time = formatTime(millis = longDate)

        val todayString = when(lang) {
            LanguageType.UZ -> {"Bugun"}
            LanguageType.RU -> {"Сегодня"}
        }



        return if (date == today){
            "$todayString $time"
        }
        else{
            val formattedDate = formatDayMonth(longDate, lang, zone)
            "$formattedDate $time"
        }
    }

    fun formatDateTimeMonthlyForChat(
        longDate: Long,
        lang: LanguageType,
        zone: TimeZone = TimeZone.currentSystemDefault()
    ): String {
        if (longDate == 0L) return ""

        val instant = Instant.fromEpochMilliseconds(longDate)

        val date = instant.toLocalDateTime(zone).date
        val today = Clock.System.now().toLocalDateTime(zone).date

        return if (date == today)
            formatTime(longDate)
        else
            formatDayMonth(longDate, lang, zone)
    }


    fun formatDayMonth(
        millis: Long,
        lang: LanguageType,
        zone: TimeZone = TimeZone.currentSystemDefault()
    ): String {

        val uzMonths = listOf(
            "Yanvar", "Fevral", "Mart", "Aprel", "May", "Iyun",
            "Iyul", "Avgust", "Sentyabr", "Oktyabr", "Noyabr", "Dekabr"
        )

        val ruMonths = listOf(
            "Января", "Февраля", "Марта", "Апреля", "Мая", "Июня",
            "Июля", "Августа", "Сентября", "Октября", "Ноября", "Декабря"
        )

        if (millis == 0L) return ""

        val date = Instant.fromEpochMilliseconds(millis)
            .toLocalDateTime(zone).date

        val months = when (lang) {
            LanguageType.RU -> ruMonths
            LanguageType.UZ -> uzMonths
        }

        val day = date.day
        val monthName = months[date.month.ordinal]

        return "$day-$monthName"
    }

    fun reformattedDayMonthForTask(
        date: LocalDate?,
        languageCode: LanguageType
    ): String {
        if (date == null) return ""

        val uzMonths = listOf(
            "Yanvar", "Fevral", "Mart", "Aprel", "May", "Iyun",
            "Iyul", "Avgust", "Sentyabr", "Oktyabr", "Noyabr", "Dekabr"
        )

        val ruMonths = listOf(
            "Января", "Февраля", "Марта", "Апреля", "Мая", "Июня",
            "Июля", "Августа", "Сентября", "Октября", "Ноября", "Декабря"
        )

        val day = date.day
        val monthIndex = date.month.ordinal

        val monthName = when (languageCode) {
            LanguageType.RU -> ruMonths.getOrNull(monthIndex)
            LanguageType.UZ -> uzMonths.getOrNull(monthIndex)
        } ?: ""

        return "$day-$monthName"
    }


    fun formatToIsoString(
        localDate: LocalDate?,
        localTime: LocalTime?,
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ): String {
        if (localTime == null || localDate == null){
            return ""
        }
        // Combine LocalDate + LocalTime into LocalDateTime
        val localDateTime = LocalDateTime(localDate, localTime)
        val instant = localDateTime.toInstant(TimeZone.currentSystemDefault())
        val utcLdt = instant.toLocalDateTime(timeZone)


        val year = utcLdt.year.toString().padStart(4, '0')
        val month = (utcLdt.month.ordinal+1).toString().padStart(2, '0')
        val day = utcLdt.day.toString().padStart(2, '0')
        val hour = utcLdt.hour.toString().padStart(2, '0')
        val minute = utcLdt.minute.toString().padStart(2, '0')
        val second = utcLdt.second.toString().padStart(2, '0')
        val millis = (utcLdt.nanosecond / 1_000_000).toString().padStart(3, '0')

        return "$year-$month-${day}T$hour:$minute:$second.${millis}"
    }

    fun formatToIsoString(
        millis: Long,
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ): String {
        println("formatToIsoString millis=$millis")
        val instant = Instant.fromEpochMilliseconds(millis)
        val localDateTime = instant.toLocalDateTime(timeZone)


        val year = localDateTime.year.toString().padStart(4, '0')
        val month = (localDateTime.month.ordinal+1).toString().padStart(2, '0')
        val day = localDateTime.day.toString().padStart(2, '0')
        val hour = localDateTime.hour.toString().padStart(2, '0')
        val minute = localDateTime.minute.toString().padStart(2, '0')
        val second = localDateTime.second.toString().padStart(2, '0')
        val millis = (localDateTime.nanosecond / 1_000_000).toString().padStart(3, '0')

        return "$year-$month-${day}T$hour:$minute:$second.${millis}"
    }



    fun getCurrentIsoDateTime(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
        val nowInstant = Clock.System.now()
        val localDateTime = nowInstant.toLocalDateTime(timeZone)

        val year = localDateTime.year.toString().padStart(4, '0')
        val month = (localDateTime.month.ordinal+1).toString().padStart(2, '0')
        val day = localDateTime.day.toString().padStart(2, '0')
        val hour = localDateTime.hour.toString().padStart(2, '0')
        val minute = localDateTime.minute.toString().padStart(2, '0')
        val second = localDateTime.second.toString().padStart(2, '0')
        val millis = (localDateTime.nanosecond / 1_000_000).toString().padStart(3, '0')

        return "$year-$month-${day}T$hour:$minute:$second.${millis}"
    }

    fun String.fromServerToLocalDateTime(): LocalDateTime =
        runCatching { Instant.parse(this).toLocalDateTime(TimeZone.currentSystemDefault()) }
            .getOrElse { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) }



    fun String?.serverDateTimeToMillis(defaultTimeZone: TimeZone = TimeZone.currentSystemDefault()): Long {
        if (this == null){
            return 0
        }
        val raw = this.trim()
        if (raw.isEmpty()) return 0L

        return try {
            // Offset bormi? (Z yoki +HH:MM / +HHMM)
            val hasZone = raw.endsWith("Z") ||
                    Regex("[+-]\\d{2}:?\\d{2}$").containsMatchIn(raw)

            // Fraksiyani 9 digit (nanosecond) ga normallashtiramiz, offsetni HHMM -> HH:MM ga
            val normalized = raw
                // ".123" yoki ".123000" -> ".123000000"
                .replace(Regex("""\.(\d{1,9})""")) { m ->
                    val frac = m.groupValues[1]
                    "." + (frac + "000000000").take(9)
                }
                // "+0500" -> "+05:00"
                .replace(Regex("""([+-]\d{2})(\d{2})$"""), "$1:$2")

            if (hasZone) {
                // Z yoki offset bor: bevosita Instant
                Instant.parse(normalized).toEpochMilliseconds()
            } else {
                // Zona yo‘q: LocalDateTime sifatida parse qilib, defaultTimeZone bilan Instantga o‘tkazamiz
                val re = Regex(
                    """^(\d{4})-(\d{2})-(\d{2})T""" +         // yyyy-MM-ddT
                            """(\d{2}):(\d{2})""" +                   // HH:mm
                            """(?::(\d{2})(?:\.(\d{1,9}))?)?$"""      // [:ss[.fffffffff]]
                )
                val m = re.matchEntire(normalized) ?: return 0L

                val year   = m.groupValues[1].toInt()
                val month  = m.groupValues[2].toInt()
                val day    = m.groupValues[3].toInt()
                val hour   = m.groupValues[4].toInt()
                val minute = m.groupValues[5].toInt()
                val second = m.groupValues.getOrNull(6)?.takeIf { it.isNotEmpty() }?.toInt() ?: 0
                val nano   = m.groupValues.getOrNull(7)?.takeIf { it.isNotEmpty() }?.let {
                    (it + "000000000").take(9).toInt()
                } ?: 0

                val ldt = LocalDateTime(year, month, day, hour, minute, second, nano)
                ldt.toInstant(defaultTimeZone).toEpochMilliseconds()
            }
        } catch (_: Throwable) {
            0L
        }
    }

    fun reformattedYearDay(reformatedDate: LocalDate?): String {
        if (reformatedDate == null) return ""

        val day = reformatedDate.day.toString().padStart(2, '0')
        val month = reformatedDate.month.number.toString().padStart(2, '0')
        val year = reformatedDate.year.toString()

        return "$day.$month.$year"
    }


    fun LocalDateTime.toUIData(): String {
        val d = date
        return "${d.day.toString().padStart(2,'0')}." +
                "${d.month.number.toString().padStart(2,'0')}." +
                d.year.toString().padStart(4,'0')
    }

    fun LocalDateTime.toUiTime(): String =
        "${hour.toString().padStart(2,'0')}:${minute.toString().padStart(2,'0')}"


}